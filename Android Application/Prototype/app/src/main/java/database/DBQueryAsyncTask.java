package database;

import android.os.AsyncTask;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by mustarohman on 24/02/2016.
 */

   public class DBQueryAsyncTask extends AsyncTask<String,Void,HashMap<String,String>> {


    public static HashMap<String,String> retrieveTours(String query) {
        // to retrive the query result.
        HashMap<String,String>  retval = new HashMap<String,String>();
        try {
            //opens the jar.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //this is url for the database host, using ssl communication.
        String url = DBConnect.url;
        Connection conn;
        try {
            //this logs in to get data from database.
            DriverManager.setLoginTimeout(5);
            //gets the url and open a port.
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql;
            //this is query.
            sql = query;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                retval.put(rs.getString("tourid"),rs.getString("tour_name"));
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
    protected HashMap<String, String> doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }



}