package uiapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import uiapp.databinding.ActivityFuncBinding;
import uiapp.ui.base.BaseFragment;

import androidz.util.OnDebouncingClickListener;
import timber.log.Timber;

public class F4Fragment extends BaseFragment {
    private ActivityFuncBinding binding;

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
        binding.titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = getParentFragmentManager().getBackStackEntryCount();
                Timber.d("backStackEntryCount=" + count);
                getParentFragmentManager().popBackStack();
            }
        });
        binding.execFunction.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Timber.d("execFunction");

            }
        });
    }

}

