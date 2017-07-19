package com.humanswissarmyknives.msfstockcount;

import android.content.Context;

/**
 * Created by dennisvocke on 24.06.17.
 * used for all products in the system
 */

class Product implements Comparable<Product>{
    private int product_id;
    private String product_code;
    private String product_description;
    private int product_sud;
    private String isBatchManaged;

    public Product() {

    }

    @Override
    public int compareTo(Product anotherProduct) {
        return this.getProduct_code().compareTo(anotherProduct.getProduct_code());
    }

    Product(int id, String product_code, String product_description, int product_sud, String isBatchManaged) {
        this.product_id = id;
        this.product_code = product_code;
        this.product_description = product_description;
        this.product_sud = product_sud;
        this.isBatchManaged = isBatchManaged;
    }

    Product(String product_code, String product_description, int product_sud, String isBatchManaged) {
        this.product_id = 0;
        this.product_code = product_code;
        this.product_description = product_description;
        this.product_sud = product_sud;
        this.isBatchManaged = isBatchManaged;
    }

    int getProduct_id() {
        return product_id;
    }

    void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    String getProduct_code() {
        return product_code;
    }

    void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    String getProduct_description() {
        return product_description;
    }

    void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    int getProduct_sud() {
        return product_sud;
    }

    void setProduct_sud(int product_sud) {
        this.product_sud = product_sud;
    }

    String getIsBatchManaged() {
        return isBatchManaged;
    }

    void setIsBatchManaged(String isBatchManaged) {
        this.isBatchManaged = isBatchManaged;
    }

    int getTotalProductQty(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);

        return db.getTotalQtyCountItemByProductCode(this.getProduct_code());
    }
}
