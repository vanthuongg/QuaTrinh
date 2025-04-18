package com.example.tuan9_viewflipper.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1]
            // Trang này đã bị kéo sang trái ngoài màn hình
            view.setAlpha(0f);

        } else if (position <= 1) { // [-1,1]
            // Giảm kích thước của trang khi chuyển đổi
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale trang lại trong khoảng từ MIN_SCALE đến 1
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Làm mờ trang tùy theo kích thước
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        } else { // (1,+Infinity]
            // Trang này đã bị kéo sang phải ngoài màn hình
            view.setAlpha(0f);
        }
    }
}

