package com.humanswissarmyknives.msfstockcount;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dennisvocke on 19.09.17.
 */

class DownloadTask extends AsyncTask<String, Void, String> {


    // get the db connection set up through the databashandler


    @Override
    protected String doInBackground(String... urls) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    JSONArray getLatestBatches(String s) {
        super.onPostExecute(s);
        JSONArray batches = null;

        try {
            JSONObject jsonObject = new JSONObject(s);
            String jsonString;

            batches = jsonObject.optJSONArray("batches");
            if (batches != null) {
                for (int i = 0; i < batches.length(); i++) {
                    JSONObject tempObject = batches.getJSONObject(i);
                    Log.i("Batch", tempObject.toString());
                }
                return batches;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return batches;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            String jsonString;

            JSONArray batches = jsonObject.optJSONArray("batches");
            if (batches != null) {
                for (int i = 0; i < batches.length(); i++) {
                    JSONObject tempObject = batches.getJSONObject(i);
                    Log.i("Batch", tempObject.toString());
                }
            }

            JSONArray products = jsonObject.optJSONArray("products");
            if (products != null) {
                for (int i = 0; i < products.length(); i++) {
                    JSONObject tempObject = products.getJSONObject(i);
                    Log.i("Product", tempObject.toString());
                }
            }

            JSONArray messages = jsonObject.optJSONArray("messages");
            if (messages != null) {
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject tempObject = messages.getJSONObject(i);
                    Log.i("Message", tempObject.toString());
                }
            }

            JSONArray countedItems = jsonObject.optJSONArray("countedItems");
            if (countedItems != null) {
                for (int i = 0; i < countedItems.length(); i++) {
                    JSONObject tempObject = countedItems.getJSONObject(i);
                    Log.i("Counted Item", tempObject.toString());
                }
            }

            JSONArray reportingLists = jsonObject.optJSONArray("reportinglists");
            if (reportingLists != null) {
                for (int i = 0; i < reportingLists.length(); i++) {
                    JSONObject tempObject = reportingLists.getJSONObject(i);
                    Log.i("Reportinglists", tempObject.toString());
                }
            }

            JSONArray warehouses = jsonObject.optJSONArray("warehouses");
            if (warehouses != null) {
                for (int i = 0; i < warehouses.length(); i++) {
                    JSONObject tempObject = warehouses.getJSONObject(i);
                    Log.i("Warehouses", tempObject.toString());
                }
            }

            JSONArray users = jsonObject.optJSONArray("users");
            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject tempObject = users.getJSONObject(i);
                    User tempUser = new User(tempObject.getString("userName"), tempObject.getString("userFunction"), tempObject.getString("userLevel"), "peter");
                    // db.addUser(tempUser);
                    Log.i("Users", tempObject.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}