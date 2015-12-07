package com.exploreru.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exploreru.R;
import com.exploreru.api.dining.Genre;

import java.util.List;

/**
 * Created by Sam on 6/8/2015.
 */
public class MealOverlayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private final List<Genre> genres;

    public MealOverlayAdapter(Context context, List<String> values, List<Genre> genres) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.genres = genres;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.location_list, null);
        LinearLayout l = (LinearLayout)rowView.findViewById(R.id.locationListLayout);
        l.setBackgroundResource(R.drawable.dining_inner_overlay);

        TextView textView = (TextView) rowView.findViewById(R.id.lblListHeader);

        boolean flag = false;
        for(int i = 0;i < genres.size();i++){
            if(values.get(position).equals(genres.get(i).genre_name)){
                flag = true;
            }
        }
        if(flag) { //If its a genre name,
            textView.setText(values.get(position)); //Set it
            textView.setTypeface(null, Typeface.BOLD); //And bold it
            l.setBackgroundResource(R.drawable.dining_inner_border);
        } else {
            textView.setText(values.get(position));
        }
        textView.setTextSize(15.0f);
        textView.setPadding(0,0,0,0);


        return rowView;
    }
}
