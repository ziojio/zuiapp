package zuiapp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidz.util.OnDebouncingClickListener;
import timber.log.Timber;
import zuiapp.databinding.ActivityMaterialBinding;

public class MaterialActivity extends BaseActivity {
    private ActivityMaterialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(0);

        binding = ActivityMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.text.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Timber.d("onDebouncingClick ");
            }
        });
        binding.seekbar.setMax(50);
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean setColor = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.text.setText("" + seekBar.getProgress() + "px");
                binding.blurView.setBlurRadius(seekBar.getProgress());
                if (!setColor) {
                    setColor = true;
                    // binding.blurView.setOverlayColor(Color.parseColor("#aaff0000"));
                    // binding.blurView.setDownsampleFactor(1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(option);
    }
}
