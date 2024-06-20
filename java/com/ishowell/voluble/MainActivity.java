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


package com.ishowell.voluble;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.appcheck.FirebaseAppCheck;
//import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ishowell.voluble.utils.FirebaseUtil;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;

    ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Initializes the Firebase App Check with the Play Integrity App Check provider.
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());*/

        //Initialize fragments and variables to ids
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView = findViewById(R.id.bottom_navi);
        searchButton = findViewById(R.id.main_search_btn);

        //Pressing the search button will bring up the search user activity
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchUserActivity.class));
        });

        /*
        * Detects when the bottom navigation bar is pressed.
        * Depending on which icon is pressed, the corresponding fragment is displayed.*/
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.menu_chat) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, chatFragment).commit();
            }
            else if(item.getItemId() == R.id.menu_profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, profileFragment).commit();
            }
            return true;
        });

        //By default the chat fragment is on
        bottomNavigationView.setSelectedItemId(R.id.menu_chat);

        //Gets the token of the device, and saves the token to the database
        getFCMToken();

    }

    //Gains the token of the device and saves it to the database
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                String token = task.getResult();
                FirebaseUtil.currentUserDetails().update("fcmtoken", token);
            }
        });
    }
}