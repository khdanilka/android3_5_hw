package ru.geekbrains.android3_5.model.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import ru.geekbrains.android3_5.App;

/**
 * Created by stanislav on 3/15/2018.
 */

public class NetworkStatus
{
    private static final String TAG = "NetworkStatus";

    public enum Status
    {
        WIFI,
        MOBILE,
        ETHERNET,
        OFFLINE
    }

    private static Status currentStatus = Status.OFFLINE;

    private static boolean isAirplane()
    {
        return Settings.Global.getInt(App.getInstance().getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static Status getStatus() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                currentStatus = Status.WIFI;
            }

            if(activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET)
            {
                currentStatus = Status.ETHERNET;
            }

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                currentStatus = Status.MOBILE;
            }
        }
        else
        {
            currentStatus = Status.OFFLINE;
        }

        return currentStatus;
    }


    public static boolean isOnline()
    {
        getStatus();
        return currentStatus.equals(Status.WIFI) || currentStatus.equals(Status.MOBILE) || currentStatus.equals(Status.ETHERNET);
    }

    public static boolean isWifi()
    {
        return getStatus().equals(Status.WIFI);
    }

    public static boolean isEthernet()
    {
        return getStatus().equals(Status.ETHERNET);
    }

    public static boolean isMobile()
    {
        return getStatus().equals(Status.MOBILE);
    }

    public static boolean isOffline()
    {
        return getStatus().equals(Status.OFFLINE);
    }
}
