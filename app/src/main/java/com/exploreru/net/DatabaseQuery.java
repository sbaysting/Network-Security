package com.exploreru.net;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by SAM on 7/10/2015.
 */
public class DatabaseQuery extends Thread{

    InputStream is;
    //The data to send
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    public String result = "";
    String query = "";
    Context context = null;

    public DatabaseQuery(String user, String pass, String query, Context context){
        nameValuePairs.add(new BasicNameValuePair("user",user));
        nameValuePairs.add(new BasicNameValuePair("pass",pass));
        nameValuePairs.add(new BasicNameValuePair("query",query));
        this.query = query;
        this.context = context;
    }

    @Override public void run() {

        boolean test1 = HTTPNetwork.isOnline(context);
        boolean test2 = HTTPNetwork.ping("http://73.10.237.195/query.php",context);
        if(test1 && test2) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                Log.i("Database Query", "Sending query: " + query);
                HttpPost httppost = new HttpPost("http://73.10.237.195/query.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();
                Log.i("Database Query", "Response to query: " + result);
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }
        } else if(test1 == false) {
            Log.e("Database Query","Failed: Not online");
        } else {
            Log.e("Database Query","Failed: Not reachable");
        }

    }
}
