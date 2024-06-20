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

package com.ishowell.voluble.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    //Gets the user UID, which serves as the primary key for the Firebase Database
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    //Gets the document reference for the current user


    /**
     * Checks if a user is currently logged in by verifying if a UID is available.
     * true if a user is logged in, false otherwise.
     */
    public static boolean isLogin() {
        if(currentUserId() != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Retrieves the Firestore DocumentReference for the currently logged-in user.
     * This reference points to the specific document in the 'users' collection that
     * stores data related to the logged-in user.
     */
    public static DocumentReference currentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    /**
     * This provides a reference to the 'users' collection in Firestore.
     * This collection contains documents for all users, with each document
     * storing user-specific information.
     */
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    /**
     * Retrieves a reference to a specific chat room in Firestore.
     * This function is used to access the document associated with a chat room
     * using its unique ID.
     * DocumentReference is a reference to the specified chat room's document in Firestore.
     */
    public static DocumentReference getChatRoomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }

    public static CollectionReference getChatRoomMessageReference(String chatRoomId){
        return getChatRoomReference(chatRoomId).collection("chats");
    }
    public static String getChatroomID(String userId1, String userId2){
        if(userId1.hashCode() < userId2.hashCode()){
            return userId1 + "_" + userId2;
        }else{
            return userId2 + "_" + userId1;
        }

    }

    public static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    //This gets the id from the list array stored in the Firestore database
    public static DocumentReference getOtherUserFromChatroom(List<String> userids){
        if(userids.get(0).equals(currentUserId())){
            return allUserCollectionReference().document(userids.get(1));
        }else{
            return allUserCollectionReference().document(userids.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp){
       return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    //This gets the storage reference for the current user's profile picture
    //Also when the user chooses a new profile picture, this function updates the storage reference
    public static StorageReference getCurrentProfilePicStorageReference(){
        return FirebaseStorage.getInstance().getReference().child("profile_pictures")
                .child(FirebaseUtil.currentUserId());
    }

    //This gets the storage reference for the current user's profile picture
    //Also when the user chooses a new profile picture, this function updates the storage reference
    public static StorageReference getOtherProfilePicStorageReference(String otherUserID){
        return FirebaseStorage.getInstance().getReference().child("profile_pictures")
                .child(otherUserID);
    }
}
