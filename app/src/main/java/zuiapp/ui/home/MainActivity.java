package zuiapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import zuiapp.databinding.ActivityMainBinding;
import zuiapp.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewpager.setAdapter(new HomeAdapter(this));
        binding.viewpager.setUserInputEnabled(false);
        binding.home.setOnClickListener(v -> binding.viewpager.setCurrentItem(0));
        binding.function.setOnClickListener(v -> binding.viewpager.setCurrentItem(1));
        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.home.setSelected(false);
                binding.function.setSelected(false);

                if (position == 0) {
                    binding.home.setSelected(true);
                } else if (position == 1) {
                    binding.function.setSelected(true);
                }
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
            switch (position) {
                case 1:
                    return new DeviceFragment();
                case 0:
                default:
                    return new HomeFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
