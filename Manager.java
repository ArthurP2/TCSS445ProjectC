package com.TCSS445Project;
/**
 * Created by Biirhanu on 3/8/2017.
 */
public class Manager {

	    int managerID;
	    String name;
	    String username;
	    String password;
	    String email;
	    String phoneNumber;
	   

	    public Manager(int theUserID, String theName, String theUsername, String thePassword,
	                String theEmail, String thePhoneNumber){
	        managerID = theUserID;
	        name = theName;
	        username = theUsername;
	        password = thePassword;
	        email = theEmail;
	        phoneNumber = thePhoneNumber;
	       
	    }

	    public int getManagerID() {
	        return managerID;
	    }

	    public void setManagerID(int mID) {
	        this.managerID = mID;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getManagername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	 
	}


