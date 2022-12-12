package zuiapp.ui.activity;

import android.content.res.Resources;

import androidz.app.AppActivity;
import androidz.blankj.AdaptScreenUtils;
import androidz.blankj.ScreenUtils;

public class BaseActivity extends AppActivity {

    @Override
    public Resources getResources() {
        if (ScreenUtils.isPortrait()) {
            return AdaptScreenUtils.adaptWidth(super.getResources(), 360);
        } else {
            return AdaptScreenUtils.adaptHeight(super.getResources(), 640);
        }
    }

}
