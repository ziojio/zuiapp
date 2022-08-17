package uiapp.log;

import android.content.Context;

import uiapp.UIApp;
import uiapp.database.room.entity.TrackLog;
import uiapp.database.room.entity.TrackLogDao;
import uiapp.util.AsyncTask;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;

public class LogUtil {

    public static File getLogDir(@NonNull Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            file = context.getCacheDir();
        }
        return new File(file, "log");
    }

    public static String getLogFileName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return "timber_" + calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DAY_OF_MONTH) + ".log";
    }

    private static TrackLogDao trackLogDao() {
        return UIApp.getDB().trackLogDao();
    }

    public static void saveLog(@NonNull TrackLog trackLog) {
        AsyncTask.doAction(() -> trackLogDao().insert(trackLog));
    }

}
