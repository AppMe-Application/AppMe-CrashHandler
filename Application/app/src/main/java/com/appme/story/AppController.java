package com.appme.story;

import android.app.Application;
import android.content.Context;
import android.view.View;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.appme.story.engine.app.analytics.crash.CrashTracker;
import com.appme.story.engine.app.analytics.crash.investigation.AppInfo;
import com.appme.story.engine.app.analytics.crash.investigation.AppInfoProvider;
import com.appme.story.engine.app.analytics.crash.CrashNotInitializedException;
import com.appme.story.engine.app.utils.AppInfoUtil;

public class AppController extends Application
{
    private static AppController _instance;
    private static Context _Context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        _Context = this;
        try {

            CrashTracker.init(this); //Initializing Sherlock
            CrashTracker.setAppInfoProvider(new AppInfoProvider() {
                    @Override
                    public AppInfo getAppInfo() {
                        return new AppInfo.Builder()
                            .with("Version", AppInfoUtil.getAppVersion(getApplicationContext())) //You can get the actual version using "AppInfoUtil.getAppVersion(context)"
                            .with("BuildNumber", "1")
                            .build();
                    }
                });

        } catch (CrashNotInitializedException e) {
            e.printStackTrace();
        } 
    }

    public static synchronized AppController getInstance() {
        return _instance;
    }
    
    public static synchronized Context getContext(){
        return _Context;
    }

}
