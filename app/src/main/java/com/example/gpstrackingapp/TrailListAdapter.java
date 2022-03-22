package com.example.gpstrackingapp;

import android.app.Person;
import android.content.Context;
import android.icu.text.MessagePattern;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class TrailListAdapter extends ArrayAdapter<Trail> {
    private Context mContext;
    private int mResource;

    public TrailListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Trail> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String date = getItem(position).getDate();
        PolylineOptions path = getItem(position).getPath();
        double timeMinutes = getItem(position).getTimeMinutes();
        int distance = getItem(position).getDistance();
        double speed = getItem(position).getSpeed();
        int calories = getItem(position).getCalories();

        Trail trail = new Trail(path, timeMinutes, distance, speed, calories, date);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent , false);

        TextView tvDate = (TextView) convertView.findViewById(R.id.trailDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.trailTime);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.trailDistance);
        TextView tvSpeed = (TextView) convertView.findViewById(R.id.trailSpeed);
        TextView tvCalories = (TextView) convertView.findViewById(R.id.trailCalories);

        tvDate.setText(date + "");
        tvTime.setText(timeMinutes + " min");
        tvDistance.setText(distance + " m");
        tvSpeed.setText(speed + " km/h");
        tvCalories.setText(calories + " cal");


        return convertView;
    }
}
