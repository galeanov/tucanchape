package com.example.amador.tucanchape;

import android.app.Application;


import com.facebook.appevents.AppEventsLogger;

public class tucanchape extends  Application{

    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);

    }
}
