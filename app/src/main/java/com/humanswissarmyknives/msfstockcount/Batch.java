package com.humanswissarmyknives.msfstockcount;

import android.content.Context;

/**
 * Created by dennisvocke on 10.07.17.
 */

class Batch implements Comparable<Batch>{

    private int batch_id;
    private String product_code;
    private String batch_number;
    private String expiry_date;
    private int batch_sud;

    public Batch() {

    }

    private Batch(String product_code, String batch_number, String expiry_date, int batch_sud) {
        this.batch_id = 0;
        this.product_code = product_code;
        this.expiry_date = expiry_date;
        this.batch_number = batch_number;
        this.batch_sud = batch_sud;
    }

    Batch(int id, String product_code, String batch_number, String expiry_date, int batch_sud) {
        this.batch_id = id;
        this.product_code = product_code;
        this.expiry_date = expiry_date;
        this.batch_number = batch_number;
        this.batch_sud = batch_sud;
    }

    public int compareTo (Batch anotherBatch) {
        return this.getBatch_number().compareTo(anotherBatch.getBatch_number());
    }

    int getBatch_id() {
        return batch_id;
    }

    private void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }

    String getProduct_code() {
        return product_code;
    }

    private void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    String getBatch_number() {
        return batch_number;
    }

    private void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    String getExpiry_date() {
        return expiry_date;
    }

    int getBatch_sud() {
        return batch_sud;
    }

    private void setBatch_sud(int batch_sud) {
        this.batch_sud = batch_sud;
    }

    int getTotalBatchQty(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);

        return db.getBatchQtyCountItemByBatchId(this.getBatch_id());
    }

    private void setExpiryDate(String expiry_date) {
        this.expiry_date = expiry_date;
    }

}
