package com.example.clientdistancetracker;
/**
 * Author: Mathew Petronilho
 * Last Edited: August 19 2021
 * Description: Activity related to getting route info from the user
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class ClientInfo extends AppCompatActivity {

    /**
     * Initalizes the view of the Activity to the client xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info2);
    }

    /**
     * Gathers the data necessary to enter a route into the client TST
     * @param v The view clicked to submit the data
     */
    public void add_client(View v){    //passing data back to a previous activity
        String client1 = ((TextView) findViewById(R.id.client1)).getText().toString();
        String client2 = ((TextView) findViewById(R.id.client2)).getText().toString();
        String distance_str = ((TextView) findViewById(R.id.distance)).getText().toString();
        Intent i = new Intent();
        String client;
        Double distance;

        //if one of the text boxes does not get filled, mark its entry with negative infinity
        if (client1.isEmpty() || client2.isEmpty() || distance_str.isEmpty()){
            client = client1;
            distance = Double.NEGATIVE_INFINITY;
        }
        else{
            client = client1 + " to " + client2;
            distance = Double.valueOf(distance_str);
        }

        //passes one piece of data with a key and value
        i.putExtra("Name", client);
        i.putExtra("Distance", distance);
        setResult(RESULT_OK, i);
        finish();

    }
}