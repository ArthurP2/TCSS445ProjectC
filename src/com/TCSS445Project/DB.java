package com.TCSS445Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Arthur on 3/8/2017.
 */
public class DB {

    private static String serverName = "cssgate.insttech.washington.edu";
    private static Connection conn;
    private static String userName = "apanlili"; //Change to yours
    private static String password = "kollunn~";


    public DB(){
    }

    public void start(){
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        try {
            conn = DriverManager.getConnection("jdbc:" + "mysql" + "://"
                    + serverName + "/", connectionProps);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void close(){
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public User getUserinfo(String username){
        String query = "SELECT * FROM apanlili.user WHERE " +
                "username='" + username + "';";
        int isBanned = 0;
        User user = new User(0,null,null,null,
                null,null,0,0);
        try {
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            try {
                while(rs.next()){
                    user.setUserID(rs.getInt(1));
                    user.setName(rs.getString(2));
                    user.setUsername(rs.getString(3));
                    user.setPassword(rs.getString(4));
                    user.setEmail(rs.getString(5));
                    user.setPhoneNumber(rs.getString(6));
                    user.setIsBanned(rs.getInt(7));
                    user.setType(rs.getInt(8));

                }
            } catch (Exception e){
                System.out.println("AA" + e);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }


}
