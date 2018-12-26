package ru.geekbrains.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class WeatherDataView extends View {

    private final static String TAG = "WeatherDataView";
    private Paint paint;

    public WeatherDataView(Context context) {
        super(context);
        Log.d(TAG, "Constructor");
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.d(TAG, "layout");
        super.layout(l, t, r, b);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw");
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawCircle(200, 200, 200, paint);
    }

    @Override
    public void invalidate() {
        Log.d(TAG, "invalidate");
        super.invalidate();
    }

    @Override
    public void requestLayout(){
        Log.d(TAG, "requestLayout");
        super.requestLayout();
    }
}