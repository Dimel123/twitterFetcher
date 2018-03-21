package app.my_group.com.myapplication.common.managers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import app.my_group.com.MyApplication;

public class NetworkManager {

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getAppComponent().getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return ((networkInfo != null && networkInfo.isConnected()));
    }

}