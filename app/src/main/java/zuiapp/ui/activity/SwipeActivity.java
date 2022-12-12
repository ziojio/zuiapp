package zuiapp.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SmartSwipeWrapper;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.BezierBackConsumer;
import com.billy.android.swipe.listener.SimpleSwipeListener;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import timber.log.Timber;
import zuiapp.databinding.ActivitySwipeBinding;

public class SwipeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySwipeBinding binding = ActivitySwipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("SwipeDemo");

        final View animView = binding.text;
        final View hScrollView = binding.hScrollView;

        binding.text.setOnClickListener(v -> {
            // 投掷动画 旋转
            FlingAnimation fling = new FlingAnimation(animView, DynamicAnimation.ROTATION);
            fling.setStartVelocity(1000)
                    .setFriction(1.1f)
                    .start();
        });

        // onSwipeStart -> onSwipeRelease -> onSwipeOpened -> onSwipeClosed
        // if condition : onSwipeOpened
        SmartSwipe.wrap(this)
                .addConsumer(new BezierBackConsumer())
                .enableHorizontal()
                .addListener(new SimpleSwipeListener() {

                    @Override
                    public void onSwipeStart(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        super.onSwipeStart(wrapper, consumer, direction);
                        Timber.d("onSwipeStart");
                    }

                    @Override
                    public void onSwipeRelease(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction, float progress, float xVelocity, float yVelocity) {
                        super.onSwipeRelease(wrapper, consumer, direction, progress, xVelocity, yVelocity);
                        Timber.d("onSwipeRelease");
                    }

                    @Override
                    public void onSwipeOpened(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        super.onSwipeOpened(wrapper, consumer, direction);
                        Timber.d("onSwipeOpened");
                        finish();
                    }

                    @Override
                    public void onSwipeClosed(SmartSwipeWrapper wrapper, SwipeConsumer consumer, int direction) {
                        super.onSwipeClosed(wrapper, consumer, direction);
                        Timber.d("onSwipeClosed");
                    }

                });
    }


}
