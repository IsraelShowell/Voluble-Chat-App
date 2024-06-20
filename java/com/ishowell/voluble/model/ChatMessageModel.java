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

public class ChatMessageModel {
    private String message;
    private String senderID;
    private Timestamp TimeMessageSent;

    public ChatMessageModel() {
        //Default constructor
    }

    public ChatMessageModel(String message, String senderID, Timestamp timeMessageSent) {
        this.message = message;
        this.senderID = senderID;
        TimeMessageSent = timeMessageSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimeMessageSent() {
        return TimeMessageSent;
    }

    public void setTimeMessageSent(Timestamp timeMessageSent) {
        TimeMessageSent = timeMessageSent;
    }
}
