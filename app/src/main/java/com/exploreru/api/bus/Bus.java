package com.exploreru.api.bus;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.exploreru.net.HTTPNetwork;
import com.exploreru.net.XMLParser;

import java.util.List;

/**
 * Created by Sam on 7/15/2015.
 */
public class Bus {

    /**
     * This class is used to parse and return bus information from the NextBus API.
     * All actual parsing will be done in the JSONParser and XMLParser classes, but this
     * class will filter out desired information and return it to the desired function
     *
     * HTTPNetwork objects will ping the server for information
     *
     * See http://api.rutgers.edu/ for more information on the API's
     */

    //URL to the XML formatted NextBus API
    private static final String XMLBaseUrl = "http://webservices.nextbus.com/service/publicXMLFeed?a=rutgers&command=";
    //URL to the JSON formatted NextBus API
    private static final String JSONBaseUrl = "http://runextbus.heroku.com/";

    public static List<Pair> getRouteCoordinates(Context c, String busRoute){

        try {
            HTTPNetwork XMLInfo = new HTTPNetwork(c, XMLBaseUrl + "routeConfig");
            XMLInfo.start(); //Start HTTPNetwork, get data from server
            XMLInfo.join(); //Wait for thread to finish
            return XMLParser.parseCoordPairs(XMLInfo.data); //Parse info and return

        } catch (InterruptedException e){
            Log.e("HTTPNetwork","Network was interrupted");
            return null;
        }
    }

    public static Integer getPrediction(String busRoute){

        return 0;
    }

    public static List<Float> getCurrentLocation(String busRoute){

        return null;
    }

}
