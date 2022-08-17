package com.ziojio.zuiapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ziojio.zuiapp.databinding.ActivityMyBinding;

import androidz.app.AppFragment;

public class AndroidFragment extends AppFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityMyBinding binding = ActivityMyBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

}
