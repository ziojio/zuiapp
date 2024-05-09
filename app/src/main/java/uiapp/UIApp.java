package uiapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Date;

import androidx.annotation.NonNull;
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
        App.INSTANCE.attachApplication(this);

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
        Timber.d("init cost time " + time + "ms");

        Timber.d("onCreate " + App.INSTANCE);
        Timber.d("onCreate " + App.INSTANCE.isDebuggable());
        Timber.d("onCreate " + App.INSTANCE.getApplicationInfo());
        Timber.d("onCreate " + App.INSTANCE.getPackageInfo());
        Timber.d("onCreate " + App.INSTANCE.getGlobalData());
    }

    public static boolean debuggable() {
        return (uiApp.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}

