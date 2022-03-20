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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    FBDatabase db;
    ListView leaderboardListView;
    TextView leaderboardTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        leaderboardListView = (ListView) findViewById(R.id.lListView);
        leaderboardTV = (TextView) findViewById(R.id.leaderboardTV);

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
        new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserListAdapter adapter = new UserListAdapter(LeaderboardActivity.this, R.layout.adapter_leaderboard_item_view_layout, db.usersList);
                        leaderboardListView.setAdapter(adapter);
                        leaderboardTV.setText("Top 50");
                    }
                });

            }
        }.start();

    }


}