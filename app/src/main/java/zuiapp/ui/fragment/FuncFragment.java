package zuiapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidz.app.AppFragment;
import androidz.util.OnDebouncingClickListener;
import timber.log.Timber;
import zuiapp.databinding.ActivityFuncBinding;
import zuiapp.ui.activity.FragmentContainerActivity;
import zuiapp.util.DebugLifecycleObserver;

public class FuncFragment extends AppFragment {
    private ActivityFuncBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new DebugLifecycleObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityFuncBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBinding();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onBinding() {
        binding.titlebar.setTitle(getClass().getSimpleName());
        binding.titlebar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getParentFragmentManager().getBackStackEntryCount();
                Timber.d("backStackEntryCount=" + count);
                if (count == 0) {
                    requireActivity().finish();
                } else {
                    getParentFragmentManager().popBackStack();
                }
            }
        });
        binding.execFunction.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Timber.d("execFunction");
                FragmentContainerActivity activity = (FragmentContainerActivity) requireActivity();
                activity.showFragment(new F2Fragment(), null, true);
            }
        });
    }

}

