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

    public boolean addToCart(int userID, int itemID){
        String query = "INSERT INTO apanlili.shoppingCart (buyerID, itemID)" +
                " VALUES (" + userID + ", " + itemID + ");";
        System.out.println(query);
        boolean success = false;
        try {
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            success = true;
        }catch (Exception e) {
            System.out.println(e);
        }
        return success;
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

    public ArrayList<Item> getMyCartItems(int buyerID){
        String query = "SELECT i.itemID, sellerID, `name`, `description`, price, `quantity`, conditionType, size, `comment` " +
                        "FROM apanlili.item AS i " +
                        "LEFT OUTER JOIN apanlili.shoppingCart AS sc " +
                        "ON i.itemID=sc.itemID " +
                        "WHERE buyerID=" + buyerID + ";";
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



    public boolean removeItem(int itemID, int userID){
            String query = "SELECT * FROM apanlili.item WHERE itemID='" + itemID + "';";
            System.out.println("FINDITEM" + query);
        boolean noProblem = checkItemExists(itemID, userID);

        if (noProblem){
           query = "DELETE FROM apanlili.item WHERE itemID='" + itemID + "';";
            System.out.println(query);
            try {
                Statement stmt = null;
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                noProblem = true;
            }catch (Exception e) {
                noProblem = false;
                System.out.println(e);
            }
        }
        return noProblem;
    }

    public boolean checkItemExists(int itemID, int userID) {
        String query = "SELECT * FROM apanlili.item WHERE (itemID='" + itemID + "' AND sellerID='" + userID + "');";
        System.out.println(query);
        boolean noProblem = false;
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
                if ((rs.getInt(1) > 0)) {
                    noProblem = true;
                }
        } catch (Exception e) {
            System.out.println(e);
        }
        return noProblem;
    }

    public boolean editItem(int itemID, int sellerID, Item theItem, boolean array[]) {
        String query = "SELECT * FROM apanlili.item WHERE " +
                "(itemID='" + itemID + "' AND sellerID='" + sellerID + "');";
        System.out.println(query);
        System.out.println("SIZE" + theItem.getSize() + array[5]);
        Item item = new Item();
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                item.setItemID(theItem.getItemID());
                item.setSellerID(theItem.getSellerID());
                System.out.println("1");
                if (array[0])
                    item.setName(theItem.getName());
                else
                    item.setName(rs.getString(3));
                System.out.println("2");
                if (array[1])
                   item.setDescription(theItem.getDescription());
                else
                    item.setDescription(rs.getString(4));
                System.out.println("3");
                if (array[2])
                    item.setQuantity(theItem.getQuantity());
                else
                    item.setQuantity(rs.getInt(5));
                System.out.println("4");
                if (array[3])
                    item.setPrice(theItem.getPrice());
                else
                    item.setPrice(rs.getDouble(6));
                System.out.println("5");
                if (array[4])
                    item.setConditionType(theItem.getConditionType());
                else
                    item.setConditionType(rs.getString(7));
                System.out.println("6");
                if (array[5])
                    item.setSize(theItem.getSize());
                else
                    item.setSize(rs.getString(8));
                System.out.println("7");
                if (array[6])
                    item.setComment(theItem.getComment());
                else
                    item.setComment(rs.getString(9));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        query = "UPDATE apanlili.item SET name='" + item.getName() + "', description='" + item.getDescription() + "', quantity='" + item.getQuantity() + "'," +
                " price='" + item.getPrice() + "', conditionType='" + item.getConditionType() + "', size='" + item.getSize() + "', comment='" + item.getComment() + "'" +
                " WHERE itemID='" + itemID + "' AND sellerID='" + sellerID + "';";
        System.out.println(query);
        boolean noProblem = false;
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            noProblem = true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return noProblem;
    }

    public boolean removeFromCart(int userID, int itemID){
        String query = "SELECT * FROM apanlili.shoppingCart WHERE itemID=" + itemID + " AND buyerID=" + userID + ";";
        System.out.println("FINDITEM" + query);
        boolean noProblem = false;
        try {
            Statement stmt = null;
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
                if ((rs.getInt(1) > 0)) {
                    noProblem = true;
                }
        }catch (Exception e) {
            System.out.println(e);
        }
        if (noProblem){
            query = "DELETE FROM apanlili.shoppingCart WHERE itemID=" + itemID + " AND buyerID=" + userID + ";";
            System.out.println("DESTORYITEM" + query);
            try {
                Statement stmt = null;
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                noProblem = true;
            }catch (Exception e) {
                noProblem = false;
                System.out.println(e);
            }
        }
        return noProblem;
    }

    public ArrayList<User> getAllSellers() {
        String query = "SELECT * FROM apanlili.user WHERE " +
                "type = 2 AND isBanned = 0;";
        System.out.println(query);

        ArrayList<User> sellers = new ArrayList<User>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            try {
                while (rs.next()) {
                    User seller = new User();
                    seller.setUserID(rs.getInt(1));
                    seller.setName(rs.getString(2));
                    seller.setUsername(rs.getString(3));
                    seller.setPassword(rs.getString(4));
                    seller.setEmail(rs.getString(5));
                    seller.setPhoneNumber(rs.getString(6));
                    seller.setIsBanned(rs.getInt(7));
                    seller.setType(rs.getInt(8));
                    sellers.add(seller);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return sellers;
    }

}
