package zuiapp;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import androidz.app.ViewModelApplication;
import androidz.util.ActivityStackManager;
import androidz.util.AppUtil;
import timber.log.Timber;
import zuiapp.database.room.AppDB;
import zuiapp.di.AppDI;
import zuiapp.di.DaggerAppDI;
import zuiapp.log.TrackLogUtil;
import zuiapp.work.AppWork;

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


        if (getPackageName().equals(getProcessName(this))) {

        }
    }

    private void initWork() {
        AppWork.doLogWork(this);
    }

    public static String getProcessName(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return getProcessName();
        }
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}

