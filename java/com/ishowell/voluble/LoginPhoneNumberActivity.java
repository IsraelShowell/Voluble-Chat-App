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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText moblieNumber;

    Button sendOTPBtn;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);

        //Assigns the variables to the layout ids
        countryCodePicker = findViewById(R.id.login_countrycode);
        moblieNumber = findViewById(R.id.login_mobilenumber);
        sendOTPBtn = findViewById(R.id.login_sendotp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        //Disables progress bar at the start of the activity
        progressBar.setVisibility(View.GONE);

        //Registers the phone number to the country code picker
        countryCodePicker.registerCarrierNumberEditText(moblieNumber);

        //Sets the onclick listener for the send otp button
        sendOTPBtn.setOnClickListener((v) -> {
            //Checks if the phone number is valid, if not, the program will inform the user
            if(!countryCodePicker.isValidFullNumber()){
                moblieNumber.setError("Moblie Number is invalid! :( ");
                return;
            }
            //Allows the app to navigate to the next activity
            Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOTPActivity.class);

            //This passes the phone number to the next activity
            //This is kind of like when I passed variable to HTML from Python and Flask
            intent.putExtra("mobileNumber", countryCodePicker.getFullNumberWithPlus());
            //Starts the next activity
            startActivity(intent);
        });

    }
}