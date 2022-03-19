package com.example.gpstrackingapp;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBDatabase {
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    public FBDatabase() {
    }

    protected void updateUser(UserClass user)
    {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child(user.getUsername()).setValue(user);
    }
}
