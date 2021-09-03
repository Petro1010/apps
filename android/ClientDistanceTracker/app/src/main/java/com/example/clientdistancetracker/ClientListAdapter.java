package com.example.clientdistancetracker;

/**
 * Author: Mathew Petronilho
 * Last Edited: August 30 2021
 * Description: Custom List Adapter that allows custom views to be used in a ListView
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClientListAdapter extends ArrayAdapter<String[]> implements Filterable {
    private static final String TAG = "ClientListAdapter";
    private Context mContext;
    private int mResource;
    private ArrayList<String[]> routes;

    public ClientListAdapter(Context context, int resource, ArrayList<String[]> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        routes = new ArrayList<>(objects);
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
        //break up the string into the proper information
        String[] info = getItem(position);
        String name = info[0];
        String nums = info[1];

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView Name = (TextView) convertView.findViewById(R.id.textView2);
        TextView Nums = (TextView) convertView.findViewById(R.id.textView3);

        Name.setText(name);
        Nums.setText("Visits: " + nums);

        return convertView;


    }

    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    /**
     * Custom Filter to deal with the ArrayList<String[]> that has been submitted. Do not need a custom filter if it is a regular
     * data type being submitted.
      */
    private Filter clientFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<String[]> suggestions = new ArrayList<>();

            //if nothing is there, show all the routes
            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(routes);

            }
            else{
                //constraint is changed to lower case to make non case sensitive, and all excess white space is trimmed
                String filter_pattern = constraint.toString().toLowerCase().trim();

                for (String[] route : routes){
                    //if name of route has constraint in it, display it
                    if (route[0].toLowerCase().contains(filter_pattern)){
                        suggestions.add(route);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
