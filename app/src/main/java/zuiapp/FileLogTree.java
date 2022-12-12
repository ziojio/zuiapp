package zuiapp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import timber.log.Timber;

class FileLogTree extends Timber.DebugTree implements Handler.Callback {
    private final SimpleDateFormat dateFormat;
    private FileOutputStream fileOutputStream;
    private HandlerThread handlerThread;
    private Handler handler;

    public FileLogTree(File file) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.CHINA);
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file, true);
            handlerThread = new HandlerThread("FileLogTree");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (handler != null) {
            String log = String.format("%s %s %s: %s \n", dateFormat.format(new Date()), typeInfo(priority), tag, message);
            handler.sendMessage(Message.obtain(handler, 1, log));
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

    private void write(String msg) {
        // long start = SystemClock.elapsedRealtime();
        try {
            fileOutputStream.write(msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // long time = SystemClock.elapsedRealtime() - start;
        // Log.d("FileLogTree", "bytes=" + bytes.length + "  time=" + time);
    }


    public void release() {
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handlerThread = null;
        handler = null;
        fileOutputStream = null;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        write((String) msg.obj);
        return true;
    }
}