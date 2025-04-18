package com.example.bttuan10_socket.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class SocketClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private static final int CONNECTION_TIMEOUT = 5000;

    public void connectToServer(String host, int port) {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);

            socket.setSoTimeout(10000);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Log.d("SocketClient", "Connected to server: " + host + ":" + port);
        } catch (IOException e) {
            Log.e("SocketClient", "Error connecting to server: " + e.getMessage(), e);
            closeConnection();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public boolean sendMessage(String message) {
        if (isConnected() && out != null) {
            try {
                out.println(message);
                if (out.checkError()) {
                    Log.e("SocketClient", "PrintWriter error when sending message");
                    return false;
                }
                Log.d("SocketClient", "Message sent: " + message);
                return true;
            } catch (Exception e) {
                Log.e("SocketClient", "Error sending message: " + e.getMessage(), e);
                return false;
            }
        } else {
            Log.e("SocketClient", "Cannot send message - socket not connected");
            return false;
        }
    }

    public String receiveMessage() {
        try {
            if (isConnected() && in != null) {
                String message = in.readLine();
                if (message != null) {
                    Log.d("SocketClient", "Message received: " + message);
                } else {
                    Log.d("SocketClient", "No message received, connection may be closed");
                }
                return message;
            }
        } catch (SocketException e) {
            Log.e("SocketClient", "Socket exception: " + e.getMessage());
            closeConnection();
        } catch (IOException e) {
            Log.e("SocketClient", "Error receiving message: " + e.getMessage(), e);
            if (!isConnected()) {
                closeConnection();
            }
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
            Log.d("SocketClient", "Connection closed");
        } catch (IOException e) {
            Log.e("SocketClient", "Error closing connection: " + e.getMessage(), e);
        }
    }
}