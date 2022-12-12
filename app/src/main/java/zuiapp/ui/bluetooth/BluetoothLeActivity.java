package zuiapp.ui.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidz.bluetooth.BluetoothLeScanCallback;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;
import zuiapp.databinding.ActivityBluetoothLeBinding;
import zuiapp.ui.activity.BaseActivity;

public class BluetoothLeActivity extends BaseActivity {
    private ActivityBluetoothLeBinding binding;
    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private boolean isScanning;
    private final BluetoothLeScanCallback scanCallback = new BluetoothLeScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result, BluetoothDevice device) {
            Timber.d("------------------------------------------------");
            Timber.d("onScanResult name: %s, address= %s", device.getName(), device.getAddress());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results, List<BluetoothDevice> devices) {
            Timber.d("------------------------------------------------");
            for (BluetoothDevice device : devices) {
                Timber.d("onBatchScanResults name: %s, address= %s", device.getName(), device.getAddress());
            }
        }

        @Override
        public void onScanFailed(int errorCode, String errorMsg) {
            Timber.d(errorMsg);
        }
    };
    private String[] perms = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }
        binding = ActivityBluetoothLeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            adapter = manager.getAdapter();
        }

        binding.open.setOnClickListener(view -> {
            adapter.enable();
        });
        binding.close.setOnClickListener(view -> {
            adapter.disable();
        });
        binding.info.setOnClickListener(view -> {
            Timber.d("name=" + adapter.getName());
            Timber.d("address=" + adapter.getAddress());
            Timber.d("isEnabled=" + adapter.isEnabled());
            ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>(adapter.getBondedDevices());
            Timber.d("pairedDevices=" + pairedDevices);
        });
        binding.scan.setOnClickListener(view -> {
            startScan();
        });
        binding.stopScan.setOnClickListener(view -> {
            stopScan();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScan();
    }

    public void stopScan() {
        if (isScanning) {
            Timber.d("stopScan ");
            isScanning = false;
            adapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
    }

    @AfterPermissionGranted(10)
    public void startScan() {
        Timber.d("startScan ");

        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            if (adapter.isEnabled()) {
                if (isScanning) {
                    showToast("扫描中...");
                } else {
                    isScanning = true;
                    adapter.getBluetoothLeScanner().startScan(scanCallback);
                }
            } else {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        } else {
            EasyPermissions.requestPermissions(this, "使用蓝牙", 10, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
