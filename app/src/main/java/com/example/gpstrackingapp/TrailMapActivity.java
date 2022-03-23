package com.example.gpstrackingapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingapp.databinding.ActivityTrailMapBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrailMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityTrailMapBinding binding;
    private static final String TAG = "TrailMapActivity";

    ArrayList<Trail> trailList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrailMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        trailList = new ArrayList<>();
        loadData();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.trailMap);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        PolylineOptions path = new PolylineOptions();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int index = extras.getInt("index");
            Trail trail = trailList.get(index);
            addTrailDataToView(trail);
            path = trail.getPath();
        }

        path.color(Color.DKGRAY);
        if (path != null) {
            mMap.addPolyline(path);
            Log.d(TAG, "onMapReady: Path not null" + path.getPoints().get(0));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(path.getPoints().get(0), 18 ));
    }

    private void addTrailDataToView(Trail trail){
        TextView tvDate = (TextView) findViewById(R.id.trailMapDate);
        TextView tvTime = (TextView) findViewById(R.id.trailMapTimer);
        TextView tvDistance = (TextView) findViewById(R.id.trailMapDistance);
        TextView tvSpeed = (TextView) findViewById(R.id.trailMapSpeed);
        TextView tvCalories = (TextView) findViewById(R.id.trailMapCalories);

        tvDate.setText(trail.date + "");
        tvTime.setText(toCorrectTime(trail.getTimeMinutes()));
        tvDistance.setText(toCorrectDistance(trail.getDistance()));
        tvSpeed.setText(toCorrectSpeed(trail.getSpeed()));
        tvCalories.setText(trail.calories + " cal");

    }
    private String toCorrectDistance(int distance){
        String sDistance;
        if (distance < 1000)
        {
            sDistance = distance + " m";
        }
        else
        {
            double distanceKm = distance / 1000.0;
            sDistance = distanceKm + " km";
        }

        return sDistance;
    }

    private String toCorrectSpeed(double speed){
        return String.format("%.2f km/h", speed);
    }

    private String toCorrectTime(double timeMinutes){
        double totalSeconds = timeMinutes * 60;
        double seconds = totalSeconds % 60;
        int newTimeMinutes = (int) timeMinutes;
        String sTime = String.format(newTimeMinutes + " : %.0f", seconds);

        return sTime;
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
}