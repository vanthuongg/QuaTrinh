 package com.example.tuan9_recyclerview.activty;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuan9_recyclerview.LinePagerIndicatorDecoration;
import com.example.tuan9_recyclerview.R;
import com.example.tuan9_recyclerview.adapter.IconAdapter;
import com.example.tuan9_recyclerview.model.IconModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcIcon;
    private IconAdapter iconAdapter;
    private List<IconModel> iconList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcIcon = findViewById(R.id.rcIcon);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        iconList = new ArrayList<>();
        iconList.add(new IconModel(R.drawable.flash_sale, "Flash Sale"));
        iconList.add(new IconModel(R.drawable.re_vo_dich, "Rẻ Vô Địch"));
        iconList.add(new IconModel(R.drawable.freeship, "Free Ship"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        rcIcon.setLayoutManager(gridLayoutManager);

        iconAdapter = new IconAdapter(getApplicationContext(), iconList);
        rcIcon.setAdapter(iconAdapter);
        rcIcon.addItemDecoration(new LinePagerIndicatorDecoration());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterListener(newText);
                return true;
            }
        });
    }

    private void filterListener(String text) {
        List<IconModel> list = new ArrayList<>();
        for (IconModel iconModel : iconList) {
            if (iconModel.getDesc().toLowerCase().contains(text.toLowerCase())) {
                list.add(iconModel);
            }
        }
        if (list.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            iconAdapter.setListenerList(list);
        }
    }
}
