package com.example.bttuan10_socket;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bttuan10_socket.server.SocketClient;
import com.example.bttuan10_socket.server.SocketServer;

public class SocketActivity extends AppCompatActivity {
    private SocketClient socketClient;
    private SocketServer socketServer;

    private EditText messageInput;
    private Button sendButton;
    private TextView responseText;
    private TextView statusText;
    private Button connectButton;
    private Button disconnectButton;

    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        responseText = findViewById(R.id.response_text);
        statusText = findViewById(R.id.status_text);
        connectButton = findViewById(R.id.connect_button);
        disconnectButton = findViewById(R.id.disconnect_button);

        socketClient = new SocketClient();
        socketServer = new SocketServer();

        socketServer.startServer();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!socketClient.isConnected()) {
                    connectToServer();
                } else {
                    Toast.makeText(SocketActivity.this, "Đã kết nối rồi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socketClient.isConnected()) {
                    disconnectFromServer();
                } else {
                    Toast.makeText(SocketActivity.this, "Chưa kết nối!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socketClient.isConnected()) {
                    String message = messageInput.getText().toString();
                    if (!TextUtils.isEmpty(message)) {
                        sendMessage(message);
                        messageInput.setText("");
                    }
                } else {
                    Toast.makeText(SocketActivity.this, "Vui lòng kết nối tới Server trước!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void connectToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketClient.connectToServer(SERVER_HOST, SERVER_PORT);
                if (socketClient.isConnected()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusText.setText("Connected");
                            statusText.setTextColor(Color.GREEN);
                            Toast.makeText(SocketActivity.this, "Kết nối thành công!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startReceivingMessages();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusText.setText("Disconnected");
                            statusText.setTextColor(Color.RED);
                            Toast.makeText(SocketActivity.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void startReceivingMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socketClient.isConnected()) {
                    String message = socketClient.receiveMessage();
                    if (message != null) {
                        final String receivedMessage = message;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                responseText.append("Server: " + receivedMessage + "\n");
                            }
                        });
                    } else {
                        // If message is null, the connection may be closed
                        if (!socketClient.isConnected()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    statusText.setText("Disconnected");
                                    statusText.setTextColor(Color.RED);
                                    Toast.makeText(SocketActivity.this, "Mất kết nối tới Server", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }).start();
    }

    private void sendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = socketClient.sendMessage(message);
                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseText.append("You: " + message + "\n");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SocketActivity.this, "Không thể gửi tin nhắn", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void disconnectFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketClient.closeConnection();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        statusText.setText("Disconnected");
                        statusText.setTextColor(Color.RED);
                        Toast.makeText(SocketActivity.this, "Đã ngắt kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socketClient.isConnected()) {
            socketClient.closeConnection();
        }
        socketServer.stopServer();
    }
}