package com.example.gpstrackingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends ArrayAdapter<UserClass> {
    Context mContext;
    int mResource;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserClass> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int rank = position + 1;
        String username = getItem(position).getUsername();
        int distance = getItem(position).getDistance();


        UserClass user = new UserClass();
        user.setDistance(distance);
        user.setUsername(username);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView rankTV = (TextView) convertView.findViewById(R.id.rankTV);
        TextView usernameTV = (TextView) convertView.findViewById(R.id.usernameTV);
        TextView distanceTV = (TextView) convertView.findViewById(R.id.distanceTV);

        rankTV.setText("#" + rank);
        usernameTV.setText(username);

        if (distance < 1000)
        {
            distanceTV.setText(distance + " m");
        }
        else
        {
            double distanceKm = distance / 1000.0;
            distanceTV.setText(distanceKm + " km");
        }
        return convertView;

    }
}
