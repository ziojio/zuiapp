package uiapp;

import android.app.Application;
import android.content.pm.ApplicationInfo;

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

@HiltAndroidApp
public class UIApp extends Application {
    static UIApp app;
    AppDB appDB;

    public static UIApp getApp() {
        return app;
    }

    public static AppDB getDB() {
        return app.appDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        appDB = AppDB.create(this);
        MMKV.initialize(this);

        if (AppUtil.isDebuggable()) {
            Timber.plant(new Timber.DebugTree());
            Timber.plant(new FileLogTree(new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(new Date()))));
        }
        LogUtil.saveLog(new TrackLog("onCreate", ""));
    }

    public static boolean debuggable() {
        return (app.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}

