package com.example.bttuan10_socket.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class SocketServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isRunning = false;
    private static final int port = 12345;

    public void startServer() {
        try {
            // Bind to all network interfaces
            serverSocket = new ServerSocket(port, 50);
            isRunning = true;
            Log.d("SocketServer", "Server started on port: " + port);
            Log.d("SocketServer", "Server IP addresses: " + getLocalIpAddress());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    waitForConnection();
                }
            }).start();
        } catch (IOException e) {
            Log.e("SocketServer", "Error starting server: " + e.getMessage(), e);
        }
    }

    private String getLocalIpAddress() {
        StringBuilder addresses = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(':') < 0) {
                        addresses.append(address.getHostAddress()).append(" on ").append(networkInterface.getDisplayName()).append(", ");
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("SocketServer", "Error getting IP address: " + e.getMessage(), e);
        }
        return addresses.toString();
    }

    private void waitForConnection() {
        try {
            while (isRunning) {
                Log.d("SocketServer", "Waiting for connection...");
                clientSocket = serverSocket.accept();
                Log.d("SocketServer", "Client connected: " + clientSocket.getInetAddress());

                // Khởi tạo input và output streams
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Send welcome message
                sendMessage("Welcome to the server!");

                // Xử lý client kết nối
                handleClient();
            }
        } catch (IOException e) {
            if (isRunning) {
                Log.e("SocketServer", "Error accepting connection: " + e.getMessage(), e);
            } else {
                Log.d("SocketServer", "Server stopped");
            }
        }
    }

    private void handleClient() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null && isRunning) {
                Log.d("SocketServer", "Message received: " + inputLine);

                String response = processMessage(inputLine);

                sendMessage(response);
            }
        } catch (IOException e) {
            Log.e("SocketServer", "Error handling client: " + e.getMessage(), e);
        } finally {
            closeClientConnection();
        }
    }

    private void closeClientConnection() {
        try {
            if (clientSocket != null) {
                clientSocket.close();
                clientSocket = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            Log.d("SocketServer", "Client connection closed");
        } catch (IOException e) {
            Log.e("SocketServer", "Error closing resources: " + e.getMessage(), e);
        }
    }

    private String processMessage(String message) {
        // Trả về một thông điệp với nội dung đã nhận
        return "Server received: " + message;
    }

    public void sendMessage(String message) {
        if (clientSocket != null && clientSocket.isConnected() && !clientSocket.isClosed() && out != null) {
            out.println(message);
            if (out.checkError()) {
                Log.e("SocketServer", "Error occurred while sending message");
                closeClientConnection();
            } else {
                Log.d("SocketServer", "Message sent: " + message);
            }
        } else {
            Log.e("SocketServer", "Unable to send message. Socket not connected or output stream is null.");
        }
    }

    public void stopServer() {
        isRunning = false;
        closeClientConnection();
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
                Log.d("SocketServer", "Server stopped");
            }
        } catch (IOException e) {
            Log.e("SocketServer", "Error stopping server: " + e.getMessage(), e);
        }
    }
}