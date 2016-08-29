package com.wtho.cwsm;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by WT on 8/29/2016.
 */
public class ConnectWithSocialMediaApp extends Application {
    public static final String TAG = "SocialMediaApp";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
    public static Context getContext(){return context;}
}
