package com.exploreru.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.exploreru.activity.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sam on 6/2/2015.
 */
public class HTTPNetwork extends Thread{

    Context c = null;
    String uri = null;
    public String data = null;

    public HTTPNetwork(Context c, String uri){

        this.c = c;
        this.uri = uri;
    }

    public boolean isOnline(){

        ConnectivityManager net = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = net.getActiveNetworkInfo();
        if(info != null && info.isConnectedOrConnecting()){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isOnline(Context c){

        ConnectivityManager net = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = net.getActiveNetworkInfo();
        if(info != null && info.isConnectedOrConnecting()){
            return true;
        } else {
            return false;
        }
    }

    public static boolean ping(String uri, Context context){
        if(isOnline(context)){
            try {
                URL url = new URL(uri);   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(2 * 1000);          // 2 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.i("HTTP Network", "Ping to address " + uri + ": Success");
                    return true;
                } else {
                    Log.e("HTTP Network", "Ping to address " + uri + ": Failed");
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        } else {
            Log.e("HTTP Network", "Ping failed: No network connection");
            return false;
        }
    }

    public boolean ping(){
        if(isOnline(c)){
            try {
                URL url = new URL(uri);   // Change to "http://google.com" for www  test.
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(2 * 1000);          // 2 s.
                urlc.connect();
                if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                    Log.i("HTTP Network", "Ping to address " + uri + ": Success");
                    return true;
                } else {
                    Log.e("HTTP Network", "Ping to address " + uri + ": Failed");
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        } else {
            Log.e("HTTP Network", "Ping failed: No network connection");
            return false;
        }
    }

    @Override public void run(){
        boolean test1 = isOnline();
        boolean test2 = ping();
        if(test1 && test2 && uri != null) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
            HttpGet request = new HttpGet(uri);
            HttpResponse response;

            try {
                response = client.execute(request);
                data = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
                data = null;

            } finally {
                client.close();
            }
        } else if(test1 == false){
            Log.e("HTTP Network","HTTP Post Failed: Not online");
            data = null;
        } else {
            Log.e("HTTP Network","HTTP Post Failed: URL/IP not reachable");
            data = null;
        }
    }

    public void setURL(String uri){
        this.uri = uri;
    }

}
