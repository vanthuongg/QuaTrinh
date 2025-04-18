package com.example.customadapter.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customadapter.R;
import com.example.customadapter.adapter.RecycleViewAdapter;
import com.example.customadapter.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewActivity extends AppCompatActivity {
    //private static final String TAG = "activity_recycleview";
    private RecyclerView rvMultipleViewType;
    private List<Object> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        rvMultipleViewType = (RecyclerView) findViewById(R.id.recycleview);

        mData = new ArrayList<>();
        mData.add(new UserModel("C", "Quan 11"));
        mData.add("Text 1");
        mData.add(R.drawable.c);

        mData.add(new UserModel("PHP", "Quan 3"));
        mData.add("Text 2");
        mData.add(R.drawable.php);

        mData.add(new UserModel("JAVA", "Quan 10"));
        mData.add("Text 3");
        mData.add(R.drawable.java);

        mData.add(new UserModel("KOTLIN", "Quan 1"));
        mData.add("Text 4");
        mData.add(R.drawable.kotlin);

        RecycleViewAdapter adapter = new RecycleViewAdapter(this, mData);
        rvMultipleViewType.setAdapter(adapter);
        rvMultipleViewType.setLayoutManager(new LinearLayoutManager(this));
    }
}
