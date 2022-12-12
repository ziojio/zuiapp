package zuiapp.ui.activity;

import android.os.Bundle;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import zuiapp.databinding.ActivityScrollBinding;

public class ScrollActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityScrollBinding binding = ActivityScrollBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("ScrollDemo");

        binding.text.setOnClickListener(v -> {
            // 投掷动画 旋转
            FlingAnimation fling = new FlingAnimation(v, DynamicAnimation.ROTATION);
            fling.setStartVelocity(1000)
                    .setFriction(1.1f)
                    .start();
        });

        OverScrollDecoratorHelper.setUpOverScroll(binding.hScrollView);

    }


}
