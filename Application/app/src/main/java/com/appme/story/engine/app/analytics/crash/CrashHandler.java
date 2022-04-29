package com.appme.story.engine.app.analytics.crash;

import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;

import com.appme.story.R;
import com.appme.story.application.ApplicationCrashHandler;

@SuppressWarnings({"FieldCanBeLocal", "StringBufferReplaceableByString"})
public class CrashHandler implements UncaughtExceptionHandler {

    private static CrashHandler mCrashHandler;
    private static Context mContext;

    public CrashHandler(){}

    public static synchronized CrashHandler getInstance() {
        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }
        return mCrashHandler;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public static String getLogFolderPath() {
        return mContext.getExternalFilesDir(".crash").getAbsolutePath();
    }
    
    // try catch
    public void uncaughtException(Thread thread, Throwable exception) {
        String appName = mContext.getString(R.string.app_name);
        System.out.println(appName);
        exception.printStackTrace();
        StringBuilder sb = new StringBuilder();
        PackageManager pm = mContext.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(mContext.getPackageName(), 0);
            sb.append(appName + " v" + packinfo.versionName);
            sb.append("\n");

            // 2.获取手机的硬件信息.
            Field[] fields = Build.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                String name = fields[i].getName();
                sb.append(name + " = ");
                String value = fields[i].get(null).toString();
                sb.append(value);
                sb.append("\n");
            }
            // 3.获取程序错误的堆栈信息 .
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));

            String result = stackTrace.toString();
            sb.append(result);
            String time =  new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String propertyName ="crash_" + time + ".txt";
            String folder = getLogFolderPath();

            File log = new File(folder, propertyName);
            log.getParentFile().mkdirs();
            File file = new File(log.getParent(), propertyName);
            if (!file.exists() || !file.isFile()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.d(CrashHandler.class.getSimpleName(), e.getMessage());
                }
            }
            FileOutputStream fos = new FileOutputStream(log);
            fos.write(sb.toString().getBytes());
            fos.close();   
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // KillProcess
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}



