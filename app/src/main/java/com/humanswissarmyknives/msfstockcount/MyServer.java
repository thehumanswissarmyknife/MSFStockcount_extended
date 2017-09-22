package com.humanswissarmyknives.msfstockcount;

import android.app.Application;
import android.content.Context;

/**
 * Created by dennisvocke on 22.09.17.
 */

public class MyServer extends Application {

    private static MyStack instance;

    private ServerConnection myServer = new ServerConnection();

    public ServerConnection getMyServer() {
        return myServer;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
