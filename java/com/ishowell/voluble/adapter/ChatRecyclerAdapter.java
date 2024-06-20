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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ishowell.voluble.R;
import com.ishowell.voluble.model.ChatMessageModel;
import com.ishowell.voluble.utils.FirebaseUtil;


//An Adapter class for the RecyclerView, which displays a list of chat messages from Firestore
public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    //Gets Context to access resources and perform LayoutInflater operations
    Context context;

    //This is the Constructor for the adapter class that accepts Firestore options and context
    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    //This Method is used to bind the data of each ChatMessageModel object to the holder views
    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {

        //Checks if the current user is the sender of the message
        if(model.getSenderID().equals(FirebaseUtil.currentUserId())){
            holder.leftChatlayout.setVisibility(View.GONE);
            holder.rightChatlayout.setVisibility(View.VISIBLE);
            holder.rightChatText.setText(model.getMessage());
        }else{
            //If the current user is not the sender of the message, then they are the receiver
            holder.rightChatlayout.setVisibility(View.GONE);
            holder.leftChatlayout.setVisibility(View.VISIBLE);
            holder.leftChatText.setText(model.getMessage());
        }
    }

    //This method creates and returns new ViewHolder instances
    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is used to inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message_recycler_row, parent, false);
        return new ChatModelViewHolder(view);
    }

    //This ViewHolder class for allows for better performance and easier access to UI elements
    class ChatModelViewHolder extends RecyclerView.ViewHolder {

        //Creating variables for UI components
        LinearLayout leftChatlayout, rightChatlayout;
        TextView leftChatText, rightChatText;

        //This Constructor initializes UI components with the correct IDs
        public ChatModelViewHolder(@NonNull View itemView) {
            super(itemView);

            //Initializing UI components
            leftChatlayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatlayout = itemView.findViewById(R.id.right_chat_layout);
            leftChatText = itemView.findViewById(R.id.left_chat_text_view);
            rightChatText = itemView.findViewById(R.id.right_chat_text_view);
        }
    }
}
