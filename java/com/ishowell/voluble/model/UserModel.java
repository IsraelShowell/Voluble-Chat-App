/*
# Creator: Israel Showell
# Start Date: 6/7/2024
# End Date: 6/20/2024
# Project: Voluble - Chat App
# Version: 1.00

# Description:
This is a chat application made in Java and the backend Firebase!
This project provided me with basic knowledge on how to structure an Android app, use Firebase as a backend, and developing a functional and useful application.
Below are some of the programming skills that this project has helped me practice!

# Practiced Skills:
- Java Development
- Android Development
- UI/UX Design
- Firebase, Firestore, Realtime Databases
- Android Studio
- Github
- Error/Debugging Skills

I followed a great tutorial from @Easy Tuto!
https://www.youtube.com/@EasyTuto1

Although only a year old, Android Studio has changed a lot since then, but his tutorial is still a great reference!
*/

package com.ishowell.voluble.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserModel {
    private String username;
    private String phone;
    private Timestamp DateAccountCreated;

    private String userId;

    private String fcmtoken;

    //Default constructor
    public UserModel(){

    }

    //Constructor used to set the user's account up
    public UserModel(String username, String phone, Timestamp DateAccountCreated, String userId) {
        this.username = username;
        this.phone = phone;
        this.DateAccountCreated = DateAccountCreated;
        this.userId = userId;
    }

    //Below are the various getter and setters used to access the user's account information
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getDateAccountCreated() {
        return DateAccountCreated;
    }

    public void setDateAccountCreated(Timestamp dateAccountCreated) {
        DateAccountCreated = dateAccountCreated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFormattedDateAccountCreated() {
        if (DateAccountCreated != null) {
            //Converts Timestamp to Date
            Date date = DateAccountCreated.toDate();

            //Formats the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    public String getFCMToken() {
        return fcmtoken;
    }

    public void setFCMToken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }
}
