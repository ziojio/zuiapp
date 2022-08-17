package com.ziojio.zuiapp;

import com.ziojio.zuiapp.database.room.AppDB;
import com.ziojio.zuiapp.di.AppDI;
import com.ziojio.zuiapp.di.DaggerAppDI;
import com.ziojio.zuiapp.log.TrackLogUtil;
import com.ziojio.zuiapp.work.AppWork;

import java.io.File;
import java.util.Calendar;

import androidz.app.ViewModelApplication;
import androidz.util.ActivityStackManager;
import androidz.util.AppUtil;
import timber.log.Timber;

public class ZuiApp extends ViewModelApplication {
    static ZuiApp app;
    AppDB appDB;
    AppDI appDI;

    public static ZuiApp getApp() {
        return app;
    }

    public static AppDI getDI() {
        return app.appDI;
    }

    public static AppDB getDB() {
        return app.appDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.initialize(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            String name = Calendar.getInstance().get(Calendar.MONTH) + 1
                    + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ".log";
            Timber.plant(new FileLogTree(new File(getCacheDir() + "/timber/" + name)));
        }
        app = this;
        appDI = DaggerAppDI.builder().application(this).build();
        appDB = AppDB.create(this);

        ActivityStackManager.getInstance().register(this);
        TrackLogUtil.saveLog("ZuiApp", "onCreate", "");

        // new DoKit.Builder(this).build();
    }

    private void initWork() {
        AppWork.doLogWork(this);
    }

}

