package com.prolificinteractive.materialcalendarview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;

public class LineSpan implements LineBackgroundSpan {

    private int color;

    LineSpan(int color) {
        this.color = color;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence charSequence, int start, int end, int lineNum) {
        Rect rect = new Rect();
        rect.set(left, top - (top - bottom), right, bottom - (top - bottom));
        paint.setColor(color);
        canvas.drawRect(rect, paint);
    }
}
