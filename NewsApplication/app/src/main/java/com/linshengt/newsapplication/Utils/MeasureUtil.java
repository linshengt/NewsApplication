package com.linshengt.newsapplication.Utils;

import android.content.Context;

/**
 * 测量工具类
 *
 * Util of measure.
 *
 * @author AigeStudio 2015-03-26
 */
public final class MeasureUtil {
    public static int dp2px(Context context, float dp) {              //密度转像素
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {             //像素转密度
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
