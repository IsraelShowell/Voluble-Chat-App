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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.FirebaseUtil;


public class LoginUsernameActivity extends AppCompatActivity {

    //Define functions

    //Create variables
    EditText username;
    Button login_btn;
    ProgressBar progressBar;
    String mobileNumber;
    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        //Assign variables to their correct ids
        username = findViewById(R.id.login_username);
        login_btn = findViewById(R.id.login_speak_up_with_Voluble_btn);
        progressBar = findViewById(R.id.username_progress_bar);

        //Gets the number passed from the OTP activity
        mobileNumber = getIntent().getStringExtra("mobileNumber");
        //Calls the getUsername function
        getUsername();

        //Sets username after button is clicked.
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUsername();
            }
        });
    }

    void setInProgress(boolean inProgress) {

        //If the OTP is in progress, show the progress bar and hide the send button
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        } else {
            //If the OTP is not progress, hide the progress bar and show the send button
            progressBar.setVisibility(View.GONE);
            login_btn.setVisibility(View.VISIBLE);
        }
    }

    //Sets the username in the database
    void setUsername() {
        String user_name = username.getText().toString();
        if (user_name.isEmpty() || user_name.length() < 4) {
            username.setError("Your username needs to be at least 4 characters long!");
            return;
        } else if (user_name.length() > 20) {
            username.setError("Your username needs to be at less than 20 characters long!");
            return;
        }
        setInProgress(true);

        //Checks userModel is already populated from the getUsername call
        if (userModel != null) {
            userModel.setUsername(user_name);
            updateOrSetUserModel();
        } else {
            //Checks to see if the user exists in the database to keep the creation date
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        //If the user exists, the userModel is retrieved and the username is updated
                        userModel = task.getResult().toObject(UserModel.class);
                        if (userModel != null) {
                            userModel.setUsername(user_name);
                        }
                    } else {
                        //If there is no existing user, a new UserModel with a new timestamp is made
                        userModel = new UserModel(user_name, mobileNumber, Timestamp.now(), FirebaseUtil.currentUserId());
                    }
                    updateOrSetUserModel();
                }
            });
        }
    }

    //This function is called by the setUsername method to update or set user model in Firestore
    void updateOrSetUserModel() {
        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    navigateToMainActivity();
                } else {
                    //Handles errors
                }
            }
        });
    }

    //Navigates to the main activity
    void navigateToMainActivity() {
        Intent intent = new Intent(LoginUsernameActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Gets the username from the text field and to check it against the database
    void getUsername(){
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);

                //Turns the UserModel into an object so it can be moved around
                if(task.isSuccessful()){
                    UserModel userModel = task.getResult().toObject(UserModel.class);
                    if(userModel != null){
                        username.setText(userModel.getUsername());
                    }
                }
            }
        });
    }
    //End of functions
}