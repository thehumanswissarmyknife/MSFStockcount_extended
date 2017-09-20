package com.humanswissarmyknives.msfstockcount;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dennisvocke on 20.09.17.
 */

public class PostJson extends AsyncTask<String, Void, String> {

    boolean postBatch;
    boolean postCountedItem;
    boolean postAll;

    int batchId;
    String foreignBatchId;
    String productCode;
    String batchNumber;
    String expiryDate;
    int batchSud;

    int countedItemId;
    String foreignCountedItemId;
    int sud;
    int userId;
    int countedQty;

    public PostJson(Batch batch) {
        this.batchId = batch.getBatch_id();
        this.productCode = batch.getProduct_code();
        this.batchNumber = batch.getBatch_number();
        this.expiryDate = batch.getExpiry_date();
        this.batchSud = batch.getBatch_sud();
        this.postBatch = true;
        this.postAll = false;
        this.postCountedItem = false;
    }

    public PostJson(CountedItem countedItem) {
        this.countedItemId = countedItem.getId();
        this.productCode = countedItem.getProduct_code();
        this.sud = countedItem.getSud();
        this.userId = countedItem.getUser_id();
        this.countedQty = countedItem.getCountedQty();
        this.batchId = countedItem.getBatchNumber_id();
        this.postBatch = false;
        this.postAll = false;
        this.postCountedItem = true;
    }

    public PostJson(Batch batch, CountedItem countedItem) {
        this.batchId = batch.getBatch_id();
        this.productCode = batch.getProduct_code();
        this.batchNumber = batch.getBatch_number();
        this.expiryDate = batch.getExpiry_date();
        this.batchSud = batch.getBatch_sud();
        this.countedItemId = countedItem.getId();
        this.sud = countedItem.getSud();
        this.userId = countedItem.getUser_id();
        this.countedQty = countedItem.getCountedQty();
        this.batchId = countedItem.getBatchNumber_id();
        this.postBatch = true;
        this.postAll = true;
        this.postCountedItem = true;
    }

    public String getBatchId() {
        return foreignBatchId;
    }


    @Override
    protected String doInBackground(String... urls) {

        if (postBatch) {
            try {

                URL url = new URL("http://192.168.178.42:3000/batches"); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.addRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.connect();

                JSONObject jsonBatch = new JSONObject();
                jsonBatch.put("productCode", productCode);
                jsonBatch.put("batchNumber", batchNumber);
                jsonBatch.put("expiryDate", expiryDate);
                jsonBatch.put("sud", batchSud);


                Log.i("object", jsonBatch.toString());

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(jsonBatch.toString());
                wr.flush();


                //  Here you read any answer from server.
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                JSONObject serverBatch;
                while ((line = serverAnswer.readLine()) != null) {

                    serverBatch = new JSONObject(line);
                    foreignBatchId = serverBatch.optJSONObject("batch").optString("_id");

                    Log.i("LINE: ", line); //<--If any response from server
                    //use it as you need, if server send something back you will get it here.
                }

                wr.close();
                serverAnswer.close();
                return foreignBatchId;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
