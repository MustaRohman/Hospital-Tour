package com.example.mustarohman.prototype.Frontend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.mustarohman.prototype.Backend.GeoLocation;
import com.example.mustarohman.prototype.R;

/**
 * Created by mustarohman on 28/02/2016.
 */
public class AddTourPointDialog extends DialogFragment {

    private int MINIMUM_TIME_BETWEEN_UPDATES = 1500;
    private int MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private LocationManager locationManager;


    public void GeoLocation(){

        GeoLocation geoLocation = new GeoLocation();

        geoLocation.showCurrentLocation();
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_addtourpoint, null))
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}
