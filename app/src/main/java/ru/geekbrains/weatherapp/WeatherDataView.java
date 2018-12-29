package ru.geekbrains.weatherapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WeatherDataView extends View {

    private final static String TAG = "WeatherDataView";
    private final static int STROKE_WIDTH = 5;
    private Paint paint;

    //TODO create custom view of weather data
    //
    public WeatherDataView(Context context) {
        super(context);
        init();
    }

    // Вызывается при вставке элемента в макет
    public WeatherDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Вызывается при вставке элемента в макет, если был добавлен стиль
    public WeatherDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Log.d(TAG, "Constructor");
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
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

        int[] location = new int[2];
        getLocationOnScreen(location);

        canvas.drawCircle(location[0], location[1], 200, paint);
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