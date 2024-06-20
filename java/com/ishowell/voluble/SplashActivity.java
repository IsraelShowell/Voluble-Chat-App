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
import android.os.Handler;

import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.AndroidUtil;
import com.ishowell.voluble.utils.FirebaseUtil;


import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    //This is the function that is called by the program first!
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This sets the view to go to the activity_splash layout
        setContentView(R.layout.activity_splash);


        //This checks to see if the app was opened via a notification!
        //And that the user is logged in
        if(FirebaseUtil.isLogin() && getIntent().getExtras() != null){
            //From notification!
            String userID = getIntent().getExtras().getString("userId");
            FirebaseUtil.allUserCollectionReference().document(userID).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            UserModel model = task.getResult().toObject(UserModel.class);

                            //This prevents the error of sending the user back to the login screen if they click a notification
                            //to open the app, and then click the back button
                            Intent mainIntent = new Intent(this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(mainIntent);

                            //Navigates to chat activity
                            Intent intent = new Intent(this, ChatActivity.class);
                            //This passes the username into the chat activity
                            AndroidUtil.passUserModelAsIntent(intent, model);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

        }else{
            //This handler delays for a set amount of time, and then brings up the login activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLogin()){
                        //If we clear data of the application, this will be false
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                    }
                    finish();
                }
            }, 2000);
        }
    }
}