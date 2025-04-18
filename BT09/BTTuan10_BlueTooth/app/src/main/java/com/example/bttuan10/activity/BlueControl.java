package com.example.bttuan10.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bttuan10.R;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BlueControl extends AppCompatActivity {
    ImageButton btnTb1, btnTb2, btnDis;
    TextView txt1, txtMAC;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Set<BluetoothDevice> pairedDevices;
    String address = null;
    private ProgressDialog progress;
    int flaglamp1 = 0;
    int flaglamp2 = 0;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_BLUETOOTH_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);

        btnTb1 = findViewById(R.id.btnTb1);
        btnTb2 = findViewById(R.id.btnTb2);
        txt1 = findViewById(R.id.textV1);
        txtMAC = findViewById(R.id.textViewMAC);
        btnDis = findViewById(R.id.btnDisc);

        // Kiểm tra quyền trước khi kết nối
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_PERMISSION
            );
        } else {
            new ConnectBT().execute();
        }

        btnTb1.setOnClickListener(v -> thietTbil());
        btnTb2.setOnClickListener(v -> thiettbi7());
        btnDis.setOnClickListener(v -> Disconnect());
    }

    private void thietTbil() {
        if (btSocket != null) {
            try {
                if (flaglamp1 == 0) {
                    flaglamp1 = 1;
                    btnTb1.setBackgroundResource(R.drawable.tbon);
                    btSocket.getOutputStream().write("1".getBytes());
                    txt1.setText("Thiết bị số 1 đang bật");
                } else {
                    flaglamp1 = 0;
                    btnTb1.setBackgroundResource(R.drawable.tboff);
                    btSocket.getOutputStream().write("A".getBytes());
                    txt1.setText("Thiết bị số 1 đang tắt");
                }
            } catch (IOException e) {
                msg("Lỗi bật tắt thiết bị 1");
            }
        }
    }

    private void thiettbi7() {
        if (btSocket != null) {
            try {
                if (flaglamp2 == 0) {
                    flaglamp2 = 1;
                    btnTb2.setBackgroundResource(R.drawable.tbon);
                    btSocket.getOutputStream().write("7".getBytes());
                    txt1.setText("Thiết bị số 7 đang bật");
                } else {
                    flaglamp2 = 0;
                    btnTb2.setBackgroundResource(R.drawable.tboff);
                    btSocket.getOutputStream().write("G".getBytes());
                    txt1.setText("Thiết bị số 7 đang tắt");
                }
            } catch (IOException e) {
                msg("Lỗi bật tắt thiết bị 7");
            }
        }
    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msg("Lỗi ngắt kết nối");
            }
        }
        finish();
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(BlueControl.this, "Đang kết nối...", "Xin vui lòng đợi!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                            ActivityCompat.checkSelfPermission(BlueControl.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ConnectSuccess = false;
                        return null;
                    }

                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!ConnectSuccess) {
                msg("Kết nối thất bại! Kiểm tra thiết bị.");
                finish();
            } else {
                msg("Kết nối thành công.");
                isBtConnected = true;
                pairedDevicesList();
            }
            progress.dismiss();
        }
    }

    private void pairedDevicesList() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    if (bt.getAddress().equals(address)) {
                        txtMAC.setText("Đã kết nối: " + bt.getName() + "\n" + bt.getAddress());
                        break;
                    }
                }
            } else {
                Toast.makeText(this, "Không tìm thấy thiết bị đã ghép đôi.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ConnectBT().execute();
            } else {
                Toast.makeText(this, "Từ chối quyền Bluetooth. Không thể kết nối.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
