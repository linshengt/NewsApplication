package com.linshengt.newsapplication.View.header;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linshengt.newsapplication.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


/**
 * author zhetengxiang
 * Date 2016/7/27
 * step0:下拉/准备下拉状态
 * step1:下拉->可刷新状态
 * step2:刷新状态
 * step3:刷新结束状态
 * step4:可刷新->下拉状态
 */

public class PtrMeiTuanHeader extends LinearLayout implements PtrUIHandler {
    private static final String TAG = "PtrMeiTuanHeader";

    ImageView ivFirst;

    ImageView ivSecond;

    ImageView ivThird;

    TextView tvMsg;
    private Matrix mMatrix = new Matrix();
 //   private AnimationDrawable mSecondAnimation;
    private AnimationDrawable mThirdAnimation;


    public PtrMeiTuanHeader(Context context) {
        super(context);
        View headerView = LayoutInflater.from(context).inflate(R.layout.header_meituan, this);
        init();
    }

    private void init() {
        ivFirst = (ImageView) findViewById(R.id.ivFirst);
        ivSecond = (ImageView) findViewById(R.id.ivSecond);
        ivThird = (ImageView) findViewById(R.id.ivThird);
        tvMsg = (TextView) findViewById(R.id.tvMsg);

    //    mSecondAnimation = (AnimationDrawable) ivSecond.getDrawable();
        mThirdAnimation = (AnimationDrawable) ivThird.getDrawable();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        Log.i(TAG, "onUIReset: ");
        resetView();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshPrepare: ");
        pullStep0(0.0f);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshBegin: ");
        pullStep2();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Log.i(TAG, "onUIRefreshComplete: ");
        pullStep3();
    }

    /**
     * 下拉的时候
     * 07-28 10:25:16.810 31949-31949/harry.com.pullrefresh I/PtrMeiTuanHeader: mOffsetToRefresh =176,currentPos
     * =303,lastPos =302
     * 07-28 10:25:16.820 31949-31949/harry.com.pullrefresh I/PtrMeiTuanHeader: mOffsetToRefresh =176,currentPos
     * =304,lastPos =303
     * 07-28 10:25:16.840 31949-31949/harry.com.pullrefresh I/PtrMeiTuanHeader: mOffsetToRefresh =176,currentPos
     * =305,lastPos =304
     * 07-28 10:25:16.870 31949-31949/harry.com.pullrefresh I/PtrMeiTuanHeader: mOffsetToRefresh =176,currentPos
     * =306,lastPos =305
     */
    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

   //     Log.i(TAG, "isUnderTouch=" + isUnderTouch +",status=" + status);
   //     Log.i(TAG, "mOffsetToRefresh =" + mOffsetToRefresh + ",currentPos =" + currentPos + ",lastPos =" + lastPos);
        if (lastPos < mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                float scale = lastPos / Float.valueOf(mOffsetToRefresh);
                pullStep0(scale);
            }
        }


        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                pullStep4();
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                pullStep1(frame);
            }
        }
    }

    private void pullStep0(float scale) {
        ivFirst.setVisibility(View.VISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.INVISIBLE);
        scaleImage(scale);
        tvMsg.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
    }

    private void pullStep1(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            ivFirst.setVisibility(View.INVISIBLE);
            ivSecond.setVisibility(View.VISIBLE);
            ivThird.setVisibility(View.INVISIBLE);
           // mSecondAnimation.start();
            tvMsg.setText(getResources().getString(R.string.cube_ptr_release_to_refresh));
        }
    }

    private void pullStep2() {
        ivFirst.setVisibility(View.INVISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.VISIBLE);
        cancelAnimationSecond();
        mThirdAnimation.start();
        tvMsg.setText(R.string.cube_ptr_refreshing);
    }

    private void pullStep3() {
        ivFirst.setVisibility(View.INVISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.VISIBLE);
        cancelAnimationThird();
        tvMsg.setText(getResources().getString(R.string.cube_ptr_refresh_complete));
    }

    /**
     * 可刷新到不可刷新
     */
    private void pullStep4() {
        ivFirst.setVisibility(View.VISIBLE);
        ivSecond.setVisibility(View.INVISIBLE);
        ivThird.setVisibility(View.INVISIBLE);
        tvMsg.setText(getResources().getString(R.string.cube_ptr_pull_down_to_refresh));
    }

    private void scaleImage(float scale) {
        mMatrix.setScale(scale, scale, ivFirst.getWidth() / 2, ivFirst.getHeight() / 2);
        ivFirst.setImageMatrix(mMatrix);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetView();
    }

    private void resetView() {
        cancelAnimations();
    }

    private void cancelAnimations() {
        cancelAnimationSecond();
        cancelAnimationThird();
    }

    private void cancelAnimationSecond() {
//        if (mSecondAnimation != null && mSecondAnimation.isRunning()) {
//            mSecondAnimation.stop();
//        }
    }

    private void cancelAnimationThird() {
        if (mThirdAnimation != null && mThirdAnimation.isRunning()) {
            mThirdAnimation.stop();
        }
    }
}
