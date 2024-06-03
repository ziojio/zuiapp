package uiapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Date;

import androidz.App;
import androidz.AppUtil;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import uiapp.database.room.AppDB;
import uiapp.database.room.entity.TrackLog;
import uiapp.log.FileLogTree;
import uiapp.log.LogUtil;
import uiapp.ui.ktx.AppCache;

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
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NonNull String message, Throwable t) {
                    LogUtil.saveLog(new TrackLog(tag, message));
                    super.log(priority, tag, message, t);
                }
            });
        }
        MMKV.initialize(this);
        appDB = AppDB.create(this);

        long time = SystemClock.elapsedRealtime() - start;
        Log.d("UIApp", "init cost time " + time + "ms");
        AppCache.set("AAA", "aaa");
        String a = (String) AppCache.get("A");

        Timber.d("onCreate isDebuggable " + App.INSTANCE.isDebuggable());
        Timber.d("onCreate globalData " + App.globalData);
    }

    public static boolean debuggable() {
        return (uiApp.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}

