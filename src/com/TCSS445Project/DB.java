package com.TCSS445Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    public boolean addItem(Item item){
        String query = "INSERT INTO apanlili.item (sellerID, name, description, quantity, price, " +
                "conditionType, size, comment) VALUES ('" + item.getSellerID() + "', '" + item.getName() + "', '" + item.getDescription() + "'" +
                ", '" + item.getQuantity() + "', '" + item.getPrice() + "', '" + item.getConditionType() + "'" +
                ", '" + item.getSize() + "', '" + item.getComment() + "');";
        System.out.println(query);
        boolean noProblem = false;
        try {
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            noProblem = true;
        }catch (Exception e) {
            System.out.println(e);
        }
        return noProblem;
    }

    public ArrayList<Item> getMyStoreItems(int sellerID){
        String query = "SELECT * FROM apanlili.item WHERE " +
                "sellerID='" + sellerID + "';";
        System.out.println(query);
        ArrayList<Item> list = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            try {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemID(rs.getInt(1));
                    item.setSellerID(rs.getInt(2));
                    item.setName(rs.getString(3));
                    item.setDescription(rs.getString(4));
                    item.setQuantity(rs.getInt(5));
                    item.setPrice(rs.getDouble(6));
                    item.setConditionType(rs.getString(7));
                    item.setSize(rs.getString(8));
                    item.setComment(rs.getString(9));
                    list.add(item);
                }
            } catch (Exception e) {
            }
        } catch (Exception A) {

        }
        System.out.println("SIZE"+ list.size());
        return list;
    }


}
