package uiapp.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import dagger.hilt.android.AndroidEntryPoint;
import uiapp.databinding.ActivityMainBinding;
import uiapp.ui.base.BaseActivity;
import uiapp.ui.fragment.homepage.DeviceFragment;
import uiapp.ui.fragment.homepage.HomeFragment;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewpager.setAdapter(new HomeAdapter(this));
        binding.viewpager.setUserInputEnabled(false);
        binding.bottomBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                binding.viewpager.setCurrentItem(group.indexOfChild(group.findViewById(checkedId)));
            }
        });
    }

    static class HomeAdapter extends FragmentStateAdapter {

        public HomeAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return new DeviceFragment();
            }
            return new HomeFragment();
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
