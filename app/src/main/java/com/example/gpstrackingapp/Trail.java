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


    public Trail(PolylineOptions path, double timeMinutes, int distance, double speed, int calories) {
        this.path = path;
        this.timeMinutes = timeMinutes;
        this.distance = distance;
        this.speed = speed;
        this.calories = calories;
        date = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
    }
}
