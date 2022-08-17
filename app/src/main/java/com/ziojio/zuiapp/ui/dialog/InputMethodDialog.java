package com.ziojio.zuiapp.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ziojio.zuiapp.databinding.DialogInputNumberBinding;

import androidx.annotation.NonNull;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class InputMethodDialog extends BottomSheetDialog {

    public InputMethodDialog(@NonNull Context context) {
        super(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        setContentView(onCreateView(getLayoutInflater(), frameLayout));
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        DialogInputNumberBinding binding = DialogInputNumberBinding.inflate(inflater, container, false);
        binding.gridlayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        return binding.getRoot();
    }


}
