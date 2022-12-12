package zuiapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import timber.log.Timber;
import zuiapp.R;
import zuiapp.databinding.ActivityFragmentContainerBinding;

public class FragmentContainerActivity extends BaseActivity {
    private static Fragment fragment;
    private int containerId;

    public static void show(Activity activity, Fragment fragment) {
        FragmentContainerActivity.fragment = fragment;
        Intent i = new Intent(activity, FragmentContainerActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFragmentContainerBinding binding = ActivityFragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.fragmentContainerView);
        containerId = binding.fragmentContainerView.getId();
        final Fragment f = fragment;
        fragment = null;
        if (f != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(containerId, f)
                    .commit();
        }
    }

    public void showFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(containerId, fragment, tag);
        if (addBackStack) transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        Timber.d("onBackPressed");
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * back to activity first fragment
     */
    public void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            manager.popBackStack();
        }
    }

}
