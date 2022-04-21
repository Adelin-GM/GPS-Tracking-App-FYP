package com.example.gpstrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchUserActivity extends AppCompatActivity {
    private static final String TAG = "SearchUserActivity";
    TextView username, distance, calories;
    EditText search;
    double distanceKm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        username = findViewById(R.id.usernameS);
        distance = findViewById(R.id.tvDistanceS);
        calories = findViewById(R.id.tvCaloriesS);
        search = findViewById(R.id.ETUsernameSearch);

    }

    private void checkRewards(){
        ImageView bronze = (ImageView) findViewById(R.id.rewardBronzeS);
        ImageView silver = (ImageView) findViewById(R.id.rewardSilverS);
        ImageView gold = (ImageView) findViewById(R.id.rewardGoldS);
        ImageView platinum = (ImageView) findViewById(R.id.rewardPlatinumS);
        ImageView diamond = (ImageView) findViewById(R.id.rewardDiamondS);


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


    public void searchProfileOnClick(View view) {
        search.setError(null);
        getUser();

    }

    private void getUser(){
        String usernameEntered = search.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(usernameEntered);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    boolean private_account = snapshot.child(usernameEntered).child("private_account").getValue(Boolean.class);
                    if (private_account){
                        search.setError("This account is private");
                        return;
                    }

                    String usernameFromDB = snapshot.child(usernameEntered).child("username").getValue(String.class);
                    int caloriesFromDB = snapshot.child(usernameEntered).child("calories").getValue(Integer.class);
                    int distanceFromDB = snapshot.child(usernameEntered).child("distance").getValue(Integer.class);

                    username.setText(usernameFromDB);

                    if (distanceFromDB < 1000)
                    {
                        distance.setText("Total distance: " + distanceFromDB + " m");
                    }
                    else
                    {
                        distanceKm = distanceFromDB / 1000.0;
                        distance.setText("Total distance: " + distanceKm + " km");
                    }

                    if (caloriesFromDB < 1000)
                    {
                        calories.setText("Calories burned: " + caloriesFromDB);
                    }
                    else if(caloriesFromDB > 1000000){
                        double mCalories = caloriesFromDB / 1000000.0;
                        calories.setText("Calories burned: " + mCalories + "M");
                    }
                    else
                    {
                        double kCalories = caloriesFromDB / 1000.0;
                        calories.setText("Calories burned: " + kCalories + "K");
                    }
                    checkRewards();
                }
                else {
                    search.setError("Not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}