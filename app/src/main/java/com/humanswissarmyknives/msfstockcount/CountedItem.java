package com.humanswissarmyknives.msfstockcount;

/**
 * Created by dennisvocke on 25.06.17.
 */

class CountedItem {
    private int id;
    private String product_code;
    private int batchNumber_id;
    private int countedQty;
    private int user_id;

    public CountedItem() {

    }

    CountedItem(String product_code, int batchNumber_id, int countedQty, int user_id) {
        this.id = 0;
        this.product_code = product_code;
        this.batchNumber_id = batchNumber_id;
        this.countedQty = countedQty;
        this.user_id = user_id;
    }

    CountedItem(int id, String product_code, int batchNumber_id, int countedQty, int user_id) {
        this.id = id;
        this.product_code = product_code;
        this.batchNumber_id = batchNumber_id;
        this.countedQty = countedQty;
        this.user_id = user_id;
    }

    int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    int getBatchNumber_id() {
        return batchNumber_id;
    }

    public void setBatchNumber_id(int batchNumber_id) {
        this.batchNumber_id = batchNumber_id;
    }

    int getCountedQty() {
        return countedQty;
    }

    public void setCountedQty(int countedQty) {
        this.countedQty = countedQty;
    }

    int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
