package com.humanswissarmyknives.msfstockcount;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dennisvocke on 13.07.17.
 * <p>
 * This class defines the standard MSF reporting lists, like LSR, MSR, etc
 */

class ReportingList {

    int id;
    private String name;
    private String comment;
    private String category;

    public ReportingList() {
    }

    public ReportingList(String name, String comment, String category) {
        this.id = 0;
        this.name = name;
        this.comment = comment;
        this.category = category;
    }

    ReportingList(int id, String name, String comment, String category) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.category = category;
    }

    public ReportingList(JSONObject myObject) {
        this.id = 0;
        try {
            this.name = myObject.getString("name");
            this.comment = myObject.getString("comment");
            this.category = myObject.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    String getComment() {
        return comment;
    }

    String getCategory() {
        return category;
    }

    public String toString() {
        return category + " - " + name;
    }
}
