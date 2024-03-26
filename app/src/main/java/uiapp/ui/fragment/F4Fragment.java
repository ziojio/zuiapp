package uiapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidz.util.OnDebouncingClickListener;
import timber.log.Timber;
import uiapp.databinding.ActivityFuncBinding;
import uiapp.ui.base.BaseFragment;
import uiapp.ui.base.MultiFragmentActivity;

public class F4Fragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MultiFragmentActivity activity = (MultiFragmentActivity) requireActivity();
        ActivityFuncBinding binding = ActivityFuncBinding.inflate(inflater, container, false);

        binding.titlebar.setTitle(getClass().getSimpleName());
        binding.titlebar.setLeftClickListener(v -> activity.pop());
        binding.execFunction.setOnClickListener((OnDebouncingClickListener) v -> {
            Timber.d("execFunction");
        });
        return binding.getRoot();
    }

}

