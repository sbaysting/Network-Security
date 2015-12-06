package com.exploreru.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.exploreru.R;
import com.exploreru.api.dining.Dining;
import com.exploreru.api.dining.Genre;
import com.exploreru.net.HTTPNetwork;

public class DiningAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List> _listDataChild;
    private List<List<Boolean>> availList;
    private List<Dining> data;
    File dining_data;
    File dining_timestamp;

    public DiningAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List> listChildData, List<List<Boolean>> availList, List<Dining> data) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.availList = availList;
        this.data = data;
        dining_data = new File(context.getFilesDir(), "Dining_data.txt");
        dining_timestamp = new File(context.getFilesDir(), "Dining_timestamp.txt");
        /*if(data == null || HTTPNetwork.isOnline(context) == false){
            for(int i = 0;i < availList.size();i++){
                List<Boolean> temp = new ArrayList<Boolean>();
                for(int j = 0;j < availList.get(i).size();j++){
                    temp.add(false);
                }
                availList.set(i,temp);
            }
        }*/
        if(data == null){
            for(int i = 0;i < availList.size();i++){
                List<Boolean> temp = new ArrayList<Boolean>();
                for(int j = 0;j < availList.get(i).size();j++){
                    temp.add(false);
                }
                availList.set(i,temp);
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //Takes inputs as an XML file and generates a view out of it for the child of the list
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if(childPosition == 0){
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Define the layout inflator for the current activity
            convertView = infalInflater.inflate(R.layout.dining_location_data, null); //Generate a view from the XML file "dining_location_data.xml"

            ImageView img = (ImageView)convertView.findViewById(R.id.dhImage);
            TextView address = (TextView)convertView.findViewById(R.id.dhInfo);
            Button link = (Button)convertView.findViewById(R.id.dhWeb);

            File imgFile;
            switch(groupPosition){
                case 0:
                    img.setBackgroundResource(R.drawable.browerdh);
                    address.setText("Brower Commons" +
                            "\n145 College Avenue\n" +
                            "New Brunswick, NJ 08901");
                    link.setText("Current Hours");
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uriUrl = Uri.parse("https://food.rutgers.edu/places-to-eat/eateries/1/4");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            _context.startActivity(launchBrowser);
                        }
                    });
                    break;
                case 1:
                    img.setBackgroundResource(R.drawable.buschdh);
                    address.setText("Busch Dining Hall" +
                            "\n608 Bartholomew Road" +
                            "\nPiscataway, NJ 08854");
                    link.setText("Current Hours");
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uriUrl = Uri.parse("https://food.rutgers.edu/places-to-eat/eateries/3/5");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            _context.startActivity(launchBrowser);
                        }
                    });
                    break;
                case 2:
                    img.setBackgroundResource(R.drawable.lividh);
                    address.setText("Livingston Dining Commons" +
                            "\n85 Avenue E\n" +
                            "Piscataway, NJ 08854");
                    link.setText("Current Hours");
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uriUrl = Uri.parse("https://food.rutgers.edu/places-to-eat/eateries/4/7");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            _context.startActivity(launchBrowser);
                        }
                    });
                    break;
                case 3:
                    img.setBackgroundResource(R.drawable.douglassdh);
                    address.setText("Neilson Dining Hall" +
                            "\n177 Ryders Lane\n" +
                            "New Brunswick, NJ 08901");
                    link.setText("Current Hours");
                    link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uriUrl = Uri.parse("https://food.rutgers.edu/places-to-eat/eateries/2/1");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            _context.startActivity(launchBrowser);
                        }
                    });
                    break;
            }

            return convertView;
        }

        final String childText = (String) getChild(groupPosition, childPosition - 1); //Get text info from the child hashmap

        LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Define the layout inflator for the current activity
        convertView = infalInflater.inflate(R.layout.meal_list, null); //Generate a view from the XML file "meal_list.xml"

        if(isChildSelectable(groupPosition,childPosition)) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(_context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.meal_overlay);
                    dialog.setCanceledOnTouchOutside(true);
                    List<Genre> genres = data.get(groupPosition).meals.get(childPosition-1).genres;
                    List<String> data = new ArrayList<String>();
                    for(int i = 0;i < genres.size();i++){
                        data.add(genres.get(i).genre_name);
                        for(int j = 0;j < genres.get(i).items.size();j++){
                            data.add(genres.get(i).items.get(j));
                        }
                    }
                    //for dismissing anywhere you touch
                    LinearLayout masterView = (LinearLayout)dialog.findViewById(R.id.meal_overlay_layout);
                    masterView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    ListView overlayList = (ListView)dialog.findViewById(R.id.listView);
                    overlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            dialog.dismiss();
                        }
                    });
                    if(overlayList==null){
                        Log.e("ListView","LISTVIEW IS NULL");
                    }
                    ListAdapter overlay = new MealOverlayAdapter(_context,data,genres);
                    if(overlay==null){
                        Log.e("ListAdapter","LISTADAPTER IS NULL");
                    }
                    overlayList.setAdapter(overlay);
                    dialog.show();
                }
            });
        }

        LinearLayout l = (LinearLayout)convertView.findViewById(R.id.mealLayout);
        if(childPosition == availList.get(groupPosition).size()) {
            l.setBackgroundResource(R.drawable.dining_inner_bottom);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem); //Find the textview named "lblListItem" within the view
        TextView txtListAvail = (TextView) convertView.findViewById(R.id.lblItemAvail);

        txtListChild.setText(childText); //Set the text within the view

        if((Boolean)availList.get(groupPosition).get(childPosition-1)) {
            txtListAvail.setText("AVAILABLE");
            txtListAvail.setTextColor(_context.getResources().getColor(R.color.availableText));
       } else if(data == null || !dining_data.exists() || !dining_timestamp.exists()){
            txtListAvail.setText("NO NETWORK");
            txtListAvail.setTextColor(Color.RED);
        } else {
            txtListAvail.setText("UNAVAILABLE");
            txtListAvail.setTextColor(Color.RED);
        }


        return convertView; //Return converted XML file
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //Takes an XML file and generates a view out of it for the parent of the list
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition); //Get text info from the parent
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //Call layout inflator service
            convertView = infalInflater.inflate(R.layout.location_list, null); //Convert "location_list.xml" to a view
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader); //Retrieve the "lblListHeader" textview from the parent
        lblListHeader.setTypeface(null, Typeface.BOLD); //Bold the text inside it
        lblListHeader.setText(headerTitle); //Set the text inside of it

        return convertView; //Return the converted view
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        try {
            if (availList.get(groupPosition).get(childPosition-1) == true) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e){
            return false;
        }
    }
}