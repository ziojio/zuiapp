package uiapp.ui.databinding;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import timber.log.Timber;
import uiapp.R;
import uiapp.databinding.ActivityDatabindingBinding;
import uiapp.ui.base.BaseActivity;


public class DataBindingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        binding.setLifecycleOwner(this);
        BindingViewModel bindingViewModel = getActivityViewModel(BindingViewModel.class);
        binding.setModel(bindingViewModel);
        binding.setHandler(new EventHandler());

        binding.titlebar.setTitle("DataBinding");

        binding.execFunction.setOnClickListener(v -> {
            Timber.d("execFunction ");
        });
    }

    public static class EventHandler {

        public void openDialog(View view) {
            Timber.d("openDialog: ");
        }

    }

}
