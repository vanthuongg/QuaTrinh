package com.example.tuan9_viewflipper.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tuan9_viewflipper.R;
import com.example.tuan9_viewflipper.RetrofitClient;
import com.example.tuan9_viewflipper.adapter.ImageSliderAdapter;
import com.example.tuan9_viewflipper.config.APIService;
import com.example.tuan9_viewflipper.model.ImageSlider;
import com.example.tuan9_viewflipper.model.MessageModel;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ImageSliderViewPageActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private ImageSliderAdapter adapter;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        viewPager = findViewById(R.id.viewpage);
        indicator = findViewById(R.id.circleIndicator);

        apiService = RetrofitClient.getClient().create(APIService.class);

        loadImages();

    }

    private void loadImages() {
        apiService.loadImageSlider(0).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ImageSlider> images = response.body().getResult();
                    adapter = new ImageSliderAdapter(ImageSliderViewPageActivity.this, images);
                    viewPager.setAdapter(adapter);
                    indicator.setViewPager(viewPager);
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.e("API_ERROR", "Failed to load images", t);
            }
        });
    }
}
