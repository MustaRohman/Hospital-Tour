package database;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by mustarohman on 24/02/2016.
 */

   public class DBQueryAsyncTask extends AsyncTask<String,Void,ArrayList<String>> {
    @Override
    //This does the connection protocol in the background.

    protected ArrayList<String> doInBackground(String... params) {
        // to retrive the query result.
        String query = params[0];
        ArrayList<String> retval = new ArrayList<>();
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