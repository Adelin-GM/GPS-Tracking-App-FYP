package com.example.gpstrackingapp;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private boolean start;
    private Chronometer chronometer;
    private boolean running;
    private UserClass user;

    TextView tvDistance;
    TextView tvSpeed;
    TextView tvCalories;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    Location lastLocation;

    ArrayList<LatLng> points;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        points = new ArrayList<>();
        start = false;
        running = false;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        assignViews();
        assignUser();
        createMap();
        getLocationPermission();

    }

    //region AssignGlobalVariables
    private void assignUser(){
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String username = extras.getString("username");
            user = new UserClass(username);
        }
    }
    private void assignViews(){
        tvDistance = findViewById(R.id.tvDistance);
        tvSpeed = findViewById(R.id.tvSpeed);
        chronometer = findViewById(R.id.tvTimer);
        tvCalories = findViewById(R.id.tvCalories);
    }

    //endregion AssignGlobalVariables

    //region MapAndLocation

    private void createMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastLocation = locationResult.getLastLocation();
                displayLastLocation();
            }
        };
    }

    private void displayLastLocation() {
        if (lastLocation != null) {
            LatLng location = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
            DrawPath(location);
        } else {
            Toast.makeText(this, "***Location NOT Available!***", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(4);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void getLocationPermission() {
        //Request user location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("**GRANTED**", "Permission granted");
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);          //10 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lastLocation = location;
                displayLastLocation();
            }
        });
    }
    //endregion MapAndLocation

    //region TrailCreation
    public void DrawPath(LatLng location) {
        if (!start) {
            return;
        }
        displayDistance();      //realtime distance
        displaySpeed();         //realtime speed
        displayCalories();

        points.add(location);
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(points);
        lineOptions.width(20);
        lineOptions.color(Color.BLUE);
        if (lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
    }

    public void StartPathBtn(View view) {
        Button startBt = (Button) view;
        if (start) {
            stopChronometer();
            displayDistance();
            points = new ArrayList<>();
            start = false;
            startBt.setText("Start");
        } else {
            startChronometer();
            start = true;
            startBt.setText("Stop");
        }
    }
    //endregion TrailCreation

    //region TrailStats
    private int getDistance() {     //returns distance in meters
        int distance = 0;
        float[] results = new float[1];
        if (points.size() > 2) {
            for (int i = 0; i < points.size() - 1; i++) {
                Location.distanceBetween(points.get(i).latitude, points.get(i).longitude,
                        points.get(i + 1).latitude, points.get(i + 1).longitude,
                        results);
                distance += results[0];
            }
        }

        Log.d("**DISTANCE**", "Distance is " + distance + " m");

        return distance;
    }

    private void displayDistance() {
        int distance = getDistance();
        if (distance < 1000)
        {
            tvDistance.setText("Distance: " +  distance + " m");
        }
        else
        {
            double distanceKm = distance / 1000.0;
            tvDistance.setText("Distance: " +  distanceKm + " km");
        }
    }

    private double getSpeed(){
        //double timeSeconds = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000.0;        //time in seconds
        double timeMinutes = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 60000.0;         //time in minutes
        double timeHours = timeMinutes / 60.0;

        double speed = (getDistance() / 1000.0) / timeHours;    //speed as km/h
        return speed;
    }

    private void displaySpeed(){
        tvSpeed.setText(String.format("Avg speed: %.2f km/h", getSpeed()));
    }

    private double getCalories(){
        //calories burned per minute = (0.035 * body weight in kg) + ((velocity in m/s^2)/ Height in m)) * 0.029 * body weight in kg
        //calories burned = calories burned per minute * time in minutes

        double speedInMPS = (5.0/18.0) * getSpeed();        //speed in meters per second m/s
        double velocityms = speedInMPS * speedInMPS;        //velocity as m/s^2
        double heightInM = user.getHeight() * 0.01;         //height in meters
        Log.d("Velocity", velocityms + " m/s^2");
        double timeMinutes = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 60000.0;

        Double caloriesPerMinute = (0.035 * user.getWeight()) + (velocityms/ heightInM) * 0.029 * user.getWeight();
        Double caloriesBurned = caloriesPerMinute * timeMinutes;
        return caloriesBurned;
    }

    private void displayCalories(){
        tvCalories.setText(String.format("Calories: %.0f", getCalories()));
    }
    //endregion TrailStats

    //region Chronometer
    private void startChronometer(){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            running = true;
        }
    }

    private void stopChronometer(){
        chronometer.stop();
        //chronometer.setBase(SystemClock.elapsedRealtime());   //this would reset chronometer back to 0
        running = false;
    }
    //endregion Chronometer



}
