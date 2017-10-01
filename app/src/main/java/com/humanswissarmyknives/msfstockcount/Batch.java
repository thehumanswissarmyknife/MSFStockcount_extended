package com.humanswissarmyknives.msfstockcount;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dennisvocke on 10.07.17.
 */

class Batch implements Comparable<Batch>{

    private int batch_id;
    private String serverBatchId;
    private String product_code;
    private String batch_number;
    private String expiry_date;
    private int batch_sud;

    public Batch() {

    }

    private Batch(String product_code, String batch_number, String expiry_date, int batch_sud) {
        this.batch_id = 0;
        this.serverBatchId = "";
        this.product_code = product_code;
        this.expiry_date = expiry_date;
        this.batch_number = batch_number;
        this.batch_sud = batch_sud;
        this.serverBatchId = "";
    }

    Batch(int id, String product_code, String batch_number, String expiry_date, int batch_sud) {
        this.batch_id = id;
        this.serverBatchId = "";
        this.product_code = product_code;
        this.expiry_date = expiry_date;
        this.batch_number = batch_number;
        this.batch_sud = batch_sud;
        this.serverBatchId = "";
    }

    Batch(int id, String serverBatchId, String product_code, String batch_number, String expiry_date, int batch_sud) {
        this.batch_id = id;
        this.serverBatchId = serverBatchId;
        this.product_code = product_code;
        this.expiry_date = expiry_date;
        this.batch_number = batch_number;
        this.batch_sud = batch_sud;
        this.serverBatchId = "";
    }

    public int compareTo (Batch anotherBatch) {
        return this.getBatch_number().compareTo(anotherBatch.getBatch_number());
    }

    int getBatch_id() {
        return batch_id;
    }

    void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    String getServerBatchId() {
        return serverBatchId;
    }

    void setServerBatchId(String serverBatchId) {
        this.serverBatchId = serverBatchId;
    }

    String getProduct_code() {
        return product_code;
    }

    void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    String getBatch_number() {
        return batch_number;
    }

    void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    String getExpiry_date() {
        return expiry_date;
    }

    int getBatch_sud() {
        return batch_sud;
    }

    void setBatch_sud(int batch_sud) {
        this.batch_sud = batch_sud;
    }

    int getTotalBatchQty(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);

        return db.getBatchQtyCountItemByBatchId(this.getBatch_id());
    }

    void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    JSONObject getJSON() {

        JSONObject jsonBatch = new JSONObject();
        try {
            jsonBatch.put("_id", this.batch_id);
            jsonBatch.put("serverBatchId", this.serverBatchId);
            jsonBatch.put("productCode", this.product_code);
            jsonBatch.put("batchNumber", this.batch_number);
            jsonBatch.put("expiryDate", this.expiry_date);
            jsonBatch.put("sud", this.batch_sud);
            return jsonBatch;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
