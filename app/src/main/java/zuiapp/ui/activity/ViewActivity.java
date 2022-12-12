package zuiapp.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import timber.log.Timber;
import zuiapp.databinding.ActivityViewBinding;

public class ViewActivity extends BaseActivity {
    private ActivityViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(0);

        binding = ActivityViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        bundle.get("");
        Timber.d(getIntent().toString());
        Timber.d(getClass().getName() + "@" + Integer.toHexString(hashCode()));
        Timber.d(bundle.toString());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        // getWindow().getDecorView().setSystemUiVisibility(option);
    }
}
