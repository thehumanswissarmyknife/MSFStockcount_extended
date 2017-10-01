package com.humanswissarmyknives.msfstockcount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dennisvocke on 24.06.17.
 */

class User {
    private int id;
    private String serverId;
    private String name;
    private String function;
    private String level;
    private String password;

    public User() {

    }

    public User(JSONObject myObject) {
        try {
            this.id = 0;
            this.serverId = myObject.getString("_id");
            this.name = myObject.getString("userName");
            this.function = myObject.getString("userFunction");
            this.level = myObject.getString("userLevel");
            this.password = myObject.getString("userPassword");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    User(String name, String function, String level, String password) {
        this.id = 0;
        this.name = name;
        this.function = function;
        this.level = level;
        this.password = password;
        this.serverId = "";
    }

    User(int id, String name, String function, String level, String password) {
        this.id = id;
        this.name = name;
        this.function = function;
        this.level = level;
        this.password = password;
        this.serverId = "";
    }

    int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    // override the toString mthod to pass cool info for the spinner on the first screen
    @Override
    public String toString() {
        return name + " (" + function + ")";
    }
}

