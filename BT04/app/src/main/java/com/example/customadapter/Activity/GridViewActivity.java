package com.example.customadapter.Activity;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customadapter.R;
import com.example.customadapter.adapter.MonHocAdapter;
import com.example.customadapter.models.MonHoc;

import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {
    GridView gridView;
    ArrayList<MonHoc> arrayList;
    MonHocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        AnhXa();

        adapter = new MonHocAdapter(this, R.layout.row_monhoc, arrayList);
        gridView.setAdapter(adapter);
    }


    private void AnhXa(){

        gridView = findViewById(R.id.gridView);
        arrayList = new ArrayList<>();
        arrayList.add(new MonHoc("Java", "Java 1", R.drawable.java));
        arrayList.add(new MonHoc("C#", "C# 1", R.drawable.c));
        arrayList.add(new MonHoc("PHP", "PHP 1", R.drawable.php));
        arrayList.add(new MonHoc("Kotlin", "Kotlin 1", R.drawable.kotlin));
    }
}
