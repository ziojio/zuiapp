package uiapp.ui.databinding;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;

import com.kunminx.architecture.ui.state.State;

import timber.log.Timber;
import uiapp.R;
import uiapp.databinding.ActivityDatabindingBinding;
import uiapp.ui.base.BaseActivity;


public class DataBindingActivity extends BaseActivity {

    private final State<String> state = new State<>("");
    private final ObservableField<String> stateF = new ObservableField<>("") {
        @Override
        public void set(String value) {
            boolean isUnChanged = stateF.get() == value;
            super.set(value);
            if (isUnChanged) {
                stateF.notifyChange();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDatabindingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        binding.setLifecycleOwner(this);
        BindingViewModel bindingViewModel = new ViewModelProvider(this).get(BindingViewModel.class);
        binding.setModel(bindingViewModel);
        binding.setHandler(new EventHandler());

        binding.titlebar.title.setText("DataBinding");

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
