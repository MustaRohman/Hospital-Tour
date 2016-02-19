package geofences;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mustarohman on 04/02/2016.
 */
public class GeofenceController {
    private final String TAG = GeofenceController.class.getName();

    private static Context context;
    private GoogleApiClient googleApiClient;
    private Gson gson;
    private SharedPreferences prefs;
    private GeofenceControllerListener listener;

    private GoogleApiClient.ConnectionCallbacks connectionAddListener =
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    // 1. Create an IntentService PendingIntent
                    Intent intent = new Intent(context, AreWeThereIntentService.class);
                    PendingIntent pendingIntent =
                            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // 2. Associate the service PendingIntent with the geofence and call addGeofences
                    PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(
                            googleApiClient, getAddGeofencingRequest(), pendingIntent);

                    // 3. Implement PendingResult callback
                    result.setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                // 4. If successful, save the geofence
                                saveGeofence();
                            } else {
                                // 5. If not successful, log and send an error
                                Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
                                        " : " + status.getStatusCode());
                                sendError();
                            }
                        }
                    });
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            };

    private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {

                }
            };

    private List<NamedGeofence> namedGeofences;

    public GeofenceController(Context context) {
        this.context = context;
    }

    public List<NamedGeofence> getNamedGeofences() {
        return namedGeofences;
    }

    private List<NamedGeofence> namedGeofencesToRemove;

    private Geofence geofenceToAdd;
    private NamedGeofence namedGeofenceToAdd;

    private static GeofenceController INSTANCE;

    public static GeofenceController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeofenceController(context);
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();

        gson = new Gson();
        namedGeofences = new ArrayList<>();
        namedGeofencesToRemove = new ArrayList<>();
        prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        loadGeofences();
    }

    public interface GeofenceControllerListener {
        void onGeofencesUpdated();
        void onError();
    }

    private GeofencingRequest getAddGeofencingRequest() {
        List<Geofence> geofencesToAdd = new ArrayList<>();
        geofencesToAdd.add(geofenceToAdd);
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofencesToAdd);
        return builder.build();
    }

    private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
        googleApiClient.connect();
    }

    private void sendError() {
        if (listener != null) {
            listener.onError();
        }
    }

    private void saveGeofence() {
        namedGeofences.add(namedGeofenceToAdd);
        if (listener != null) {
            listener.onGeofencesUpdated();
        }
        String json = gson.toJson(namedGeofenceToAdd);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(namedGeofenceToAdd.id, json);
        editor.apply();
    }

    public void addGeofence(NamedGeofence namedGeofence, GeofenceControllerListener listener) {
        this.namedGeofenceToAdd = namedGeofence;
        this.geofenceToAdd = namedGeofence.geofence();
        this.listener = listener;

        connectWithCallbacks(connectionAddListener);
    }

    private void loadGeofences() {
        // Loop over all geofence keys in prefs and add to namedGeofences
        Map<String, ?> keys = prefs.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            String jsonString = prefs.getString(entry.getKey(), null);
            NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
            namedGeofences.add(namedGeofence);
        }

        // Sort namedGeofences by name
        Collections.sort(namedGeofences);
    }

}
