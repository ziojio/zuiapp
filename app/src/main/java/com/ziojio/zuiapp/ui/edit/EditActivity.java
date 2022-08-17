package com.ziojio.zuiapp.ui.edit;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.ziojio.zuiapp.databinding.ActivityEditBinding;
import com.ziojio.zuiapp.ui.activity.BaseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class EditActivity extends BaseActivity implements View.OnClickListener {
    private ActivityEditBinding binding;
    private SimpleDateFormat format;
    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        binding.filename.setText(format.format(new Date()) + ".txt");
        binding.save.setOnClickListener(this);

        launcher = registerForActivityResult(new ActivityResultContracts.CreateDocument("text/txt"),
                result -> {
                    Timber.d("result=" + result);
                });
        binding.text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Timber.d("beforeTextChanged: s=%s, start=%s, count=%s, after=%s", s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timber.d("onTextChanged: s=%s, start=%s, before=%s, count=%s", s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Timber.d("afterTextChanged: s=%s", s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        saveText();
    }

    private void saveText() {
        String name = binding.filename.getText().toString();
        Timber.d("name=" + name);
        int length = binding.text.getText().length();
        Timber.d("text length=" + length);
        String text = binding.text.getText().toString();
        Timber.d("--------------------------------------------------------------");
        Timber.d("text=" + text);
        Timber.d("--------------------------------------------------------------");
        try {
            FileWriter fileWriter = new FileWriter(new File(getExternalCacheDir(), name));
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
