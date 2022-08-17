package uiapp;

import android.app.Application;

import com.tencent.mmkv.MMKV;
import uiapp.database.room.AppDB;
import uiapp.database.room.entity.TrackLog;
import uiapp.log.FileLogTree;
import uiapp.log.LogUtil;

import java.io.File;
import java.util.Date;

import androidz.util.AppUtil;
import timber.log.Timber;

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

        if (AppUtil.isDebuggable(this)) {
            Timber.plant(new Timber.DebugTree());
            Timber.plant(new FileLogTree(new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(new Date()))));
        }

        LogUtil.saveLog(new TrackLog("onCreate", ""));
    }

}

