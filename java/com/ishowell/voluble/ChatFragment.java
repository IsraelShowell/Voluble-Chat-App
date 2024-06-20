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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.ishowell.voluble.adapter.RecentChatRecyclerAdapter;
import com.ishowell.voluble.adapter.SearchUserRecyclerAdapter;
import com.ishowell.voluble.model.ChatRoomModel;
import com.ishowell.voluble.model.UserModel;
import com.ishowell.voluble.utils.FirebaseUtil;


public class ChatFragment extends Fragment {

    RecyclerView chat_fragment_recyclerview;
    RecentChatRecyclerAdapter adapter;

    public ChatFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Since this is a fragment, we need to convert the layout to a view to access the IDs
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chat_fragment_recyclerview = view.findViewById(R.id.recycler_view_chat_fragment);

        setUpRecyclerView();


        return view;
    }

    void setUpRecyclerView(){

        //This will return recent chats that have been had between 2 users
        Query query = FirebaseUtil.allChatRoomCollectionReference()
                .whereArrayContains("userids", FirebaseUtil.currentUserId())
                .orderBy("lastMessageTime", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatRoomModel> options = new FirestoreRecyclerOptions.Builder<ChatRoomModel>()
                .setQuery(query, ChatRoomModel.class).build();

        adapter = new RecentChatRecyclerAdapter(options, getContext());
        chat_fragment_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        chat_fragment_recyclerview.setAdapter(adapter);
        adapter.startListening();
    }

    //These functions cause the adapter to start and stop listening to the database
    //So that it updates dynamically
    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}