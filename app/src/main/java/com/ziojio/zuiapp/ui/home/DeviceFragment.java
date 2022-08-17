package com.ziojio.zuiapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ziojio.zuiapp.databinding.ActivityDeviceBinding;
import com.ziojio.zuiapp.ui.activity.DataBaseActivity;
import com.ziojio.zuiapp.ui.bluetooth.BluetoothActivity;
import com.ziojio.zuiapp.ui.bluetooth.BluetoothLeActivity;
import com.ziojio.zuiapp.ui.camera.AudioActivity;
import com.ziojio.zuiapp.ui.camera.Camera2Activity;
import com.ziojio.zuiapp.ui.camera.TakePictureActivity;
import com.ziojio.zuiapp.ui.camera.TakeVideoActivity;

import androidz.app.AppFragment;

public class DeviceFragment extends AppFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityDeviceBinding binding = ActivityDeviceBinding.inflate(inflater, container, false);

        binding.datebase.setOnClickListener(v -> startActivity(new Intent(requireContext(), DataBaseActivity.class)));
        binding.bluetooth.setOnClickListener(v -> startActivity(new Intent(requireContext(), BluetoothActivity.class)));
        binding.bluetoothLe.setOnClickListener(v -> startActivity(new Intent(requireContext(), BluetoothLeActivity.class)));
        binding.audio.setOnClickListener(v -> startActivity(new Intent(requireContext(), AudioActivity.class)));
        binding.camera.setOnClickListener(v -> startActivity(new Intent(requireContext(), TakePictureActivity.class)));
        binding.camera2.setOnClickListener(v -> startActivity(new Intent(requireContext(), Camera2Activity.class)));
        binding.video.setOnClickListener(v -> startActivity(new Intent(requireContext(), TakeVideoActivity.class)));

        return binding.getRoot();
    }

}
