package com.TCSS445Project;

import java.sql.*;
import java.util.Properties;


public class Main {

    private static String serverName = "cssgate.insttech.washington.edu";
    private static Connection conn;
    private static String userName = "apanlili"; //Change to yours
    private static String password = "kollunn~";

    public static void main(String[] args) {
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        try {
            conn = DriverManager.getConnection("jdbc:" + "mysql" + "://"
                    + serverName + "/", connectionProps);
            System.out.println("Connected to database");
            Statement stmt = null;
            String username = "Jimbob";
            String query = "SELECT COUNT(*) FROM apanlili.user WHERE username='" + username + "';";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            try {
                while (rs.next())
                System.out.println(rs.getInt(1));

            } catch (Exception e ) {
                System.out.println(e);
            }

        } catch (SQLException e){
            System.out.print(e.toString());
        }

        GUI gui = new GUI();
        gui.start();
    }

}

