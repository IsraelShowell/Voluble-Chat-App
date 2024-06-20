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

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.ishowell.voluble.adapter.SearchUserRecyclerAdapter;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.FirebaseUtil;

public class SearchUserActivity extends AppCompatActivity {

    EditText searchInput;
    ImageButton searchButton;
    ImageButton backButton;
    RecyclerView recyclerView;

    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        //Initialize variables to their correct ids
        searchInput = findViewById(R.id.search_user_input);
        searchButton = findViewById(R.id.search_user_btn);
        backButton = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.search_user_recycler_view);

        //Causes the keyboard to come up first when the page is loaded
        searchInput.requestFocus();


        //This code calls the deprecated .onBackPressed method
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled */) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        //Used to help with the deprecated method
        getOnBackPressedDispatcher().addCallback(this, callback);

        //Handles the UI back button
        backButton.setOnClickListener(v -> {
            //This triggers the OnBackPressedCallback
            onBackPressed();
        });

        //This code runs when the search button has been pressed
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString();
            if(searchTerm.isEmpty() || searchTerm.length() < 3){
                searchInput.setError("Search term must be at least 3 characters!");
                return;
            }else{
                //Calls the method that brings the users that match the inputted name
                setUpSearchUserRecyclerView(searchTerm);
            }
        });
    }

    //This method brings the usernames that match the inputted name
    void setUpSearchUserRecyclerView(String searchTerm){
        //Creates a high value character string to capture all subsequent characters
        //That come after the search term
        String endTerm = searchTerm + '\uf8ff';

        //This will return all names close to the search term.
        //So "Test" will bring "Test adkajsdjnas"
        Query query = FirebaseUtil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("username", searchTerm)
                .whereLessThan("username", endTerm);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
                .setQuery(query, UserModel.class).build();

        adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    //Previous search code that was bringing up somewhat incorrect terms.

    /*    //This will return all names close to the search term.
    //So "Test" will bring "Test adkajsdjnas"
    Query query = FirebaseUtil.allUserCollectionReference()
            .whereGreaterThanOrEqualTo("username", searchTerm);

    FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>()
            .setQuery(query, UserModel.class).build();

    adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
    adapter.startListening();
}
*/


    //These functions cause the adapter to start and stop listening to the database
    //So that it updates dynamically
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}