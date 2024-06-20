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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import com.ishowell.voluble.utils.AndroidUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity {

    //Defined functions
    void sendOTP(String phoneNumber, boolean isResent) {
        startResendtimer();
        //Shows/hides the progress bar and button
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        //Sets the phone number for the OTP
                        .setPhoneNumber(phoneNumber)
                        //Sets the timeout for the OTP
                        .setTimeout(timeoutseconds, TimeUnit.SECONDS)
                        //Sets the current activity to this
                        .setActivity(this)


                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                //Handles successful verification
                                signIn(phoneAuthCredential);
                                //Shows/hides the progress bar and button
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                //Shows failure message
                                AndroidUtil.showToast(getApplicationContext(), "OTP Verification Failed! :(");

                                //Shows/hides the progress bar and button
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);

                                //Stores the verification code
                                verificationCode = s;
                                //Stores the resending token
                                ResendingToken = forceResendingToken;

                                //Shows failure message
                                AndroidUtil.showToast(getApplicationContext(), "OTP Sent Successfully! :)");
                                //Shows/hides the progress bar and button
                                setInProgress(false);
                            }
                        });
        //Checks if the OTP is being resent or not
        if (isResent) {
            //Resends the OTP to the phone number
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
        } else {
            //Sends the OTP to the phone number
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }
    void setInProgress(boolean inProgress) {

        //If the OTP is in progress, show the progress bar and hide the send button
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            sendBtn.setVisibility(View.GONE);
        } else {
            //If the OTP is not progress, hide the progress bar and show the send button
            progressBar.setVisibility(View.GONE);
            sendBtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneauthcred) {
        setInProgress(true);
        mAuth.signInWithCredential(phoneauthcred).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginOTPActivity.this, LoginUsernameActivity.class);
                    intent.putExtra("mobileNumber", phoneNumber);
                    startActivity(intent);
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "OTP Verification Failed! :(");
                    setInProgress(false);
                }

            }
        });
    }
    //This function is used to start the timer for the OTP and reset it
    void startResendtimer() {
        //Disables the resend button until timer is done
        resendOTP.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //Updates the text on the resend button every second
                timeoutseconds--;
                resendOTP.setText("Resend OTP in " + timeoutseconds + " seconds");
                if (timeoutseconds == 0) {
                    timeoutseconds = 45L;
                    timer.cancel();
                    //Enables the resend button and resets the text
                    runOnUiThread(() -> {
                        resendOTP.setEnabled(true);
                        resendOTP.setText("Resend OTP");
                    });
                }
            }
        },0,1000);
    }

//Define variables
    String phoneNumber;
    //OTP validity time in seconds
    Long timeoutseconds = 45L;

    String verificationCode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;
    EditText OTPText;
    Button sendBtn;
    ProgressBar progressBar;
    TextView resendOTP;

    // Firebase Authentication instance
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //Code called on runtime
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otpactivity);

        //Initializes the variables to their correct ids
        OTPText = findViewById(R.id.login_otp);
        sendBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_otp_progress_bar);
        resendOTP = findViewById(R.id.resend_otp);

        //Gets the phone number from the Intent passed from the LoginPhoneNumberActivity
        phoneNumber = getIntent().getStringExtra("mobileNumber");

        //Displays the phone number in a short toast message
        //Toasts are short messages that appear on the screen for a long period of time, about 3.5 seconds
        //Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_LONG).show();

        //Calls the send OTP function
        sendOTP(phoneNumber, false);

        //Lambda version of the set on click listener
        //This takes the enter OTP, compares it to the one stored in the verification code
        //and then calls the signIn function if it is valid.
        //The setInProgress function is called to hide UI features
        sendBtn.setOnClickListener(v -> {
            String enteredOTP = OTPText.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOTP);
            signIn(credential);
            setInProgress(true);
        });

        //Lambda version of the set on click listener
        //This allows users to resend the OTP by clicking the resend OTP button
        resendOTP.setOnClickListener(v -> {
            sendOTP(phoneNumber, true);
        });
    }
}