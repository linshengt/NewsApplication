package com.linshengt.newsapplication.Utils;

/**
 * Created by linshengt on 2016/8/9.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {

            return false;

        }

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        return (info != null) && info.isAvailable();

    }

}

