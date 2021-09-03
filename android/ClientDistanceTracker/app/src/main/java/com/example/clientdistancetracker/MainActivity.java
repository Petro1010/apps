package com.example.clientdistancetracker;
/**
 * Author: Mathew Petronilho
 * Last Edited: August 30 2021
 * Description: Main Activity of the application and its essential methods
 */

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ClientTST clients = new ClientTST();
    private ActivityResultLauncher<Intent> ClientInfoResultLauncher;
    private ActivityResultLauncher<Intent> DeleteConfirmResultLauncher;
    private ListView listView;
    private ArrayList<String[]> client_names;
    private ArrayList<String[]> log_tracker = new ArrayList<>();
    private ClientListAdapter adapter;
    private static final String CLIENT_ROUTES_KEY = "client_routes";
    private static final String SHARED_PREF_KEY = "shared_pref";
    private static final String LOG_KEY = "log_info";

    /**
     * Initializes the view of the activity to activity_main xml, defines the listView, and defines the retrievers for data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        load_data();
        update_listView();
        update_distance_text();

        //retrieving data from the clientInfo activity
        ClientInfoResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();

                            //if data is marked as empty in a field, do not add it to the TST
                            if (data.getDoubleExtra("Distance", 0.0) == Double.NEGATIVE_INFINITY){
                                return;
                            }
                            String route_name = data.getStringExtra("Name");
                            Double distance = data.getDoubleExtra("Distance", 0.0);
                            clients.put(route_name, distance);

                            add_to_log(route_name, distance);

                            //set starting text of the app
                            update_distance_text();

                            //update array list with any new clients
                            update_listView();

                            //save the change in data
                            save_data();
                        }
                    }
                });

        // Add Text Change Listener to EditText, part of search bar functionality
        ((EditText) findViewById(R.id.search)).addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                (MainActivity.this).adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //retrieving data from the delete confirmation pop up
        DeleteConfirmResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data.getBooleanExtra("Delete", false)){
                                //delete the client
                                clients.put(data.getStringExtra("Name"), null);

                                //set starting text of the app
                                update_distance_text();
                                //update the list and the view
                                update_listView();
                                //save the change in data
                                save_data();
                            }

                        }
                    }
                });


    }

    /**
     * Displays the menu in the top right corner of the screen.
     * To create the menu option, go to res folder, new Android resource directory, and define the menu from the new xml file
     * @param menu the menu that is specified
     * @return Boolean representing whether it displays or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
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
        if (id == R.id.menu1){
            Intent i = new Intent(this, RouteLog.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST", log_tracker);
            i.putExtra("route_info", args);
            startActivity(i);
        }

        if (id == R.id.menu2){
            clients = new ClientTST();
            log_tracker = new ArrayList<>();
            update_distance_text();
            update_listView();
            save_data();
        }



        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to launch the ClientInfo activity
     * @param v The view clicked to launch the activity
     */
    public void launch_client_info(View v){  //how to get data from a previous activity
        Intent i = new Intent(this, ClientInfo.class);
        ClientInfoResultLauncher.launch(i);
    }

    /**
     * Updates the listView with the all the clients in the TST
     * Note: This includes a custom ListAdapter for a custom view layout for each view within the listView.
     *       If you do not want a special ListAdapter, the simpler way is provided
     */
    private void update_listView(){  //updating list view with items
        client_names = clients.keys();

        adapter = new ClientListAdapter(this, R.layout.adapter_view_layout, client_names);
        listView.setAdapter(adapter);

        //simple way of adding to a list
        //ArrayAdapter arrAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, client_names);
        //listView.setAdapter(arrAdapt);
    }

    /**
     * Updates the distance tracker TextView at the top of the page, accurate to 2 decimal places
     */
    private void update_distance_text(){
        ((TextView) findViewById(R.id.total_distance)).setText(Math.round(clients.total_distance() * 100.0)/100.0 + " km");
    }

    /**
     * Updates the number of visits that have been done for a set of clients
     * @param v The view clicked to activate this method
     */
    public void add_visit(View v){
        //get overall parent of view
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        //get part of view with 2 text boxes
        LinearLayout text_boxes = (LinearLayout) parent.getChildAt(0);

        TextView name = (TextView) text_boxes.getChildAt(0);
        TextView visits = (TextView) text_boxes.getChildAt(1);

        String visit_count = "Visits: " + String.valueOf(Integer.valueOf(visits.getText().toString().replace("Visits: ", "")) + 1);
        //System.out.println(Integer.valueOf(visit_count));
        String route_name = name.getText().toString();

        clients.put(route_name, 2.0);
        visits.setText(visit_count);

        add_to_log(route_name, clients.get(route_name));

        //set starting text of the app
        update_distance_text();

        //save data
        save_data();


    }

    /**
     * Activates pop up to ensure a client deletion
     * @param v The delete button
     */
    public void confirm_delete(View v){
        //get overall parent of view
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        //get part of view with 2 text boxes
        LinearLayout text_boxes = (LinearLayout) parent.getChildAt(0);

        //get name of the client being deleted
        TextView name = (TextView) text_boxes.getChildAt(0);
        Intent i = new Intent(this, PopUp.class);
        i.putExtra("Name", name.getText().toString());

        //launch popup activity
        DeleteConfirmResultLauncher.launch(i);


    }

    public void add_to_log(String name, Double distance){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String str_date = dateFormat.format(date);

        String[] info = new String[]{name, String.valueOf(distance), str_date};

        log_tracker.add(info);

    }

    /**
     * Working with shared preferences to store data
     * Gson allows us to store objects as strings in shared preferences, it can be added in gradle dependencies
     */
    private void save_data(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String text = gson.toJson(clients); //makes our TST a string
        String log_input = gson.toJson(log_tracker);

        editor.putString(CLIENT_ROUTES_KEY, text);
        editor.putString(LOG_KEY, log_input);
        editor.apply();
    }

    /**
     * Retrieve the data within the shared preferences
     */
    private void load_data(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<ClientTST>(){}.getType();
        Type type2 = new TypeToken<ArrayList<String[]>>(){}.getType();
        if (sharedPreferences.getString(CLIENT_ROUTES_KEY,null) == null && sharedPreferences.getString(LOG_KEY,null) == null){  //if its empty do not do anything
            return;
        }
        else if (sharedPreferences.getString(LOG_KEY,null) == null){
            clients = gson.fromJson(sharedPreferences.getString(CLIENT_ROUTES_KEY, null), type);
            return;
        }
        else if (sharedPreferences.getString(CLIENT_ROUTES_KEY,null) == null){
            log_tracker = gson.fromJson(sharedPreferences.getString(LOG_KEY, null), type2);
            return;
        }
        else{
            clients = gson.fromJson(sharedPreferences.getString(CLIENT_ROUTES_KEY, null), type);
            log_tracker = gson.fromJson(sharedPreferences.getString(LOG_KEY, null), type2);
        }

    }
}