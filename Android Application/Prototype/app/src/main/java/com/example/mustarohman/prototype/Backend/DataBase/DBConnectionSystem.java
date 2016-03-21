package com.example.mustarohman.prototype.Backend.DataBase;

<<<<<<< HEAD
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
=======
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
>>>>>>> 681320a01b75abd1239159bfc6d46aef6c81ebcb

/**
 * Created by yezenalnafei on 25/02/2016.
 */
public class DBConnectionSystem {
    private static Connection conn;
    private static Statement st;
    public static String tourCodeGlobal = "";


    public DBConnectionSystem() {

    }


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


    public HashMap<String, String> loginQueryFetch(String query) throws ExecutionException, InterruptedException {

        loginQuery loginQuery = new loginQuery();
        return loginQuery.execute(query).get();
    }

    //this method used to pass update queries to the database
    public boolean UpdateDatabase(String query) throws ExecutionException, InterruptedException {
        UpdateQuery updateQuery = new UpdateQuery();

        return updateQuery.execute(query).get();
    }

    public ArrayList<String> getTourCodes() throws ExecutionException, InterruptedException {
        return new TourCodesListQuery().execute().get();
    }

    private class loginQuery extends AsyncTask<String, Void, HashMap<String, String>> {
        @Override
        //This does the connection protocol in the background.
        protected HashMap<String, String> doInBackground(String... params) {
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
                while (rs.next()) {
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
            sql = "SELECT DISTINCT ON (location.locationid) * from usertour, tour_res, location_res, location where usertour.tourid = ? and " +
                    "usertour.tourid = tour_res.tourid and tour_res.locationid = location_res.locationid " +
                    "and location_res.username = usertour.username and location_res.locationid = location.locationid; ";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, tourCode);
            result = statement.executeQuery();
            while (result.next()) {
                retval.add(new TourLocation(result.getInt("locationid"), result.getString("lname"), result.getFloat("latitude"), result.getFloat("logitude")));
            }
            result.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retval;
    }


    private class UpdateQuery extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
//Here we could add progress bar,
        }

        //this will allow the user to insert/update the database
        @Override
        protected Boolean doInBackground(String... params) {
            connectionDriver();
            boolean check = false;

            try {

                PreparedStatement statement = conn.prepareStatement(params[0]);
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

    private class TourCodesListQuery extends AsyncTask<Void, Void, ArrayList<String>> {
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

    public static ArrayList<TourLocation> locationMediaQuery(ArrayList<TourLocation> arrayList) {

        String[] imageExt = {"tif", "png", "jpeg", "jpg", "gif"};
        String[] vidExt = {"m4v", "mp4"};

        connectionDriver();

        ResultSet result = null;

        try {
            PreparedStatement statement = conn.prepareStatement("select location_res.locationid, media.mediaid, ext_name, description, media_type, media_name " +
                    "from usertour, tour_res, location_res, media where usertour.tourid ='TOR124' and " +
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
            result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return arrayList;

    }
}















