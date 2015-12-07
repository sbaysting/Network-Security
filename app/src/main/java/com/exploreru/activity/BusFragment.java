package com.exploreru.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// In this case, the fragment displays simple text based on the page
public class BusFragment extends Fragment {

    Context context;

    HTTPNetwork net;
    List<Dining> dataParsed;
    String data;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List> listDataChild;
    List<List<String>> itemList;
    List<List<Boolean>> availList;
    Thread network;

    public static DiningFragment newInstance() {
        Bundle args = new Bundle();
        DiningFragment fragment = new DiningFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dining, container, false);
        // get the listview
        context = view.getContext();
        net = new HTTPNetwork(context,"https://rumobile.rutgers.edu/1/rutgers-dining.txt");

        getData();
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

    private void getData(){

        network = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    net.start();
                    net.join();
                    dataParsed = JSONParser.parseDiningAPI(net.data);
                }catch(InterruptedException e){
                    e.printStackTrace();
                    dataParsed = null;
                }
            }
        });
        network.start();
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        itemList = new ArrayList<List<String>>();
        availList = new ArrayList<List<Boolean>>();
        listDataChild = new HashMap<String, List>();

        // Adding child data
        for(int i = 0;i < dataParsed.size();i++){
            listDataHeader.add(dataParsed.get(i).location_name);
            List<String> dataList = new ArrayList<String>();
            List<Boolean> isAvail = new ArrayList<Boolean>();
            for(int j = 0;j < dataParsed.get(i).meals.size();j++){
                dataList.add(dataParsed.get(i).meals.get(j).meal_name);
                isAvail.add(dataParsed.get(i).meals.get(j).meal_avail);
            }
            dataList.add(null);
            itemList.add(dataList);
            availList.add(isAvail);
        }

        for(int i = 0;i < listDataHeader.size();i++) {
            listDataChild.put(listDataHeader.get(i), itemList.get(i)); // Header, Child data
        }

    }
}