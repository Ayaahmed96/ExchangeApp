package com.example.exchangeapp;

import java.util.Date;

public class Users {
    private String Username;
    private String userEmail;
    private String userPassword;
    private String location;
    private boolean gender;
    private String profile_pic_id;


    public Users(String username, String userEmail, String userPassword, String location, boolean gender, String profile_pic_id) {
        Username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.location = location;
        this.gender = gender;
        this.profile_pic_id = profile_pic_id;

    }

    public Users(){

    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUsername(String username) {
        Username = username;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return Username;
    }


    public String getLocation() {
        return location;
    }

    public boolean isGender() {
        return gender;
    }


    public String getProfile_pic_id() {
        return profile_pic_id;
    }

    public void setProfile_pic_id(String profile_pic_id) {
        this.profile_pic_id = profile_pic_id;
    }
}
