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
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.AndroidUtil;
import com.ishowell.voluble.utils.FirebaseUtil;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    //Declaring variables
    ImageView profilePic;
    EditText username;
    EditText PhoneNumber;

    Button updateBtn;

    ProgressBar progressBar;
    TextView logoutBtn;

    UserModel currentUser;

    //This enables the user to pick an image to use as their profile picture
    ActivityResultLauncher<Intent> imagePickLauncher;

    //Uri = Uniform Resource Identifier
    /*
    *  It is a string of characters that uniquely identifies a resource.
    * In Android development, URIs are commonly used to locate and
    * access various types of data and resources. Like images!
    * */
    Uri selectedImage;

    public ProfileFragment() {
        //Required empty public constructor
    }

    //This overridden function allows the app to detect when the activity is created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
            //If result is ok, that means an image has been selected!
                    if(result.getResultCode() == getActivity().RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            //This get the uri from the user's selected image
                            selectedImage = data.getData();
                            //This sets the profile picture to the selected image
                            AndroidUtil.setProfilepic(getContext(), selectedImage, profilePic);
                        }
                    }
                }
                );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Initializing variables to correct UI elements
        profilePic = view.findViewById(R.id.profile_image_view);
        username = view.findViewById(R.id.profile_username);
        PhoneNumber = view.findViewById(R.id.profile_phone);
        updateBtn = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.profile_logout_btn);

        //Calls the function to fill out the text fields on the profile screen
        getuserdata();

        //Sets on click listeners for the update profile button
        updateBtn.setOnClickListener(v -> {
            updateBtnclick();
        });

        //This logs the user out of the app
        //and takes them back to the Splash screen
        logoutBtn.setOnClickListener(v -> {
            //This deletes the token from Firebase after a user has logged out
            //A new token will be created when the user logs in again
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FirebaseUtil.logout();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        });

        profilePic.setOnClickListener(v -> {
            //The ImagePicker library is used to handle image selection
            //The ImagePicker is called with the current context

            /*
            The cropping is set to a square shape
            The image is compressed to a maximum of 512 KB
            The image is also set to a maximum size of 512 x 512 pixels
            */
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    //Creates the intent for picking the image
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            //This launches the intent with a predefined ActivityResultLauncher
                            imagePickLauncher.launch(intent);
                            //Returns null as since the Function1 interface needs to
                            return null;
                        }
                    });
        });

        return view;
    }

    //This function is called when the update button is clicked
    //It calls the backend function to update the user data and checks the username
    void updateBtnclick(){
        String newUsername = username.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 4) {
            username.setError("Your new username needs to be at least 4 characters long!");
            return;
        } else if (newUsername.length() > 20) {
            username.setError("Your new username needs to be at less than 20 characters long!");
            return;
        }
        //This sets the new username to the current user
        currentUser.setUsername(newUsername);
        setInProgress(true);

        //Checks to make sure the user has actually chosen an image.
        //If they haven't, that means they are just updating their username or have misclicked
        if(selectedImage == null){
            UpdateFirestoreData();
        }else{
            FirebaseUtil.getCurrentProfilePicStorageReference().putFile(selectedImage)
                    .addOnCompleteListener(task -> {
                        //This calls the backend function to update the user data
                        UpdateFirestoreData();
                        setInProgress(false);
                    });
        }
    }


    //This function updates the user data in Firestore
    //This is another backend function
    void UpdateFirestoreData(){
        FirebaseUtil.currentUserDetails().set(currentUser)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(), "Profile has been updated successfully!");
                    }else{
                        AndroidUtil.showToast(getContext(), "Failed to update profile! :(");
                    }
                });
    }

    //This retrieves the user data from Firestore and passes it to the profile screen
    void getuserdata(){

        //Controls the UI appearance
        setInProgress(true);

        //This gets the profile picture from Firestore,
        //So that the profile screen can see the custom profile picture
        FirebaseUtil.getCurrentProfilePicStorageReference().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilepic(getContext(), uri, profilePic);
                    }
                });

        //Getting user details from Firebase.
        //This form is called Lambda, a shorthand version of the complete call.
        //I used this throughout the program!
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);
            setInProgress(false);
            //Checking if the currentuser is null or not.
            if(currentUser == null){
                return;
            }else{
                username.setText(currentUser.getUsername());
                PhoneNumber.setText(currentUser.getPhone());
            }

        });
    }

    void setInProgress(boolean inProgress) {

        //If the OTP is in progress, show the progress bar and hide the send button
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            updateBtn.setVisibility(View.GONE);
        } else {
            //If the OTP is not progress, hide the progress bar and show the send button
            progressBar.setVisibility(View.GONE);
            updateBtn.setVisibility(View.VISIBLE);
        }
    }

}