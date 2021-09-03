package com.example.clientdistancetracker;

/**
 * Author: Mathew Petronilho
 * Last Edited: August 29 2021
 * Description: Custom List Adapter that allows custom views to be used in a ListView
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class LogListAdapter extends ArrayAdapter<String[]> implements Filterable {
    private static final String TAG = "LogListAdapter";
    private Context mContext;
    private int mResource;

    public LogListAdapter(Context context, int resource, ArrayList<String[]> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    /**
     * Adds a single view to the ListView using the data given in the submitted Iterable object (Array)
     * @param position  Position of data in Array and in ListView
     * @param convertView
     * @param parent
     * @return The final View
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] info = getItem(position);
        String name = info[0];
        String nums = info[1];
        String date = info[2];

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView Name = (TextView) convertView.findViewById(R.id.log_text_1);
        TextView Nums = (TextView) convertView.findViewById(R.id.log_text_2);
        TextView Date = (TextView) convertView.findViewById(R.id.log_text_3);

        Name.setText(name);
        Nums.setText("Distance: " + nums + "km");
        Date.setText("Date: " + date);

        return convertView;


    }


}

