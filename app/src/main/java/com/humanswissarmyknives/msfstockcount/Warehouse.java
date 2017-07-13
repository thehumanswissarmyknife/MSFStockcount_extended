package com.humanswissarmyknives.msfstockcount;

/**
 * Created by dennisvocke on 13.07.17.
 * <p>
 * Defines the class for all Warehouses. Could be MED, LOG, WatSan, ...
 */

class Warehouse {

    int id;
    private String name;
    private String category;
    private int list_id;

    public Warehouse() {

    }

    public Warehouse(String name, String category, int list_id) {
        this.id = 0;
        this.name = name;
        this.category = category;
        this.list_id = list_id;
    }

    Warehouse(int id, String name, String category, int list_id) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.list_id = list_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setList_id(int list_id) {
        this.list_id = list_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    String getCategory() {
        return category;
    }

    int getList_id() {
        return list_id;
    }

    @Override
    public String toString() {
        return category + " - " + name;
    }
}
