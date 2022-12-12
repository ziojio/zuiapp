package zuiapp.ui.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidz.bluetooth.BluetoothDiscoverReceiver;
import androidz.recyclerview.BaseListAdapter;
import androidz.recyclerview.BaseViewHolder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;
import zuiapp.R;
import zuiapp.databinding.ActivityBluetoothBinding;
import zuiapp.databinding.AdapterBtdeviceBinding;
import zuiapp.ui.activity.BaseActivity;

public class BluetoothActivity extends BaseActivity {
    private ActivityBluetoothBinding binding;
    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private String[] perms = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final BroadcastReceiver receiver = new BluetoothDiscoverReceiver(new BluetoothDiscoverReceiver.OnDiscoverListener() {
        @Override
        public void onFound(BluetoothDevice device) {
            Timber.d("onFound: name=" + device.getName() + ", address=" + device.getAddress());

            BTAdapter adapter = (BTAdapter) binding.recyclerview.getAdapter();
            if (!adapter.contains(device)) {
                adapter.add(device);
            }
        }

        @Override
        public void onStart() {
            Timber.d("onStart: ");
            BTAdapter adapter = (BTAdapter) binding.recyclerview.getAdapter();
            adapter.refreshList(null);
        }

        @Override
        public void onStop() {
            Timber.d("onStop: ");
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }
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

        BTAdapter adapter = new BTAdapter();
        binding.recyclerview.setAdapter(adapter);
        binding.discover.setOnClickListener(view -> {
            adapter.submitList(null);
            startDiscover();
        });

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @AfterPermissionGranted(10)
    public void startDiscover() {
        Timber.d("startDiscover: ");
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            if (adapter.isEnabled()) {
                new Thread() {
                    @Override
                    public void run() {
                        if (adapter.isDiscovering()) {
                            adapter.cancelDiscovery();
                        }
                        adapter.startDiscovery();
                    }
                }.start();
            } else {
                startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
            }
        } else {
            requestBluetoothPermission(10);
        }
    }

    private void requestBluetoothPermission(int reqCode) {
        EasyPermissions.requestPermissions(this, "使用蓝牙", reqCode, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    static class BTAdapter extends BaseListAdapter<BluetoothDevice> {
        static final DiffUtil.ItemCallback<BluetoothDevice> callback = new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
                return oldItem.equals(newItem);
            }
        };

        protected BTAdapter() {
            super(callback);
        }

        @Override
        protected int getLayoutRes(int viewType) {
            return R.layout.adapter_btdevice;
        }

        @Override
        public void onBindItem(BluetoothDevice device, BaseViewHolder holder, int position) {
            AdapterBtdeviceBinding binding = AdapterBtdeviceBinding.bind(holder.itemView);
            binding.name.setText(device.getName() == null ? "unknown" : device.getName());
            binding.address.setText(device.getAddress());
        }
    }

}
