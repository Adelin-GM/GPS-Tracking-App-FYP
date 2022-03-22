package com.example.gpstrackingapp;

import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.util.Calendar;

public class Trail {
    PolylineOptions path;
    double timeMinutes;
    int distance;
    double speed;
    int calories;
    String date;

    public Trail() {
    }


    public Trail(PolylineOptions path, double timeMinutes, int distance, double speed, int calories, String date) {
        this.path = path;
        this.timeMinutes = timeMinutes;
        this.distance = distance;
        this.speed = speed;
        this.calories = calories;
        this.date = date;
    }

    public PolylineOptions getPath() {
        return path;
    }

    public double getTimeMinutes() {
        return timeMinutes;
    }

    public int getDistance() {
        return distance;
    }

    public double getSpeed() {
        return speed;
    }

    public int getCalories() {
        return calories;
    }

    public String getDate() {
        return date;
    }
}
