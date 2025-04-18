package com.example.tuan9_viewflipper.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuan9_viewflipper.R;
import com.example.tuan9_viewflipper.adapter.SliderViewAdapter;
import com.library.foysaltech.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.library.foysaltech.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class SlideViewActivity extends AppCompatActivity {
    private SliderView sliderView;
    private ArrayList<Integer> arrayList;
    private SliderViewAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_view);

        sliderView = findViewById(R.id.imageSlide);
        arrayList = new ArrayList<>();
        arrayList.add(R.drawable.coffee);
        arrayList.add(R.drawable.congtypizza);
        arrayList.add(R.drawable.quangcao);
        arrayList.add(R.drawable.themoingon);

        sliderAdapter = new SliderViewAdapter(getApplicationContext(), arrayList);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.red));
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        sliderView.setScrollTimeInSec(3);
    }

}
