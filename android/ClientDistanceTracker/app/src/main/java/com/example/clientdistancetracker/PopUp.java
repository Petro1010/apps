package com.example.clientdistancetracker;
/**
 * Author: Mathew Petronilho
 * Last Edited: August 19 2021
 * Description: A custom Activity Object that acts as a Pop Up
 *              Note: to finish creating the pop up, make an xml for its model, make a custom style in themes.xml, and add
 *              pop up activity in the android manifest
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

public class PopUp extends Activity {

    private String client;

    /**
     * Sets up the new Pop Up window with the dimensions adjusted.
     * Also unpacks the client name to be deleted from the previous activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_window);

        //setting new dimensions for the pop up window`
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (0.8*width), (int) (0.5*height));

        //deal with intent
        Intent i = getIntent();
        client = i.getStringExtra("Name");
    }

    /**
     * If the Yes button is selected, then message to delete client will be sent back to main activity
     * @param v The confirm delete button
     */
    public void delete(View v){

        Intent i = new Intent();
        i.putExtra("Name", client);
        i.putExtra("Delete", true);
        setResult(RESULT_OK, i);
        finish();

    }

    /**
     * If the No button is selected, then message to not delete client will be sent back to main activity
     * @param v The do not delete button
     */
    public void unconfirm_delete(View v){

        Intent i = new Intent();
        i.putExtra("Name", client);
        i.putExtra("Delete", false);
        setResult(RESULT_OK, i);
        finish();

    }
}
