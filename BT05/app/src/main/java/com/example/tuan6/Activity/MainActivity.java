package com.example.tuan6.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tuan6.DatabaseHandler;
import com.example.tuan6.Model.NotesModel;
import com.example.tuan6.NotesAdapter;
import com.example.tuan6.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    ListView listView;
    ArrayList<NotesModel> arrayList;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listView1);
        arrayList = new ArrayList<>();
        adapter = new NotesAdapter(this, R.layout.row_item, arrayList);
        listView.setAdapter(adapter);

        databaseHandler = new DatabaseHandler(this, "notes.sqlite", null, 1);

        Button btnResetDB = findViewById(R.id.btnResetDB);
        btnResetDB.setOnClickListener(view -> {
            resetDatabase();
        });

        InitDatabaseSQLite();
        if (!isDatabasePopulated()) {
            createDatabaseSQLite();
        }

        databaseSQLite();
    }


    private void createDatabaseSQLite() {
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'SQLite 1')");
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'SQLite 2')");
        databaseHandler.QueryData("INSERT INTO Notes VALUES(null, 'SQLite 3')");
    }


    private void InitDatabaseSQLite() {
        databaseHandler = new DatabaseHandler(this, "notes.sqlite", null, 1);
        databaseHandler.QueryData("CREATE TABLE IF NOT EXISTS Notes(Id INTEGER PRIMARY KEY AUTOINCREMENT, NameNotes VARCHAR(200))");
    }


    private void databaseSQLite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrayList.clear();
                Cursor cursor = databaseHandler.GetData("SELECT * FROM Notes");
                while (cursor.moveToNext()) {
                    String name = cursor.getString(1);
                    int id = cursor.getInt(0);
                    arrayList.add(new NotesModel(id, name));
                }
                cursor.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAddNotes) {
            Toast.makeText(this, "Thêm Notes được chọn!", Toast.LENGTH_SHORT).show();
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }


    private void DialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_notes);

        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonAdd = dialog.findViewById(R.id.buttonThem);
        Button buttonCancel = dialog.findViewById(R.id.buttonHuy);

        buttonAdd.setOnClickListener(v -> {
            String name = editText.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên Notes", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> {
                    databaseHandler.QueryData("INSERT INTO Notes VALUES(null, '" + name + "')");

                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Đã thêm Notes", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        databaseSQLite();
                    });
                }).start();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // Hàm dialog cập nhật Notes
    public void DialogCapNhatNotes(String name, int id) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_notes);

        EditText editText = dialog.findViewById(R.id.editTextName);
        Button buttonEdit = dialog.findViewById(R.id.buttonEdit);
        Button buttonHuy = dialog.findViewById(R.id.buttonHuy);

        editText.setText(name);

        buttonEdit.setOnClickListener(v -> {
            String newName = editText.getText().toString().trim();
            if (!newName.isEmpty()) {
                new Thread(() -> {
                    databaseHandler.QueryData("UPDATE Notes SET NameNotes ='" + newName + "' WHERE Id = " + id);

                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Đã cập nhật Notes thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        databaseSQLite();
                    });
                }).start();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên Notes", Toast.LENGTH_SHORT).show();
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // Hàm dialog xóa
    public void DialogXoaNotes(String name, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn xóa Notes " + name + " này không ?");

        builder.setPositiveButton("Có", (dialog, which) -> {
            new Thread(() -> {
                databaseHandler.QueryData("DELETE FROM Notes WHERE Id = '" + id + "'");

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Đã xóa Notes " + name + " thành công", Toast.LENGTH_SHORT).show();
                    databaseSQLite();
                });
            }).start();
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    // Xử lý khi liên tục thao tác sẽ tạo db
    private boolean isDatabasePopulated() {
        Cursor cursor = databaseHandler.GetData("SELECT COUNT(*) FROM Notes");
        boolean hasData = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                hasData = count > 0;
            }
            cursor.close();
        }
        return hasData;
    }


    private void resetDatabase() {
        this.deleteDatabase("notes.sqlite");

        InitDatabaseSQLite();
        createDatabaseSQLite();

        arrayList.clear();
        databaseSQLite();

        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã tạo lại database!", Toast.LENGTH_SHORT).show();
    }

}