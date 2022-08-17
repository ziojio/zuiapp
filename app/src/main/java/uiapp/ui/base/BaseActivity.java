package uiapp.ui.base;

import android.content.res.Resources;
import android.widget.Toast;

import uiapp.UIApp;

import androidz.app.AppActivity;
import androidz.util.AdaptScreenUtils;
import androidz.util.ScreenUtil;

public class BaseActivity extends AppActivity {

    @Override
    public Resources getResources() {
        if (ScreenUtil.isPortrait(UIApp.getApp())) {
            return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
        } else {
            return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
        }
    }

    protected void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

}
