package com.example.gpstrackingapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserClass {
    String username;
    String password;
    Double weight;
    int height;

    protected UserClass() {
    }

    protected UserClass(String username, String password, Double weight, int height) {
        this.username = username;
        this.password = password;
        this.weight = weight;
        this.height = height;
    }

    protected UserClass(String username){
        this.username = username;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    password = snapshot.child(username).child("password").getValue(String.class);
                    weight = snapshot.child(username).child("weight").getValue(Double.class);
                    height = snapshot.child(username).child("height").getValue(int.class);

                }
                else {
                    Log.d("***UserClass***", "User not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    protected String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    protected Double getWeight() {
        return weight;
    }

    protected void setWeight(Double weight) {
        this.weight = weight;
    }

    protected int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }
}
