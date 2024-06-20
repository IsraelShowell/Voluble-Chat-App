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

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.functions.FirebaseFunctions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ishowell.voluble.adapter.ChatRecyclerAdapter;
import com.ishowell.voluble.model.ChatMessageModel;
import com.ishowell.voluble.model.ChatRoomModel;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.AndroidUtil;
import com.ishowell.voluble.utils.FirebaseUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {

    //Declare variables
    UserModel otherUser;
    String chatroomID;
    ChatRoomModel chatRoomModel;

    ChatRecyclerAdapter chatRecyclerAdapter;

    EditText messageInput;
    ImageView sendButton;
    ImageView backButton;

    TextView otherUsername;
    RecyclerView chatRecyclerView;

    ImageView profile_pic;

    FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Gets the other user from the intent
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomID = FirebaseUtil.getChatroomID(FirebaseUtil.currentUserId(), otherUser.getUserId());

        //Sets the variables to correct ids
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_btn);
        backButton = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        profile_pic = findViewById(R.id.profile_pic);

        //This gets the profile picture from Firestore,
        //So that the profile screen can see the custom profile picture
        FirebaseUtil.getOtherProfilePicStorageReference(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilepic(this, uri, profile_pic);
                    }
                });

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

        //Sets username to the other chat user
        otherUsername.setText(otherUser.getUsername());

        //Adds a click listener to the send button
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }else{
                sendMessage(message);
            }

        });
        getOrCreateChatRoomModel();

        setupChatRecyclerView();
    }

    void setupChatRecyclerView(){
        //This will return all the chats that are in the chatroom
        Query query = FirebaseUtil.getChatRoomMessageReference(chatroomID)
                .orderBy("timeMessageSent", Query.Direction.DESCENDING);

        //Runs the query and talks to the Firestore
        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        //Prepares to display the chats in the recycler view
        chatRecyclerAdapter = new ChatRecyclerAdapter(options, getApplicationContext());

        //Sets up the layout and how it is displayed
        LinearLayoutManager manager = (new LinearLayoutManager(this));
        manager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(manager);
        chatRecyclerView.setAdapter(chatRecyclerAdapter);
        chatRecyclerAdapter.startListening();

        //This causes the screen to scroll to newly sent messages
        chatRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                chatRecyclerView.smoothScrollToPosition(0);
            }
        });
    }
    void sendMessage(String message) {
        //Sets the last message details
        chatRoomModel.setLastMessageTime(Timestamp.now());
        chatRoomModel.setLastMessageSender(FirebaseUtil.currentUserId());
        chatRoomModel.setLastMessage(message);

        //Update the chat room details in the database
        FirebaseUtil.getChatRoomReference(chatroomID).set(chatRoomModel);

        //Creates the message model and adds it to the database
        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUserId(), Timestamp.now());
        //Adds the message to the database
        FirebaseUtil.getChatRoomMessageReference(chatroomID).add(chatMessageModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        messageInput.setText(""); // Clears the input field after sending
                        //triggerPushNotification(message);
                    }
                });
    }

    /*void triggerPushNotification(String message) {
        Map<String, Object> data = new HashMap<>();
        //Adds the message to the data map
        data.put("message", message);
        //Calls the other user's ID
        data.put("userId", otherUser.getUserId());

        //Sends the notification
        mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .addOnFailureListener(e -> Log.e("PushNotification", "Failed to send notification", e));
    }*/

    void getOrCreateChatRoomModel(){
        FirebaseUtil.getChatRoomReference(chatroomID).get().addOnCompleteListener(task -> {
            //Checks if the task is successful
            if(task.isSuccessful()){
                //Converts the Firestore document into a ChatRoomModel object.
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);

                if(chatRoomModel == null){
                    // If there's no existing chat room, this will create a new ChatRoomModel instance.
                    // This instance includes the chat room ID, a list of user IDs (current user and another user),
                    // the current timestamp for the chat room creation time, and an empty last message string since no message has been sent
                    chatRoomModel = new ChatRoomModel(
                            chatroomID,
                            Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    //Adds the chatroomID to the database
                    FirebaseUtil.getChatRoomReference(chatroomID).set(chatRoomModel);
                }
            }
        });
    }
}