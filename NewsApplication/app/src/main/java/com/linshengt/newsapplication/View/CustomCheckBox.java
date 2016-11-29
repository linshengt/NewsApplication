package com.linshengt.newsapplication.View;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linshengt.newsapplication.Utils.HLog;


/**
 * Created by linshengt on 2016/10/12.
 */
public class CustomCheckBox extends View {

    private String TAG = "CustomCheckBox";
    private Paint linePaint, circlePaint;
    private Paint textPaintLeft, textPaintRight, textPaintStrStandard;
    private int width;
    private int lenTotal, start, end, third, two_third;
    private int Y;
    public  float RADIUS;
    private Point currentPoint;
    private CircleParam cParaStart, cParaThird, cPareTwoThird, cParaEnd;

    private CustuomCheckBoxStatus mCustuomCheckBoxStatus = CustuomCheckBoxStatus.START;
    public enum CustuomCheckBoxStatus{
        START,
        THIRD,
        TWO_THIRD,
        END
    }
    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        intiPara();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float offsetLeft, offsetRight, offsetStrStandard;

        canvas.drawLine(start, Y, end, Y, linePaint);
        canvas.drawLine(start, Y-10, start, Y+10, linePaint);
        canvas.drawLine(third, Y-10, third, Y+10, linePaint);
        canvas.drawLine(two_third, Y-10, two_third, Y+10, linePaint);
        canvas.drawLine(end, Y-10, end, Y+10, linePaint);

        offsetLeft = textPaintLeft.measureText("A") / 2;
        offsetRight = textPaintRight.measureText("A") / 2;
        offsetStrStandard = textPaintStrStandard.measureText("标准") / 2;

        canvas.drawText("A", start-offsetLeft, Y-60, textPaintLeft);
        canvas.drawText("A", end-offsetRight, Y-60, textPaintRight);
        canvas.drawText("标准", third-offsetStrStandard, Y-60, textPaintStrStandard);

        if(currentPoint == null) {
            HLog.i(TAG, "currentPoint == null");
            currentPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            drawCircle(canvas);
        }else {
            HLog.i(TAG, "currentPoint -- else");
            drawCircle(canvas);
        }

    }



    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, circlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(600, 300);


    }

    private float lastPointX,lastPointY;
    private boolean isMove;
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastPointX = event.getX();
                lastPointY = event.getY();
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                lastPointX = event.getX();
                lastPointY = event.getY();
                Animation();
                break;
            case MotionEvent.ACTION_UP:
                if(isMove == false){
                    Animation();
                }
                break;
        }

        return true;
    }


    private void Animation(){
        if(Math.pow((lastPointX - cParaStart.getCenterX()), 2) + Math.pow((lastPointY - cParaStart.getCenterY()), 2) < Math.pow(RADIUS+10, 2)){
            Point startPoint = new Point(currentPoint.getX(), currentPoint.getY());
            Point endPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            startAnimation(startPoint, endPoint, 0);
            isMove = true;
            mCustuomCheckBoxStatus = CustuomCheckBoxStatus.START;
            onClickListener.setOnclickListener(mCustuomCheckBoxStatus);
        }else if(Math.pow((lastPointX - cParaThird.getCenterX()), 2) + Math.pow((lastPointY - cParaThird.getCenterY()), 2) < Math.pow(RADIUS+10, 2)){
            Point startPoint = new Point(currentPoint.getX(), currentPoint.getY());
            Point endPoint = new Point(cParaThird.getCenterX(), cParaThird.getCenterY());
            startAnimation(startPoint, endPoint, 0);
            isMove = true;
            mCustuomCheckBoxStatus = CustuomCheckBoxStatus.THIRD;
            onClickListener.setOnclickListener(mCustuomCheckBoxStatus);
        }else if(Math.pow((lastPointX - cPareTwoThird.getCenterX()), 2) + Math.pow((lastPointY - cPareTwoThird.getCenterY()), 2) < Math.pow(RADIUS+10, 2)){
            Point startPoint = new Point(currentPoint.getX(), currentPoint.getY());
            Point endPoint = new Point(cPareTwoThird.getCenterX(), cPareTwoThird.getCenterY());
            startAnimation(startPoint, endPoint, 0);
            isMove = true;
            mCustuomCheckBoxStatus = CustuomCheckBoxStatus.TWO_THIRD;
            onClickListener.setOnclickListener(mCustuomCheckBoxStatus);
        }else if(Math.pow((lastPointX - cParaEnd.getCenterX()), 2) + Math.pow((lastPointY - cParaEnd.getCenterY()), 2) < Math.pow(RADIUS+10, 2)){
            Point startPoint = new Point(currentPoint.getX(), currentPoint.getY());
            Point endPoint = new Point(cParaEnd.getCenterX(), cParaEnd.getCenterY());
            startAnimation(startPoint, endPoint, 0);
            isMove = true;
            mCustuomCheckBoxStatus = CustuomCheckBoxStatus.END;
            onClickListener.setOnclickListener(mCustuomCheckBoxStatus);
        }
    }
    public void setmCustuomCheckBoxStatus(CustuomCheckBoxStatus status){
        mCustuomCheckBoxStatus = status;
        if(mCustuomCheckBoxStatus == CustuomCheckBoxStatus.START){
            Point startPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            Point endPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            startAnimation(startPoint, endPoint, 0);
        }else if(mCustuomCheckBoxStatus == CustuomCheckBoxStatus.THIRD){
            Point startPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            Point endPoint = new Point(cParaThird.getCenterX(), cParaThird.getCenterY());
            startAnimation(startPoint, endPoint, 0);
        }else if(mCustuomCheckBoxStatus == CustuomCheckBoxStatus.TWO_THIRD){
            Point startPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            Point endPoint = new Point(cPareTwoThird.getCenterX(), cPareTwoThird.getCenterY());
            startAnimation(startPoint, endPoint, 0);
        }else if(mCustuomCheckBoxStatus == CustuomCheckBoxStatus.END){
            Point startPoint = new Point(cParaStart.getCenterX(), cParaStart.getCenterY());
            Point endPoint = new Point(cParaEnd.getCenterX(), cParaEnd.getCenterY());
            startAnimation(startPoint, endPoint, 0);
        }
    }

    public void setOnClickListener(CustomCheckBox.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private onClickListener onClickListener = null;
    public interface onClickListener{
        public void setOnclickListener(CustuomCheckBoxStatus Status);
    }

    private void startAnimation(Point startPoint, Point endPoint, int time){
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.setDuration(time);
        anim.start();
    }
    private void intiPara(){
        width = 600;
        lenTotal = width - 200;
        start = 100;
        third = start + lenTotal / 3;
        two_third = start + lenTotal * 2 / 3;
        end = lenTotal + start;
        RADIUS = 30;
        Y = 200;

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(Color.LTGRAY);
        circlePaint.setAntiAlias(true);

        textPaintLeft = new Paint();
        textPaintLeft.setColor(Color.BLACK);
        textPaintLeft.setAntiAlias(true);
        textPaintLeft.setTextSize(30);
        textPaintLeft.setStrokeWidth(20);


        textPaintRight = new Paint();
        textPaintRight.setColor(Color.BLACK);
        textPaintRight.setAntiAlias(true);
        textPaintRight.setTextSize(50);
        textPaintRight.setStrokeWidth(20);

        textPaintStrStandard = new Paint();
        textPaintStrStandard.setColor(Color.BLACK);
        textPaintStrStandard.setAntiAlias(true);
        textPaintStrStandard.setTextSize(30);
        textPaintStrStandard.setStrokeWidth(20);

        cParaStart = new CircleParam(start, Y, RADIUS);
        cParaThird = new CircleParam(third, Y, RADIUS);
        cPareTwoThird = new CircleParam(two_third, Y, RADIUS);
        cParaEnd = new CircleParam(end, Y, RADIUS);
    }
    public class Point {
        private float x;
        private float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
    }
    public class PointEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
            Point point = new Point(x, y);
            return point;
        }
    }
    private class CircleParam {
        private float centerX, centerY, radius;

        public CircleParam(float centerX, float centerY, float radius) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }

        public float getCenterX() {
            return centerX;
        }

        public float getCenterY() {
            return centerY;
        }

        public float getRadius() {
            return radius;
        }

    }
}
