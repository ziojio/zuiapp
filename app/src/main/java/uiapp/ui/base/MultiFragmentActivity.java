package uiapp.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import uiapp.R;
import uiapp.databinding.ActivityFragmentContainerBinding;

/**
 * 多个 Fragment 容器，像 Activity 一样
 */
public class MultiFragmentActivity extends BaseActivity {
    private int containerId;

    public static <T extends Fragment> void start(@NonNull Activity activity, @NonNull Class<T> fragment) {
        start(activity, fragment, null);
    }

    public static <T extends Fragment> void start(@NonNull Activity activity, @NonNull Class<T> fragment,
                                                  @Nullable Bundle arguments) {
        Intent intent = new Intent(activity, MultiFragmentActivity.class);
        intent.putExtra("fragment", fragment.getName());
        if (arguments != null) {
            intent.putExtra("arguments", arguments);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityFragmentContainerBinding binding = ActivityFragmentContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.fragmentContainerView);
        containerId = binding.fragmentContainerView.getId();

        final String fragmentClass = getIntent().getStringExtra("fragment");
        if (fragmentClass != null) {
            Fragment fragment = getSupportFragmentManager().getFragmentFactory()
                    .instantiate(getClassLoader(), fragmentClass);
            final Bundle arguments = getIntent().getBundleExtra("arguments");
            if (arguments != null) {
                fragment.setArguments(arguments);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(containerId, fragment)
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

    public void pop() {
        pop(null);
    }

    public void pop(@Nullable String name) {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack(name, 0);
        }
    }

    public void start(@NonNull Fragment fragment) {
        start(fragment, null, true, null);
    }

    public void start(@NonNull Fragment fragment, @Nullable String tag) {
        start(fragment, tag, true, null);
    }

    public void start(@NonNull Fragment fragment, @Nullable String tag, boolean addBackStack) {
        start(fragment, tag, addBackStack, null);
    }

    public void start(@NonNull Fragment fragment, @Nullable String tag, boolean addBackStack, @Nullable String name) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out,
                R.anim.slide_left_in, R.anim.slide_right_out);
        transaction.replace(containerId, fragment, tag);
        if (addBackStack) {
            transaction.addToBackStack(name);
        }
        transaction.commit();
    }
}
