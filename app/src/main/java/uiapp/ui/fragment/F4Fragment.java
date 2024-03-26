package uiapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import timber.log.Timber;
import uiapp.databinding.ActivityFuncBinding;
import uiapp.ui.base.MultiFragment;

public class F4Fragment extends MultiFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFuncBinding binding = ActivityFuncBinding.inflate(inflater, container, false);

        binding.titlebar.setTitle(getClass().getSimpleName());
        binding.titlebar.setLeftClickListener(v -> pop());

        binding.execFunction.setOnClickListener(v -> {
            Timber.d("execFunction");
        });
        return binding.getRoot();
    }

}

