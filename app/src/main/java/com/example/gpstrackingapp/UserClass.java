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
    int calories;
    int distance;

    protected UserClass() {
    }

    public UserClass(String username, String password, Double weight, int height, int calories, int distance) {
        this.username = username;
        this.password = password;
        this.weight = weight;
        this.height = height;
        this.calories = calories;
        this.distance = distance;
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
                    calories = snapshot.child(username).child("calories").getValue(int.class);
                    distance = snapshot.child(username).child("distance").getValue(int.class);

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
