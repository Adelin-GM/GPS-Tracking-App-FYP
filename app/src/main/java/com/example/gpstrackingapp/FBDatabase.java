package com.example.gpstrackingapp;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FBDatabase {
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private final int TOPUSERS = 50;
    public ArrayList<UserClass> usersList;


    public FBDatabase() {
        usersList = new ArrayList<>();
    }

    protected void updateUser(UserClass user)
    {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child(user.getUsername()).setValue(user);
    }

    protected void getAllUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users").orderByChild("distance").limitToLast(TOPUSERS);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserClass> list = new ArrayList<>();

                for (DataSnapshot result: snapshot.getChildren()) {
                    UserClass user = new UserClass();
                    user.setUsername(result.child("username").getValue(String.class));
                    user.setDistance(result.child("distance").getValue(int.class));
                    list.add(user);
                }
                Collections.reverse(list);
                usersList = list;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("***UserClass***", "onCancelled: " + error.toException());
            }
        });

    }
}
