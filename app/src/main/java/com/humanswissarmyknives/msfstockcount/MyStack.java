package com.humanswissarmyknives.msfstockcount;

import android.app.Application;
import android.content.Context;

/**
 * Created by dennisvocke on 21.09.17.
 */

class MyStack extends Application {

    private static MyStack instance;

    private Stack myStack = new Stack();
    private Stack pushedStack = new Stack();

    public Stack getMyStack() {
        return myStack;
    }

    public Stack getPushedStack() {
        return pushedStack;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

}
