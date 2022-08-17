package com.ziojio.zuiapp.ui.databinding;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ziojio.zuiapp.R;
import com.ziojio.zuiapp.databinding.ActivityDatabindingBinding;
import com.ziojio.zuiapp.model.StockLiveData;
import com.ziojio.zuiapp.ui.activity.BaseActivity;
import com.ziojio.zuiapp.ui.dialog.InputMethodDialog;
import com.ziojio.zuiapp.ui.dialog.InputMethodDialog2;
import com.ziojio.zuiapp.ui.dialog.ViewBindingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidz.util.OnDebouncingClickListener;
import timber.log.Timber;


public class DataBindingActivity extends BaseActivity {
    private MainViewModel mainViewModel;
    private DataBindingActivity activity = this;
    private static final SimpleDateFormat format =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        binding.setLifecycleOwner(this);
        mainViewModel = getActivityViewModel(MainViewModel.class);
        binding.setModel(mainViewModel);
        binding.setHandler(new EventHandler());

        binding.execFunction.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Timber.d("execFunction ");
                var string = "str";
                var number = 10;
                var bool = string.isBlank();

                // ViewBindingDialog dialog = new ViewBindingDialog();
                // dialog.show(getSupportFragmentManager(), dialog.toString());

                String msg = "Now Time: " + format.format(new Date());
                Timber.d(msg);
                ViewBindingDialog dialog = new ViewBindingDialog();
                dialog.show(DataBindingActivity.this.getSupportFragmentManager(), "123");
            }
        });
    }

    public class EventHandler {

        public void openDialog1(View view) {
            Timber.d("openDialog1: ");
            ViewBindingDialog dialog = new ViewBindingDialog();
            dialog.show(getSupportFragmentManager(), "1");
        }

        public void openDialog2(View view) {
            Timber.d("openDialog2: ");
            InputMethodDialog dialog = new InputMethodDialog(activity);
            dialog.show();
        }

        public void openDialog3(View view) {
            Timber.d("openDialog3: ");
            InputMethodDialog2 dialog = new InputMethodDialog2();
            dialog.show(activity.getSupportFragmentManager(), "3");
        }
    }

    public static class MainViewModel extends ViewModel {
        public String title = "DataBinding";
        public MutableLiveData<String> datatime = new MutableLiveData<>(format.format(new Date()));
        public MutableLiveData<String> imgUrl = new MutableLiveData<>(
                "https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7482b3ad2cd14edda31f05399c2ae759~tplv-k3u1fbpfcp-zoom-1.image");

        public StockLiveData stockLiveData = new StockLiveData("main");

    }
}
