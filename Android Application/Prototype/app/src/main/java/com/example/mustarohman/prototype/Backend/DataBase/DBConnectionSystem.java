package com.example.mustarohman.prototype.Backend.DataBase;

import android.os.AsyncTask;

import com.example.mustarohman.prototype.Backend.Objects.TourLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by yezenalnafei on 25/02/2016.
 */
public class DBConnectionSystem {
     Connection conn;
    Statement st;


    public DBConnectionSystem() {

    }


    public void connectionDriver() {

        try {
            //opens the jar.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //this is url for the database host, using ssl communication.
        String url = DBConnect.url;

        try {
            //this logs in to get data from database.
            DriverManager.setLoginTimeout(5);
            //gets the url and open a port.
            conn = DriverManager.getConnection(url);
            st = conn.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, String> loginQueryFetch(String query) throws ExecutionException, InterruptedException {

        loginQuery loginQuery = new loginQuery();
        return loginQuery.execute(query).get();
    }

    public   ArrayList<TourLocation> getTourlocations(String query) throws ExecutionException, InterruptedException {

        TourQuery getTourlocations = new TourQuery();
        return getTourlocations.execute(query).get();
    }
    //this method used to pass update queries to the database
    public void UpdateDatabase(String query){
        UpdateQuery updateQuery = new UpdateQuery();
        updateQuery.execute(query);
    }

     private  class loginQuery extends AsyncTask<String,Void,HashMap<String,String>> {
            @Override
            //This does the connection protocol in the background.
            protected HashMap<String,String> doInBackground(String... params) {
                // to retrive the query result.
                String query = params[0];
                HashMap<String, String> retval = new HashMap<>();
                try {
                    connectionDriver();
                    //this logs in to get data from database.
                    String sql;
                    //this is query.
                    sql = query;
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()) {
                        retval.put(rs.getString("username"), rs.getString("password"));
                    }
                    rs.close();
                    st.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return retval;
            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
}


    private  class TourQuery extends AsyncTask<String,Void,ArrayList<TourLocation>> {
        @Override
        //This does the connection protocol in the background.
        protected ArrayList<TourLocation> doInBackground(String... params) {
            // to retrive the query result.
            String query = params[0];
            ArrayList<TourLocation> retval = new ArrayList<>();
            try {
                connectionDriver();
                //this logs in to get data from database.
                String sql;
                //this is query.
                sql = query;
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()) {
                    retval.add(new TourLocation(rs.getInt("locationid"), rs.getString("lname"), rs.getFloat("latitude"), rs.getFloat("logitude")));
                }
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return retval;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private class UpdateQuery extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
//Here we could add progress bar,
        }
//this will allow the user to insert/update the database
        @Override
        protected String doInBackground(String... params) {
            connectionDriver();

            try {
                //This will pass the query to the database
                st.executeQuery(params[0]);
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }


}
















