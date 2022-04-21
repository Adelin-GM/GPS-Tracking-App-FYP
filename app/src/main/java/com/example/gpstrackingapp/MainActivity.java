package com.example.gpstrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText passId, usernameId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passId = findViewById(R.id.pasId);
        usernameId = findViewById(R.id.usernameId);
        fillUsernameField();

    }

    private void fillUsernameField(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            usernameId.setText(extras.getString("username"));
        }
    }


    public void logInBtn(View view) {
        logInUser();
    }

    public void registerBtn(View view) {
        changeToRegisterActivity();

    }

    private void changeToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void isUser(){
        FBDatabase db = new FBDatabase();
        String userEnteredUsername = usernameId.getText().toString();
        String userEnteredPassword = passId.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernameId.setError(null);
                if (snapshot.exists()){
                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    //Change activity if password matches
                    if (passwordFromDB.equals(db.hashPassword(userEnteredPassword))){
                        openMapActivity(userEnteredUsername);
                    }
                    else{
                        passId.setError("Incorrect Password");
                    }

                }
                else {
                    usernameId.setError("Incorrect username");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void logInUser()
    {
        if (requestLocationPermisionIsTrue())
            isUser();
    }



    public void openMapActivity(String username){
        try{
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("username", username);
            startActivity(i);

        }catch (Exception e){
            Toast.makeText(this, "Get the location", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean requestLocationPermisionIsTrue() {
        //Request user location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        } else {
            Log.d("**GRANTED**", "Permission granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isUser();
        }
        else{
            Toast.makeText(this, "Allow access to location", Toast.LENGTH_SHORT).show();
        }
    }


}