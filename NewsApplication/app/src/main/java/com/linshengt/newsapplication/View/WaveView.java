package com.linshengt.newsapplication.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.linshengt.newsapplication.R;

/**
 * Created by linshengt on 2016/10/30.
 */
public class WaveView extends View {

    private int width, heitht;
    private float swing, bewidth;
    private Paint mPaint = new Paint();
    private float offset;

    private void initData(){
        swing = 50;
        offset = 0;
    }
    private void initPaint(){
        mPaint.setColor(getResources().getColor(R.color.colorWave));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setAntiAlias(true);
    }
    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initPaint();

        startAnimation();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        heitht = h;
    }

    private void drawBeizer(Canvas canvas){

        Path path = new Path();

        path.moveTo(-2*bewidth+offset, heitht/2);


        path.quadTo(-bewidth*3/2+offset,   heitht/2+swing,  -bewidth  +offset,   heitht/2);
        path.quadTo(-bewidth/2  +offset,   heitht/2-swing,              offset,   heitht/2);

        path.quadTo( bewidth/2  +offset,   heitht/2+swing,   bewidth  +offset,   heitht/2);
        path.quadTo (bewidth*3/2+offset,   heitht/2-swing,   bewidth*2+offset,   heitht/2);


        path.lineTo(bewidth*2+offset, heitht);
        path.lineTo(-2*bewidth+offset, heitht);
        canvas.drawPath(path, mPaint);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bewidth = width/2;
        drawBeizer(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), 300);
    }

    private void startAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                offset = (float) animation.getAnimatedValue()*width;
                invalidate();
            }
        });
        valueAnimator.start();
    }
}
