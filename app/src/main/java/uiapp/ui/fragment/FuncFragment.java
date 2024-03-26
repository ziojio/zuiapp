package uiapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uiapp.databinding.ActivityFuncBinding;
import uiapp.ui.base.MultiFragment;

public class FuncFragment extends MultiFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFuncBinding binding = ActivityFuncBinding.inflate(inflater, container, false);

        binding.titlebar.setTitle(getClass().getSimpleName());
        binding.titlebar.setLeftClickListener(v -> {
            pop();
        });
        binding.execFunction.setOnClickListener(v -> {
            start(new F2Fragment());
        });

        return binding.getRoot();
    }

}

