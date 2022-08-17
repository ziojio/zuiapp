package uiapp.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;

public class FileLogTree extends Timber.DebugTree implements Handler.Callback {
    private final File file;
    private SimpleDateFormat dateFormat;
    private FileWriter fileWriter;
    private HandlerThread handlerThread;
    private Handler handler;

    public FileLogTree(File file) {
        this.file = Objects.requireNonNull(file);
        initLog();
    }

    private void initLog() {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, true);

            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ROOT);
            handlerThread = new HandlerThread("FileLog");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper(), this);
        } catch (Exception e) {
            Log.e("FileLogTree", "initLog Failed file[" + file + "]", e);
        }
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = Objects.requireNonNull(dateFormat);
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (handler != null) {
            String log = dateFormat.format(new Date()) + " " + typeInfo(priority) + " " + tag + ": " + message + "\n";
            handler.sendMessage(Message.obtain(handler, 0, log));
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        try {
            // long start = SystemClock.elapsedRealtime();
            final String log = (String) msg.obj;
            if (fileWriter != null) {
                fileWriter.write(log);
                fileWriter.flush();
            }
            // long time = SystemClock.elapsedRealtime() - start;
            // Log.d("FileLogTree", "write length=" + log.length() + " time=" + time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void exit() {
        handler = null;
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileWriter = null;
        }
    }

    private String typeInfo(int priority) {
        switch (priority) {
            case Log.VERBOSE:
                return "V";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
            case Log.DEBUG:
            default:
                return "D";
        }
    }
}