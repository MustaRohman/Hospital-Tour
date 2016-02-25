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

public class loginQuery extends AsyncTask<String,Void,HashMap<String,String>> {
    @Override
    //This does the connection protocol in the background.

    protected HashMap<String,String> doInBackground(String... params) {
        // to retrive the query result.
        String query = params[0];
        HashMap<String, String> retval = new HashMap<>();
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