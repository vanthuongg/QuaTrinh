package com.example.tuan9_recyclerview;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;

public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {
    private final int colorActive = 0xDEFFFFFF;
    private final int colorInactive = 0x66FFFFFF;

    private final float DP = Resources.getSystem().getDisplayMetrics().density;
    private final int indicatorHeight = (int) (16 * DP);
    private final float indicatorStrokeWidth = 2 * DP;
    private final float indicatorItemLength = 8 * DP;
    private final float indicatorItemPadding = 4 * DP;

    private final Paint paint = new Paint();

    public LinePagerIndicatorDecoration() {
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(indicatorStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int itemCount = parent.getAdapter().getItemCount();

        float totalLength = indicatorItemLength * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * indicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2f;
        float indicatorPosY = parent.getHeight() - indicatorHeight;

        drawInactiveIndicators(canvas, indicatorStartX, indicatorPosY, itemCount);
        drawActiveIndicator(canvas, indicatorStartX, indicatorPosY, parent);
    }

    private void drawInactiveIndicators(Canvas canvas, float startX, float posY, int itemCount) {
        paint.setColor(colorInactive);
        float itemWidth = indicatorItemLength + indicatorItemPadding;
        float x = startX;
        for (int i = 0; i < itemCount; i++) {
            canvas.drawLine(x, posY, x + indicatorItemLength, posY, paint);
            x += itemWidth;
        }
    }

    private void drawActiveIndicator(Canvas canvas, float startX, float posY, RecyclerView parent) {
        paint.setColor(colorActive);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof androidx.recyclerview.widget.LinearLayoutManager)) {
            return;
        }

        int activePosition = ((androidx.recyclerview.widget.LinearLayoutManager) layoutManager)
                .findFirstVisibleItemPosition();

        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        float itemWidth = indicatorItemLength + indicatorItemPadding;
        float highlightStart = startX + itemWidth * activePosition;
        canvas.drawLine(highlightStart, posY, highlightStart + indicatorItemLength, posY, paint);
    }
}
