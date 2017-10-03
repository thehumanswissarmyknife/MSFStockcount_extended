package com.humanswissarmyknives.msfstockcount;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by dennisvocke on 03.10.17.
 */

public class SingletonClass {

    private static volatile SingletonClass sSoleInstance;
    private static Socket mSocket;
    private static Stack globalStack;
    private static Stack pushedStack;


    //private constructor.
    private SingletonClass() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        try {
            mSocket = IO.socket(MainActivity.url);
            Log.i("Connection", MainActivity.url);
        } catch (URISyntaxException e) {
        }
        globalStack = new Stack();
        pushedStack = new Stack();
    }

    public static SingletonClass getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (SingletonClass.class) {
                if (sSoleInstance == null) sSoleInstance = new SingletonClass();
            }
        }

        return sSoleInstance;
    }

    //Make singleton from serialize and deserialize operation.
    protected SingletonClass readResolve() {
        return getInstance();
    }

    public static Socket getSocket() {
        return mSocket;
    }

    public static Stack getGlobalStack() {

        return globalStack;
    }

    public static Stack getPushedStack() {
        return pushedStack;
    }
}