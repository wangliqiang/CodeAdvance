package com.app.codeadvance.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class GradientText extends AppCompatTextView {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mProgress = 0f;

    public GradientText(@NonNull Context context) {
        super(context);
    }

    public GradientText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawCenterLineX(canvas);
//        drawCenterLineY(canvas);
        drawTextBlack(canvas);
        drawTextRed(canvas);

    }

    private void drawTextBlack(Canvas canvas) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        float width = paint.measureText("hello world!");
        float baseLine = (float) getHeight() / 2 + ((fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
        float x = (float) getWidth() / 2 - width / 2;

        canvas.drawText("hello world!", x, baseLine, paint);
    }

    private void drawTextRed(Canvas canvas) {
        canvas.save();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        paint.setColor(Color.RED);
        paint.setTextSize(60);
        float width = paint.measureText("hello world!");
        float baseLine = (float) getHeight() / 2 + ((fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
        float x = (float) getWidth() / 2 - width / 2;

        float right = x + width * mProgress;
        Rect rect = new Rect((int) x, 0, (int) right, getHeight());
        canvas.clipRect(rect);
        canvas.drawText("hello world!", x, baseLine, paint);
        canvas.restore();
    }


    private void drawCenterLineX(Canvas canvas) {
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine((float) getWidth() / 2, 0, (float) getWidth() / 2, getHeight(), paint);
    }

    private void drawCenterLineY(Canvas canvas) {
        canvas.save();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawLine(0, (float) getHeight() / 2, getWidth(), (float) getHeight() / 2, paint);
    }
}
