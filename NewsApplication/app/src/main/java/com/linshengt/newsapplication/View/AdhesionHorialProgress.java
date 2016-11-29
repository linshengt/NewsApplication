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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linshengt on 2016/10/24.
 */
public class AdhesionHorialProgress extends View {

    private String TAG = "AdhesionHorialProgress";
    private Paint mPaintCircle;
    private float mdynamicCircleRadius;   //动态圆半径
    private int mCountCircle;               //静态圆个数
    private float mStaticCircleRadius;    //静态圆半径
    private float mIntervalCircle;         //静态圆间隔
    private int mAnimationTime;          //动画时间
    private float mCoefficient;            //控件大小倍数
    private float mWidth, mHeight;
    private float mMaxStaticCircleRadiusScaleRate = 0.4f;//静态圆变化半径的最大比率
    private float mMaxAdherentLength;//最大粘连长度
    private Circle mStaticCircle;
    private Circle  mDynamicCircle = new Circle();
    private List<Circle> mCircles = new ArrayList<>();

    public AdhesionHorialProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        reset();
        startAnimation();
    }

    private void reset(){
        mPaintCircle = new Paint();

        mPaintCircle.setColor(Color.argb(255, 0, 0, 255));
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.FILL);

        mCoefficient = 1;
        mdynamicCircleRadius = 8;
        mStaticCircleRadius = 10;
        mCountCircle = 5;
        mIntervalCircle = mStaticCircleRadius*3;
        mAnimationTime = 1000;
        mMaxAdherentLength = 3.5f * mStaticCircleRadius;

        mWidth = mIntervalCircle * (mCountCircle+1) + mStaticCircleRadius * mCountCircle *2;
        mHeight = mStaticCircleRadius * 2 * (1 + mMaxStaticCircleRadiusScaleRate);

        for(int i = 0; i < mCountCircle; i ++){
            mStaticCircle = new Circle();
            mStaticCircle.r = mStaticCircleRadius;
            mStaticCircle.x = (2*i+1)*mStaticCircleRadius + (i+1)*mIntervalCircle;
            mStaticCircle.y = mHeight / 2;
            mCircles.add(mStaticCircle);
        }

        mDynamicCircle = new Circle();
        mDynamicCircle.x = mdynamicCircleRadius;
        mDynamicCircle.y = mHeight / 2;
        mDynamicCircle.r = mdynamicCircleRadius;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        canvas.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < mCircles.size(); i ++){
            mStaticCircle = mCircles.get(i);


            if(isDoAdhesion()){
                canvas.drawCircle(mStaticCircle.x, mStaticCircle.y, mStaticCircleRadius, mPaintCircle);
                Path path = Adhesion.drawAdhesionBody(mStaticCircle.x, mStaticCircle.y,
                        mStaticCircleRadius, 45, mDynamicCircle.x, mDynamicCircle.y, mDynamicCircle.r, 45);
                canvas.drawPath(path, mPaintCircle);
            }else {
                canvas.drawCircle(mStaticCircle.x, mStaticCircle.y, mStaticCircle.r, mPaintCircle);
            }
        }

        canvas.drawCircle(mDynamicCircle.x, mDynamicCircle.y, mDynamicCircle.r, mPaintCircle);
    }

    private boolean isDoAdhesion(){

            /* 半径变化 */
        float distance = (float) Math.sqrt(Math.pow(mDynamicCircle.x - mStaticCircle.x, 2) + Math.pow(mDynamicCircle.y - mStaticCircle.y, 2));

        float scale = mMaxStaticCircleRadiusScaleRate - mMaxStaticCircleRadiusScaleRate * (distance / mMaxAdherentLength);
        mStaticCircleRadius = mStaticCircle.r * (1 + scale);
        if(distance < mMaxAdherentLength){
            return true;
        }else{
            return false;
        }
    }
    private void startAnimation(){
        ValueAnimator xValueAnimator = ValueAnimator.ofFloat(mDynamicCircle.x, mWidth - mdynamicCircleRadius);
        xValueAnimator.setRepeatCount(Animation.INFINITE);
        xValueAnimator.setRepeatMode(Animation.REVERSE);

        xValueAnimator.setDuration(mAnimationTime);

        xValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDynamicCircle.x = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        xValueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) mWidth, resolveSizeAndState((int) mHeight, heightMeasureSpec, MeasureSpec.UNSPECIFIED));
    }


    private class Circle{
        private float x;

        private float y;
        private float r;
    }
}
