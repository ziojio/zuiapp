package uiapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import uiapp.R;
import uiapp.databinding.ActivityFragmentContainerBinding;
import uiapp.ui.base.BaseActivity;

public class MultiFragmentActivity extends BaseActivity {
    private static Fragment fragment;
    private int containerId;

    public static void start(Context context, Fragment fragment) {
        MultiFragmentActivity.fragment = fragment;
        Intent i = new Intent(context, MultiFragmentActivity.class);
        context.startActivity(i);
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

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();
        if (count > 0) {
            manager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void showFragment(@NonNull Fragment fragment, @Nullable String tag, boolean addBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);

        transaction.replace(containerId, fragment, tag);

        if (addBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
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
