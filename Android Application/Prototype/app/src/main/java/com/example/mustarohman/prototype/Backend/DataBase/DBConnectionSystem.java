package com.example.mustarohman.prototype.Backend.DataBase;


import android.os.AsyncTask;
import android.util.Log;

import com.example.mustarohman.prototype.Backend.Objects.Media;
import com.example.mustarohman.prototype.Backend.Objects.TourLocation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    private static Connection conn;
    private static Statement st;
    public static String tourCodeGlobal = "";



    public DBConnectionSystem() {

    }

    /**
     * Initialises the Postgresql driver Lib.
     */
    public static void connectionDriver() {

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

    /**
     * Get User login security details
     * @param query
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public HashMap<String, ArrayList<String>> loginQueryFetch(String query) throws ExecutionException, InterruptedException {

        loginQuery loginQuery = new loginQuery();
        return loginQuery.execute(query).get();
    }

    //this method used to pass update queries to the database
    public boolean UpdateDatabase(String locationName, double latitude, double longitude) throws ExecutionException, InterruptedException {
        UpdateQuery updateQuery = new UpdateQuery(locationName);

        return updateQuery.execute(latitude, longitude).get();
    }

    //testing purposes
    public ArrayList<String> getTestingTourCodes() throws ExecutionException, InterruptedException {
        return new TestingTourCodesListQuery().execute().get();
    }

    public HashMap<String,String> getTourCodes(String query) throws ExecutionException, InterruptedException {
        return new TourCodesListQuery().execute(query).get();
    }

    public ArrayList<String> getTourName() throws ExecutionException, InterruptedException{
        return new TourNames().execute().get();

    }


    //testing purposes
    public class TourNames extends AsyncTask<Void,Void,ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Void...params){
            String query = "Select * from tour;";

            ArrayList<String> retval = new ArrayList<>();
            try {
                connectionDriver();
                ResultSet rs = st.executeQuery(query);
                while(rs.next()){
                    retval.add(rs.getString("tour_name"));

                }
                rs.close();
                st.close();
                conn.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }

            return retval;
        }

        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);

        }

    }


    private class loginQuery extends AsyncTask<String, Void, HashMap<String, ArrayList<String>>> {
        @Override
        //This does the connection protocol in the background.
        protected HashMap<String, ArrayList<String>> doInBackground(String... params) {
            // to retrive the query result.
            String query = "select * from users where username = ?;";
            ResultSet rs = null;
            HashMap<String, ArrayList<String>> retval = new HashMap<>();
            try {
                connectionDriver();
                //this logs in to get data from database.
                String sql;
                //this is query.
                sql = query;
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, params[0]);
                rs = statement.executeQuery();
                ArrayList<String> loginData = new ArrayList<>();
                while (rs.next()) {
                    loginData.add(rs.getString("password"));
                    loginData.add(rs.getString("active"));
                    retval.put(rs.getString("username"),loginData);
                }
                System.out.println("Test111" + loginData.toString());
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

    /**
     * retrieve the data for input tour code,
     * @param tourCode
     * @return
     */

    public static ArrayList<TourLocation> retrieveTourLocations(String tourCode) {
        // to retrieve the query result.
        ArrayList<TourLocation> retval = new ArrayList<>();
        ResultSet result = null;
        tourCodeGlobal = tourCode;
        try {
            connectionDriver();
            //this logs in to get data from database.
            String sql;
            //this is query.
            sql = "SELECT DISTINCT ON (location.locationid) * from usertour, tour_res, location_res, location where usertour.tourid = ? and usertour.tourid = tour_res.tourid and tour_res.locationid = location_res.locationid and location_res.username = usertour.username and location_res.locationid = location.locationid;";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, tourCode);
            result = statement.executeQuery();
            while (result.next()) {
                retval.add(new TourLocation(result.getInt("locationid"), result.getString("lname"), result.getFloat("latitude"), result.getFloat("longitude")));
            }
            result.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retval;
    }

    /**
     * This used for all update queries need to be executed by the app.
     */
    private class UpdateQuery extends AsyncTask<Double, Void, Boolean> {

        private String locationName;

        public UpdateQuery(String locationName){
            this.locationName = locationName;
        }


        @Override
        protected void onPreExecute() {
//Here we could add progress bar,
        }

        //this will allow the user to insert/update the database
        @Override
        protected Boolean doInBackground(Double... params) {
            connectionDriver();
            boolean check = false;
            try {
                PreparedStatement statement = conn.prepareStatement("Insert into location (lname,latitude,longitude) values(?,?,?);");
                statement.setString(1, locationName);
                statement.setDouble(2, params[0]);
                statement.setDouble(3, params[1]);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    Log.d("UpdateQuery", "An existing user was updated successfully!");
                    check = true;
                }

                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.d("UpdateQuery", "error");
            }
            return check;
        }

//        @Override
//        protected void onPostExecute(String result) {
//
//        }
    }

    /**
     * This gets the tours code for the logged in user.
     */

    private class TourCodesListQuery extends AsyncTask<String, Void, HashMap<String,String>> {
        @Override
        //This does the connection protocol in the background.
        protected HashMap<String,String> doInBackground(String... params) {
            // to retrieve the query result.
            String query = "select * from usertour, tour  where usertour.username = ? and usertour.tourid = tour.tourid; ";
            HashMap<String,String> retval = new HashMap<>();
            ResultSet rs = null;
            try {
                connectionDriver();
                //this logs in to get data from database.
                //this is query.
                String sql = query;
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, params[0]);
                rs = statement.executeQuery();
                while (rs.next()) {
                    retval.put(rs.getString("tourid"), rs.getString("tour_name"));
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

    /**
     * Get the locations that belongs to tour code.
     * @param arrayList
     * @return
     */

    public static ArrayList<TourLocation> locationMediaQuery(ArrayList<TourLocation> arrayList) {

        String[] imageExt = {"tif", "png", "jpeg", "jpg", "gif"};
        String[] vidExt = {"m4v", "mp4"};

        connectionDriver();

        ResultSet result = null;

        try {
            PreparedStatement statement = conn.prepareStatement("select location_res.locationid, media.mediaid, ext_name, description, media_type, media_name " +
                    "from usertour, tour_res, location_res, media where usertour.tourid ='"+tourCodeGlobal+"' and " +
                    "usertour.tourid = tour_res.tourid and tour_res.locationid = location_res.locationid and location_res.mediaid = media.mediaid and " +
                    "location_res.username = usertour.username and location_res.locationid = ?;");

            for (TourLocation arrayListLocation : arrayList) {
                statement.setInt(1, arrayListLocation.getId());
                result = statement.executeQuery();
                ArrayList<Media> mediaArray = new ArrayList<>();

                while (result.next()) {
                    String name = result.getString("media_name");
                    String description = result.getString("description");
                    String inBucketName = result.getString("ext_name");
                    String mediaType = result.getString("media_type");
                    int mediaID = result.getInt("mediaid");

                    //Check datatype
                    Media.DataType dataType;
                    if (mediaType.toUpperCase().equals("IMAGE")){
                        dataType = Media.DataType.IMAGE;
                    } else dataType = Media.DataType.VIDEO;

//                    String[] nameSplit = name.split(".");
//                    String extension = MimeTypeMap.getFileExtensionFromUrl(name);
//                    Log.d("locationMediaQuery", )
//                    if (name.co) {
//                        dataType = Media.DataType.IMAGE;
//                    } else {
//                        dataType = Media.DataType.VIDEO;
//                    }
                    mediaArray.add(new Media(name, description, dataType, inBucketName, mediaID));
                }
                arrayListLocation.setMediaList(mediaArray);
            }

            st.close();
            conn.close();
            if (result != null) {
                result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayList;

    }
    //testing purposes
    private class TestingTourCodesListQuery extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        //This does the connection protocol in the background.
        protected ArrayList<String> doInBackground(Void... params) {
            // to retrieve the query result.
            String query = "Select * from tour;";
            ArrayList<String> retval = new ArrayList<>();
            try {
                connectionDriver();
                //this logs in to get data from database.
                //this is query.
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    retval.add(rs.getString("tourid"));
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

}















