package com.exploreru.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.exploreru.R;
import com.exploreru.adapter.DiningAdapter;
import com.exploreru.api.dining.Dining;
import com.exploreru.net.HTTPNetwork;
import com.exploreru.net.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class DiningFragment extends Fragment {

    public static final String CONTEXT = "context";
    Context context;

    public HTTPNetwork net;
    List<Dining> dataParsed;
    String data;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List> listDataChild;
    List<List<String>> itemList;
    List<List<Boolean>> availList;
    Thread network;
    File dining_data;
    File dining_timestamp;
    Calendar c;

    public static DiningFragment newInstance(Context context) {
        Bundle args = new Bundle();
        DiningFragment fragment = new DiningFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.net = new HTTPNetwork(context,"https://rumobile.rutgers.edu/1/rutgers-dining.txt");
        fragment.getData();
        return fragment;
    }

    public void setContext (Context context){
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dining, container, false);

        expListView = (ExpandableListView) view.findViewById(R.id.diningListView);

        // preparing list data
        try {
            network.join();
            prepareListData();
            listAdapter = new DiningAdapter(context, listDataHeader, listDataChild, availList, dataParsed);
            // setting list adapter
            expListView.setAdapter(listAdapter);

        }catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public void getData(){

        //Create file pointers and get access to calendar information
        dining_data = new File(context.getFilesDir(), "Dining_data.txt");
        dining_timestamp = new File(context.getFilesDir(), "Dining_timestamp.txt");
        Log.i("Dining Data", "Set dining data path to: "+dining_data.getAbsolutePath());
        Log.i("Dining Data", "Set dining timestamp path to: " + dining_timestamp.getAbsolutePath());
        c = Calendar.getInstance();

        //Create network thread
        network = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("Dining Data", "Read from server!");
                    net.start();
                    net.join();
                    PrintWriter dining_data_writer = new PrintWriter(dining_data);
                    PrintWriter dining_time_writer = new PrintWriter(dining_timestamp);
                    //Write new timestamp
                    Log.i("Dining Data", "Writing new timestamp in "+dining_timestamp.getAbsolutePath());
                    dining_time_writer.println(c.get(Calendar.DAY_OF_YEAR));
                    dining_time_writer.print(c.get(Calendar.HOUR_OF_DAY));
                    dining_time_writer.close();
                    //Write new data
                    Log.i("Dining Data", "Writing new data in "+dining_data.getAbsolutePath());
                    dining_data_writer.print(net.data);
                    dining_data_writer.close();
                    //Assign list data
                    dataParsed = JSONParser.parseDiningAPI(net.data);
                }catch(InterruptedException e){
                    e.printStackTrace();
                    dataParsed = null;
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                    dataParsed = null;
                }
            }
        });

        //If both files exist
        if(dining_data.exists() && dining_timestamp.exists()){
            //Read text from file
            Log.i("Dining Data", "Both files exist! Checking timestamp...");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dining_timestamp));
                String line;
                int count = 0;
                boolean nextDay = false;
                boolean pastThreeAM = false;
                while ((line = reader.readLine()) != null) {
                    //This is the day of the year, check if the current day is greater than the listed day
                    Log.i("Dining Data", "Reading line "+count+" in "+dining_timestamp.getAbsolutePath());
                    Log.i("Dining Data", "Line data: "+line);
                    if(count == 0 && (c.get(Calendar.DAY_OF_YEAR) > (new Integer(line)))){
                        Log.i("Dining Data", "Setting nextDay = true!");
                        nextDay = true;
                    }
                    //This is the hour of the day, check if the current hour is greater than three AM
                    if(count == 1 &&(3 >= (new Integer(line)))){
                        Log.i("Dining Data", "Setting pastThreeAM = true!");
                        pastThreeAM = true;
                    }
                    count++;
                }
                reader.close();
                // If next day and past three AM, get new data
                if(nextDay && pastThreeAM){
                    network.start();
                }
                // Otherwise, read from data file
                else{
                    network = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.i("Dining Data", "Read from file!");
                                BufferedReader reader = new BufferedReader(new FileReader(dining_data));
                                Log.i("Dining Data", "Reading from data file in "+dining_data.getAbsolutePath());
                                String line;
                                StringBuilder data = new StringBuilder();
                                while ((line = reader.readLine()) != null) {
                                    data.append(line);
                                }
                                dataParsed = JSONParser.parseDiningAPI(data.toString());
                            }catch(FileNotFoundException e){
                                e.printStackTrace();
                                dataParsed = null;
                            }catch(IOException e){
                                e.printStackTrace();
                                dataParsed = null;
                            }
                        }
                    });
                    network.start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i("Dining Data", "One or both files don't exist, creating them now");
            network.start();
        }
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        itemList = new ArrayList<List<String>>();
        availList = new ArrayList<List<Boolean>>();
        listDataChild = new HashMap<String, List>();

        if(dataParsed != null) {
            // Adding child data
            for (int i = 0; i < dataParsed.size(); i++) {
                listDataHeader.add(dataParsed.get(i).location_name);
                List<String> dataList = new ArrayList<String>();
                List<Boolean> isAvail = new ArrayList<Boolean>();
                for (int j = 0; j < dataParsed.get(i).meals.size(); j++) {
                    dataList.add(dataParsed.get(i).meals.get(j).meal_name);
                    isAvail.add(dataParsed.get(i).meals.get(j).meal_avail);
                }
                dataList.add(null);
                itemList.add(dataList);
                availList.add(isAvail);
            }

            for (int i = 0; i < listDataHeader.size(); i++) {
                listDataChild.put(listDataHeader.get(i), itemList.get(i)); // Header, Child data
            }
        } else {
            List<String> dataList = new ArrayList<String>();
            dataList.add("Network/source not available");
            listDataChild.put("Network/source not available",dataList);
        }

    }
}