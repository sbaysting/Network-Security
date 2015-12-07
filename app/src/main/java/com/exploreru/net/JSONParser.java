package com.exploreru.net;

import android.util.Log;

import com.exploreru.api.dining.Dining;
import com.exploreru.api.dining.Genre;
import com.exploreru.api.dining.Meal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 6/2/2015.
 */
public class JSONParser {

    public static List<Dining> parseDiningAPI(String data){

        List<Dining> diningList = new ArrayList<Dining>();

        try {
            JSONArray array = new JSONArray(data);
            for(int i = 0;i < array.length();i++) {
                Dining dining = new Dining();
                JSONObject obj = array.getJSONObject(i);
                dining.date = obj.getInt("date");
                dining.location_name = obj.getString("location_name");

                JSONArray meals = obj.getJSONArray("meals");
                List<Meal> mealList = new ArrayList<Meal>();
                for (int j = 0; j < meals.length(); j++) {
                    JSONObject obj2 = meals.getJSONObject(j);
                    Meal meal = new Meal();
                    meal.meal_name = obj2.getString("meal_name");
                    meal.meal_avail = obj2.getBoolean("meal_avail");
                    JSONArray genres;
                    try {
                        genres = obj2.getJSONArray("genres");
                    }catch(JSONException e){
                        genres = null;
                    }
                    List<Genre> genreList = new ArrayList<Genre>();
                    if(genres != null) {
                        for (int k = 0; k < genres.length(); k++) {
                            JSONObject obj3 = genres.getJSONObject(k);
                            Genre genre = new Genre();
                            genre.genre_name = obj3.getString("genre_name");

                            JSONArray items;
                            try {
                                items = obj3.getJSONArray("items");
                            }catch(JSONException e){
                                items = null;
                            }
                            List<String> itemList = new ArrayList<String>();
                            if(items != null) {
                                for (int l = 0; l < items.length(); l++) {
                                    itemList.add((String) items.get(l));
                                }
                            }
                            genre.items = itemList;
                            genreList.add(genre);
                        }
                    }
                    meal.genres = genreList;
                    mealList.add(meal);
                }
                dining.meals = mealList;
                diningList.add(dining);
            }

            return diningList;

        } catch (Exception e){
            e.printStackTrace();
            return null;

        }
    }

    public static String parseBlankFrag(String data) {
        //parse json data
        try {
            JSONArray jArray = new JSONArray(data);
            JSONObject json_data = null;
            for (int i = 0; i < jArray.length(); i++) {
                json_data = jArray.getJSONObject(i);
            }
            return json_data.getString("name");
        } catch (JSONException e) {
            Log.e("log_tag", "Error parsing data " + e.toString());
            return (String) data;
        }
    }

}
