package com.paradisetechnologies.brithwine;

import android.app.Application;
import com.paradisetechnologies.brithwine.receiver.ConnectivityReceiver;

public class GlobalApplication extends Application {

    private static GlobalApplication globalApplication;
    public static final String SERVER_URL = "http://admin.brightwing.in/api/";
    public static final String SERVER_IMAGE_URL = "http://admin.brightwing.in/api/";

    @Override
    public void onCreate()
    {
        super.onCreate();
        globalApplication = this;
    }

    public static GlobalApplication getAppInstance()
    {
        return globalApplication;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
