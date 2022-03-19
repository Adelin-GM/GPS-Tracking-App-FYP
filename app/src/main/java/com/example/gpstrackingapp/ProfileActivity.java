package com.example.gpstrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView username, height, weight, distance, calories;
    double distanceKm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username);
        height = findViewById(R.id.tvHeight);
        weight = findViewById(R.id.tvWeight);
        distance = findViewById(R.id.tvDistance);
        calories = findViewById(R.id.tvCalories);

        fillTextViewsData();
        checkRewards();



    }

    private void fillTextViewsData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String extraUsername = extras.getString("username");
            int extraHeight = extras.getInt("height");
            Double extraWeight = extras.getDouble("weight");
            int extraDistance = extras.getInt("distance");
            int extraCalories = extras.getInt("calories");

            username.setText(extraUsername);
            height.setText("Height: " + extraHeight);
            weight.setText("Weight: " + extraWeight);


            if (extraDistance < 1000)
            {
                distance.setText("Total distance: " + extraDistance + " m");
            }
            else
            {
                distanceKm = extraDistance / 1000.0;
                distance.setText("Total distance: " + distanceKm + " km");
            }

            if (extraCalories < 1000)
            {
                calories.setText("Calories burned: " + extraCalories);
            }
            else if(extraCalories > 1000000){
                double mCalories = extraCalories / 1000000.0;
                calories.setText("Calories burned: " + mCalories + "M");
            }
            else
            {
                double kCalories = extraCalories / 1000.0;
                calories.setText("Calories burned: " + kCalories + "K");
            }
        }
    }

    private void checkRewards(){
        ImageView bronze = (ImageView) findViewById(R.id.rewardBronze);
        ImageView silver = (ImageView) findViewById(R.id.rewardSilver);
        ImageView gold = (ImageView) findViewById(R.id.rewardGold);
        ImageView platinum = (ImageView) findViewById(R.id.rewardPlatinum);
        ImageView diamond = (ImageView) findViewById(R.id.rewardDiamond);


        if (distanceKm >= 1)
            bronze.setColorFilter(Color.parseColor("#b08d57"));
        if(distanceKm >= 5)
            silver.setColorFilter(Color.parseColor("#5B5B5B"));
        if(distanceKm >= 10)
            gold.setColorFilter(Color.parseColor("#FFD700"));
        if(distanceKm >= 50)
            platinum.setColorFilter(Color.parseColor("#3D8B72"));
        if(distanceKm >= 100)
            diamond.setColorFilter(Color.parseColor("#483d8b"));


    }


    public void leaderboardBtn(View view) {
        changeToLeaderboardActivity();
    }

    private void changeToLeaderboardActivity(){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }
}