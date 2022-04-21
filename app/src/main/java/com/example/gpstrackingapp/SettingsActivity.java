package com.example.gpstrackingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    UserClass user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        assignUser();
    }

    @Override
    protected void onPause() {
        super.onPause();
        applySettings();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    private void assignUser(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String username = extras.getString("username");
            user = new UserClass(username);
        }
    }

    private void applySettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        applyProfileSettingsToDB(preferences);
    }

    private void applyProfileSettingsToDB(SharedPreferences preferences){
        boolean private_account = preferences.getBoolean("private", true);
        user.setPrivate_account(private_account);
        FBDatabase db = new FBDatabase();
        db.updateUser(user);
    }
}