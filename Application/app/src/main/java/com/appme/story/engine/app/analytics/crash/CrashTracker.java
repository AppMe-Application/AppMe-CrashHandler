package com.appme.story.engine.app.analytics.crash;

import android.support.v4.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.appme.story.engine.app.analytics.crash.database.CrashRecord;
import com.appme.story.engine.app.analytics.crash.database.CrashDatabaseHelper;
import com.appme.story.engine.app.analytics.crash.investigation.AppInfoProvider;
import com.appme.story.engine.app.analytics.crash.investigation.Crash;
import com.appme.story.engine.app.analytics.crash.investigation.CrashAnalyzer;
import com.appme.story.engine.app.analytics.crash.investigation.CrashReporter;
import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.analytics.crash.investigation.DefaultAppInfoProvider;
import com.appme.story.application.ApplicationCrashHandler;

import java.util.List;

public class CrashTracker {
    
    private static final String TAG = CrashTracker.class.getSimpleName();
    private static CrashTracker instance;
    private final CrashDatabaseHelper database;
    private final CrashReporter crashReporter;
    private AppInfoProvider appInfoProvider;
    private static Context mContext;

    private CrashTracker(Context context) {
        mContext = context;
        database = new CrashDatabaseHelper(context);
        crashReporter = new CrashReporter(context);
        appInfoProvider = new DefaultAppInfoProvider(context);
    }

    public static void init(final Context context) {
        Log.d(TAG, "Initializing Sherlock...");
        instance = new CrashTracker(context);

        final Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable throwable) {
                    analyzeAndReportCrash(throwable);
                    handler.uncaughtException(thread, throwable);
                }
            });
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static CrashTracker getInstance() {
        if (!isInitialized()) {
            throw new CrashNotInitializedException();
        }
        Log.d(TAG, "Returning existing instance...");
        return instance;
    }

    public List<Crash> getAllCrashes() {
        return getInstance().database.getCrashes();
    }

    private static void analyzeAndReportCrash(Throwable throwable) {
        Log.d(TAG, "Analyzing Crash...");
        CrashAnalyzer crashAnalyzer = new CrashAnalyzer(throwable);
        Crash crash = crashAnalyzer.getAnalysis();
        CrashViewModel crashView = new CrashViewModel(crash);
        int crashId = instance.database.insertCrash(CrashRecord.createFrom(crash));
        crash.setId(crashId);
        instance.crashReporter.report(crashView);
        restart();
        //android.os.Process.killProcess(android.os.Process.myPid());
        Log.d(TAG, "Crash analysis completed!");
    }

    public static void setAppInfoProvider(AppInfoProvider appInfoProvider) {
        getInstance().appInfoProvider = appInfoProvider;
    }

    public AppInfoProvider getAppInfoProvider() {
        return getInstance().appInfoProvider;
    }
    
    private static void restart() {
        PackageManager pm = mContext.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(mContext.getPackageName());
        if (intent != null) {
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            mContext.startActivity(intent);
        }
        //finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    
}
