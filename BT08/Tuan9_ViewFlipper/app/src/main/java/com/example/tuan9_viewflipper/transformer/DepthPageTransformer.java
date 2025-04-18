package com.example.tuan9_viewflipper.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class DepthPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1]
            // Trang này đã bị kéo sang trái ngoài màn hình
            view.setAlpha(0f);

        } else if (position <= 0) { // [-1,0]
            // Sử dụng hiệu ứng mặc định khi kéo sang trái
            view.setAlpha(1f);
            view.setTranslationX(0f);
            view.setTranslationZ(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);

        } else if (position <= 1) { // (0,1]
            // Làm mờ dần trang khi chuyển đổi
            view.setAlpha(1 - position);
            // Dịch chuyển trang theo chiều ngang
            view.setTranslationX(pageWidth * -position);
            // Đặt phía sau trang bên trái
            view.setTranslationZ(-1f);
            // Thu nhỏ trang trong khoảng từ MIN_SCALE đến 1
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // Trang này đã bị kéo sang phải ngoài màn hình
            view.setAlpha(0f);
        }
    }
}
