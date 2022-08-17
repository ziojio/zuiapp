package com.ziojio.zuiapp.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.ziojio.zuiapp.databinding.ActivityInputBinding;

import timber.log.Timber;

public class InputActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInputBinding binding = ActivityInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Timber.d("beforeTextChanged s=" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.d("onTextChanged s=" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Timber.d("afterTextChanged s=" + s.toString());
            }
        });
    }

}
