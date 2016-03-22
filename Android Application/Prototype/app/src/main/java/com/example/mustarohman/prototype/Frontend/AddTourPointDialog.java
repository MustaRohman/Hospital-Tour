package com.example.mustarohman.prototype.Frontend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnectionSystem;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;
import com.example.mustarohman.prototype.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by mustarohman on 28/02/2016.
 */
public class AddTourPointDialog extends DialogFragment {


    private LinearLayout linearLayout;
    @NonNull
    @Override

    /**
     *
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View builderView = inflater.inflate(R.layout.dialog_addtourpoint,null);
        builder.setView(builderView)
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

        linearLayout = (LinearLayout) builderView.findViewById(R.id.linear_room);
        getExsitingLocatons();
        return builder.create();

    }

    /**
     *
     */
    public void getExsitingLocatons(){

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        DBConnectionSystem dbConnectionSystem = new DBConnectionSystem();
        try {
           ArrayList<TourLocation> locationsList = new DBAsyncTask().execute("select * from location;").get();
            for (TourLocation tourLocation : locationsList){
                Button button = new Button(getContext());
                button.setOnClickListener(listener);
                button.setText(tourLocation.getName());
                linearLayout.addView(button);
                Log.d("e", tourLocation.getName());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    private class DBAsyncTask extends AsyncTask<String, String, ArrayList<TourLocation>> {
        @Override
        protected ArrayList<TourLocation> doInBackground(String... params) {
            ArrayList<TourLocation> locations = null;
            locations = DBConnectionSystem.retrieveTourLocations(params[0]);
            return locations;

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }


    }


}
