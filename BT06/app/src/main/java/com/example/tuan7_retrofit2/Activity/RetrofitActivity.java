package com.example.tuan7_retrofit2.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuan7_retrofit2.Adapter.CategoryAdapter;
import com.example.tuan7_retrofit2.Interface.APIService;
import com.example.tuan7_retrofit2.Model.Category;
import com.example.tuan7_retrofit2.R;
import com.example.tuan7_retrofit2.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    RecyclerView rcCate;
    CategoryAdapter categoryAdapter;
    APIService apiService;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        GetCategory();

    }

    private void AnhXa(){
        rcCate = findViewById(R.id.rc_category);
    }

    private void GetCategory(){
        apiService = RetrofitClient.getClient().create(APIService.class);
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(RetrofitActivity.this, categoryList);
                    rcCate.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    rcCate.setLayoutManager(layoutManager);
                    rcCate.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                }
                else{
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, @NonNull Throwable t) {
                Log.d("Logg", t.getMessage());
            }
        });
    }
}
