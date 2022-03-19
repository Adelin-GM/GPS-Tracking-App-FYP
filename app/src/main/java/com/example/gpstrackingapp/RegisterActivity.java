package com.example.gpstrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText passId, usernameId, weightET, heightET;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        passId = findViewById(R.id.pasId);
        usernameId = findViewById(R.id.usernameId);
        weightET = findViewById(R.id.weightET);
        heightET = findViewById(R.id.heightET);
    }

    public void registerBtn(View view) {
        if (usernameId.getText().toString().equals("")){
            usernameId.setError("Enter a username");
            return;
        }
        if (passId.getText().toString().equals("")){
            passId.setError("Enter a password");
            return;
        }
        //register user if the username is not existent
        usernameNotTaken();

    }

    private void usernameNotTaken(){
        String userEnteredUsername = usernameId.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if user not found register user
                if (!snapshot.exists()){
                    registerUser();
                }
                else {
                    usernameId.setError("User already exists");
                    Toast.makeText(getBaseContext(), "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("**===USER===**", "ERROR");
            }
        });
    }

    private void registerUser()
    {
        if (!isWeightValid())
            return;
        if (!isHeightValid())
            return;

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        String username = usernameId.getText().toString();
        String password = passId.getText().toString();
        Double weight = Double.valueOf(weightET.getText().toString());
        int height = Integer.valueOf(heightET.getText().toString());


        UserClass userClass = new UserClass(username, password, weight, height, 0 , 0);
        reference.child(username).setValue(userClass);

        changeToLogInActivity(username);
    }

    private void changeToLogInActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private boolean isWeightValid(){
        try {
            Double.valueOf(weightET.getText().toString());
            return true;
        }
        catch (Exception e){
            Log.d("***WEIGHT***","Invalid weight");
            weightET.setError("Invalid weight. Correct format e.g.: 65.0");
            return false;
        }
    }
    private boolean isHeightValid(){
        try {
            Integer.valueOf(heightET.getText().toString());
            return true;
        }
        catch (Exception e){
            Log.d("***HEIGHT***","Invalid height");
            heightET.setError("Invalid weight. Correct format e.g.: 165");
            return false;
        }
    }


}