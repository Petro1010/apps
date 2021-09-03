package com.example.clientdistancetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class RouteLog extends AppCompatActivity {

    private ListView route_log;
    private LogListAdapter log_adapter;
    private ArrayList<String[]> route_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_log);

        Intent i = getIntent();
        route_log = findViewById(R.id.route_log);
        Bundle args = i.getBundleExtra("route_info");
        route_info = (ArrayList<String[]>) args.getSerializable("ARRAYLIST");

        update_log();
    }

    /**
     * Update the log ListView
     */
    private void update_log(){
        log_adapter = new LogListAdapter(this, R.layout.log_entry, route_info);
        route_log.setAdapter(log_adapter);
    }

    public void create_file(){
        FileOutputStream fos = null;
        String text = "";
        try {
            fos = openFileOutput("route_log.txt", MODE_PRIVATE);

            for (String[] info : route_info) {
                text = text + ("Route: " + info[0] + ", Distance: " + info[1] + "km, Date: " + info[2] + "\n");
            }
            fos.write((text).getBytes());
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Displays the menu in the top right corner of the screen.
     * To create the menu option, go to res folder, new Android resource directory, and define the menu from the new xml file
     * @param menu the menu that is specified
     * @return Boolean representing whether it displays or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_menu, menu);
        return true;
    }

    /**
     * Method that defines what is to be done depending on what menu item is clicked
     * @param item The item in the menu
     * @return whether item was selected on not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //if a certain menu item is clicked, this defines what happens
        if (id == R.id.menu3){
            create_file();
        }
        return super.onOptionsItemSelected(item);
    }
}