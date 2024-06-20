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

package com.ishowell.voluble.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ishowell.voluble.ChatActivity;
import com.ishowell.voluble.R;
import com.ishowell.voluble.model.ChatRoomModel;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.FirebaseUtil;
import com.ishowell.voluble.utils.AndroidUtil;

//An Adapter class for the RecyclerView, which displays a list of users from Firestore
public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    //Gets Context to access resources and perform LayoutInflater operations
    Context context;

    //This is the Constructor for the adapter class that accepts Firestore options and context
    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    //This Method is used to bind the data of each ChatRoomModel object to the holder views
    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserids())
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());;


                        UserModel otherUsermodel = task.getResult().toObject(UserModel.class);

                        //This gets the profile picture from Firestore,
                        //So that the profile screen can see the custom profile picture
                        FirebaseUtil.getOtherProfilePicStorageReference(otherUsermodel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(t -> {
                                    if(t.isSuccessful()){
                                        Uri uri = t.getResult();
                                        AndroidUtil.setProfilepic(context, uri, holder.profile_pic);
                                    }
                                });

                        holder.username_text.setText(otherUsermodel.getUsername());

                        if(lastMessageSentByMe){
                            holder.lastMessageText.setText("You: " + model.getLastMessage());
                        }else{
                            holder.lastMessageText.setText("Them: " + model.getLastMessage());
                        }
                        holder.lastMessageTimeSent.setText(FirebaseUtil.timestampToString(model.getLastMessageTime()));

                        //This sets a click listener for each item/username
                        holder.itemView.setOnClickListener(v -> {
                                //Navigates to chat activity
                                Intent intent = new Intent(context, ChatActivity.class);
                                //This passes the username into the chat activity
                                AndroidUtil.passUserModelAsIntent(intent, otherUsermodel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                        });
                    }
                });
    }

    //This method creates and returns new ViewHolder instances
    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is used to inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatRoomModelViewHolder(view);
    }

    //This ViewHolder class for allows for better performance and easier access to UI elements
    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder {

        //Creating variables for UI components
        TextView username_text;
        TextView lastMessageText, lastMessageTimeSent;
        ImageView profile_pic;

        //This Constructor initializes UI components with the correct IDs
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            username_text = itemView.findViewById(R.id.recent_username_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTimeSent = itemView.findViewById(R.id.last_message_time_text);
            profile_pic = itemView.findViewById(R.id.profile_pic);
        }
    }
}
