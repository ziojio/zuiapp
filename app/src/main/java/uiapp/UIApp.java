package uiapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Date;

import androidz.util.AppUtil;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import uiapp.database.room.AppDB;
import uiapp.database.room.entity.TrackLog;
import uiapp.log.FileLogTree;
import uiapp.log.LogUtil;
import uiapp.ui.ktx.App;


@HiltAndroidApp
public class UIApp extends Application {
    private static UIApp uiApp;
    AppDB appDB;

    public static UIApp getApp() {
        return uiApp;
    }

    public static AppDB getDB() {
        return uiApp.appDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uiApp = this;

        Log.d("UIApp", "onCreate " + this);
        long start = SystemClock.elapsedRealtime();
        if (AppUtil.isDebuggable(this)) {
            Timber.plant(new FileLogTree(new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(new Date()))));
            Timber.plant(new Timber.DebugTree());
        }
        MMKV.initialize(this);
        appDB = AppDB.create(this);

        long time = SystemClock.elapsedRealtime() - start;
        LogUtil.saveLog(new TrackLog("UIApp", "init cost time " + time + "ms"));

        App.attachApplication(this);
        Log.d("UIApp", "onCreate " + App.INSTANCE);
        Log.d("UIApp", "onCreate " + App.getAttachApplication());
        Log.d("UIApp", "onCreate " + App.isDebuggable());
        Log.d("UIApp", "onCreate " + App.INSTANCE.getApplicationInfo());
        Log.d("UIApp", "onCreate " + App.getPackageInfo());
        Log.d("UIApp", "onCreate " + App.globalData);
    }

    public static boolean debuggable() {
        return (uiApp.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}

