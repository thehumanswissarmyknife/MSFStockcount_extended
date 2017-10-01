package com.humanswissarmyknives.msfstockcount;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
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
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

/**
 * Created by dennisvocke on 20.09.17.
 */

public class PostJson extends AsyncTask<String, Void, String> {

    DatabaseHandler db;
    Context context;
    Stack globalStack;

    boolean postBatch;
    boolean postCountedItem;
    boolean postAll;

    int batchId;
    String serverBatchId;
    String productCode;
    String batchNumber;
    String expiryDate;
    int batchSud;

    int countedItemId;
    String serverCountedItemId;
    int sud;
    int userId;
    int countedQty;

    public PostJson(Batch batch, Stack stack) {
        this.batchId = batch.getBatch_id();
        this.productCode = batch.getProduct_code();
        this.batchNumber = batch.getBatch_number();
        this.expiryDate = batch.getExpiry_date();
        this.batchSud = batch.getBatch_sud();
        this.postBatch = true;
        this.postAll = false;
        this.postCountedItem = false;
        this.globalStack = stack;
    }

    public PostJson(CountedItem countedItem, Stack stack) {
        this.countedItemId = countedItem.getId();
        this.productCode = countedItem.getProduct_code();
        this.sud = countedItem.getSud();
        this.userId = countedItem.getUser_id();
        this.countedQty = countedItem.getCountedQty();
        this.batchId = countedItem.getBatchNumber_id();
        this.postBatch = false;
        this.postAll = false;
        this.postCountedItem = true;
        this.globalStack = stack;
    }

    public PostJson(Batch batch, CountedItem countedItem, Stack stack) {
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
        Log.i("Posting", "both");
        this.globalStack = stack;
    }

    boolean serverReachable(String url, int port) {
        boolean exists = false;

        try {
            SocketAddress sockaddr = new InetSocketAddress(url, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
        } catch (IOException e) {
            // Handle exception
        }
        return exists;
    }

    public String getBatchId() {
        return serverBatchId;
    }


    @Override
    protected String doInBackground(String... urls) {
        db = new DatabaseHandler(context);

        if (postBatch) {
            try {
                Log.i("Start posting", "batch");

                URL url = new URL(MainActivity.getUrl() + "/batches"); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.addRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.connect();

                JSONObject jsonBatch = new JSONObject();
                jsonBatch.put("batchId", batchId);
                jsonBatch.put("productCode", productCode);
                jsonBatch.put("batchNumber", batchNumber);
                jsonBatch.put("expiryDate", expiryDate);
                jsonBatch.put("sud", batchSud);


                Log.i("Batch posted", jsonBatch.toString());

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(jsonBatch.toString());
                wr.flush();


                //  Here you read any answer from server.
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                JSONObject serverBatch;
                while ((line = serverAnswer.readLine()) != null) {

                    serverBatch = new JSONObject(line);
                    serverBatchId = serverBatch.optJSONObject("batch").optString("_id");
//                    if(check if the stackItem in the globalstack is this one and if so, push the serverBatchId to it...)
                    Log.i("Stack batch before rem", "2");
                    removeItemsFromStack(serverBatchId, "");
                    Log.i("Stack batch after", globalStack.myStack.get(0).getBatch().getServerBatchId());

                    Log.i("Server Batch ", line); //<--If any response from server
                    //use it as you need, if server send something back you will get it here.
                }

                wr.close();
                serverAnswer.close();
//                return serverBatchId;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (postCountedItem) {

            try {
                Log.i("Start posting", "counted item");
                URL url = new URL(MainActivity.getUrl() + "/counteditems"); //Enter URL here
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
                httpURLConnection.addRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.connect();

                JSONObject jsonCountedItem = new JSONObject();
                jsonCountedItem.put("batchId", serverBatchId);
                jsonCountedItem.put("productCode", productCode);
                jsonCountedItem.put("totalQuantity", countedQty);
                jsonCountedItem.put("sud", sud);
                jsonCountedItem.put("user", userId);


                Log.i("Counted item posted", jsonCountedItem.toString());

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(jsonCountedItem.toString());
                wr.flush();


                //  Here you read any answer from server.
                BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                JSONObject serverCountedItem;
                while ((line = serverAnswer.readLine()) != null) {

                    serverCountedItem = new JSONObject(line);
                    serverBatchId = serverCountedItem.optJSONObject("countedItem").optString("_id");

                    Log.i("server counted item", line); //<--If any response from server
                    //use it as you need, if server send something back you will get it here.
                }

                wr.close();
                serverAnswer.close();
                return serverBatchId;


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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MyStack myStack = new MyStack();
        globalStack = myStack.getMyStack();

        if (postCountedItem && postBatch) {
            Log.i("Batch", batchNumber + " Countedite: " + String.valueOf(countedItemId));
        } else if (postBatch) {
            Log.i("Batch", batchNumber);
            if (globalStack.getStackHeight() > 0 && globalStack.getOldestStackItem().getBatch().getBatch_id() == batchId) {
                globalStack.removeOldestStackItem();
            }
        } else if (postCountedItem) {
            Log.i("Counted Item", String.valueOf(countedItemId));
        }

    }

    void removeItemsFromStack(String batchID, String countedItemId) {

        for (int i = 0; i < globalStack.myStack.size(); i++) {
            if (globalStack.myStack.get(i).getBatch().getServerBatchId() == batchID) {
                globalStack.myStack.get(i).removeBatch();
            }
            if (globalStack.myStack.get(i).getCountedItem().getServerId() == countedItemId) {
                globalStack.myStack.get(i).removeCountedItem();
            }
            if (globalStack.myStack.get(i).getBatch() == null && globalStack.myStack.get(i).getCountedItem() == null) {
                globalStack.myStack.remove(i);
            }
        }

    }
}
