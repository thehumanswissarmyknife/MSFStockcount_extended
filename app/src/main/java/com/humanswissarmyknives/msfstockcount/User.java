package com.humanswissarmyknives.msfstockcount;

/**
 * Created by dennisvocke on 24.06.17.
 */

class User {
    private int id;
    private String name;
    private String function;
    private String level;

    public User() {

    }

    User(String name, String function, String level) {
        this.name = name;
        this.function = function;
        this.level = level;
    }

    User(int id, String name, String function, String level) {
        this.id = id;
        this.name = name;
        this.function = function;
        this.level = level;
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
}

