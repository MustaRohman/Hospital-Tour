package database;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by yezenalnafei on 21/02/2016.
 */
public class DBQuery extends Thread {


    public String query;


    /**
     * operation is true for insert and false for retrieval
     * @param query
     */
    public DBQuery(String query){
        this.query = query;
    }
    public void run(){

        try {
            //opens the jar.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            query = e.toString();
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
            if (rs != null) {
                while (rs.next()) {
                    System.out.println(rs.getString("tourid"));

                }
            }
            st.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
