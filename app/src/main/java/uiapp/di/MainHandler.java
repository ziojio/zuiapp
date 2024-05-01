package uiapp.di;

import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;


public class MainHandler extends Handler {

    @Inject
    public MainHandler() {
        super(Looper.getMainLooper());
    }
}
