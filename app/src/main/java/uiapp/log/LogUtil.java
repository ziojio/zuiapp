package uiapp.log;

import android.content.Context;

import uiapp.UIApp;
import uiapp.database.room.entity.TrackLog;
import uiapp.util.AsyncTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        return "timber_" + new SimpleDateFormat("yyyyMMdd", Locale.US).format(date) + ".log";
    }

    public static void saveLog(@NonNull TrackLog trackLog) {
        AsyncTask.doAction(() -> UIApp.getDB().trackLogDao().insert(trackLog));
    }

}
