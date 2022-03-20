package com.example.gpstrackingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    FBDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        db = new FBDatabase();
        db.getAllUsers();



        waitToFillUsers();
    }

    private void waitToFillUsers(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (db.usersList.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                displayUsers();
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }
    
    private void displayUsers(){
        for (int i = 0; i < db.usersList.size(); i++){
            Log.d("***Leaderboard***", "Rank: #" + (i + 1) + " Username: " + db.usersList.get(i).username + " Distance: " + db.usersList.get(i).distance);
        }
    }


}