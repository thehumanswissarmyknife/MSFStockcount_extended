package com.humanswissarmyknives.msfstockcount;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by dennisvocke on 22.09.17.
 */

public class ServerConnection {

    String url;
    int port;

    boolean reachable;

    public ServerConnection() {
        this.url = "http://192.168.178.42";
        this.port = 3000;
    }

    public String getUrl() {
        return url;
    }

    public String getFullUrl() {
        return url + ":" + port;
    }

    boolean isReachable(String url, int port) {
        boolean exists = false;

        try {
            SocketAddress sockaddr = new InetSocketAddress(url, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
        } catch (IOException e) {
            // Handle exception
        }
        return exists;
    }
}
