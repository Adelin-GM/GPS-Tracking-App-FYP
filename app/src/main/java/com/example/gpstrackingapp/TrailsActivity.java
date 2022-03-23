package com.example.gpstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrailsActivity extends AppCompatActivity {
    private static final String TAG = "TrailActivity";
    ArrayList<Trail> trailList;
    ListView trailsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trails);
        trailsListView = (ListView) findViewById(R.id.trailsListView);
        trailList = new ArrayList<>();
        loadData();
        displayData();
    }

    public void trailInfoBtn(View view) {
        Log.d(TAG, "trailInfoBtn: Started");
        TextView textView = view.findViewById(R.id.trailCalories);
        String s = view.getTag().toString();
        Log.d(TAG, "trailInfoBtn: " + s);

        Log.d(TAG, "trailInfoBtn: fromlist " + trailList.get(Integer.parseInt(s)).getDistance());
        changeToTrailMapActivity(Integer.parseInt(s));

    }

    private void changeToTrailMapActivity(int i){
        Intent intent = new Intent(this, TrailMapActivity.class);
        intent.putExtra("index", i);
        startActivity(intent);

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list" , null);
        Type type = new TypeToken<ArrayList<Trail>>() {}.getType();
        trailList = gson.fromJson(json, type);

        if (trailList == null) {
            trailList = new ArrayList<>();
        }
        for (Trail trail: trailList) {
            Log.d(TAG, trail.date);
        }

    }

    private void displayData(){
        TrailListAdapter adapter = new TrailListAdapter(TrailsActivity.this, R.layout.adapter_trail_view_layout, trailList);
        trailsListView.setAdapter(adapter);
    }




}