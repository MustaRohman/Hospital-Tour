package com.example.mustarohman.prototype;

import android.test.AndroidTestCase;

import com.example.mustarohman.prototype.Backend.DataBase.DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Yahya on 15/03/16.
 * Test used to see if a connection to the database can be established
 */
public class databaseConnection extends AndroidTestCase {

    private static Connection conn;
    private static Statement st;
    boolean reachable = false;

    public void testTestConnection() throws Exception {
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

        try {
            //if connection lasts x amount of time
            reachable = conn.isValid(5);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertThat(Boolean.TRUE, is(reachable));
    }
}
