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


package com.ishowell.voluble.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ishowell.voluble.model.UserModel;

public class AndroidUtil {

    //This function is used to show toast message across various parts of the program
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //Allows me to pass the user's information as an intent to the next activity
    public static void passUserModelAsIntent(Intent intent, UserModel usermodel){
        intent.putExtra("username", usermodel.getUsername());
        intent.putExtra("phone", usermodel.getPhone());
        intent.putExtra("userId", usermodel.getUserId());
    }

    //This function is used to get the user's information from the intent
    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel usermodel = new UserModel();
        usermodel.setUsername(intent.getStringExtra("username"));
        usermodel.setPhone(intent.getStringExtra("phone"));
        usermodel.setUserId(intent.getStringExtra("userId"));
        return usermodel;
    }

    //This function is used to set the profile picture of the user
    //Glide takes care of the circular shape of the profile picture
    public static void setProfilepic(Context context, Uri imageUri, ImageView profilepic){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(profilepic);
    }
}
