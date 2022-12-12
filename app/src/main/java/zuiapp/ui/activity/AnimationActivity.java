package zuiapp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidz.util.AnimateUtil;
import zuiapp.databinding.ActivityAnimationBinding;

public class AnimationActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAnimationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("Animation");

        binding.text.setOnClickListener(this);

        final SpringForce springForce = new SpringForce(1)
                .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_MEDIUM);
        binding.dot.setOnClickListener(v -> {
            final SpringAnimation anim = new SpringAnimation(v, DynamicAnimation.SCALE_X);
            final SpringAnimation anim2 = new SpringAnimation(v, DynamicAnimation.SCALE_Y);
            anim.setSpring(springForce);
            anim2.setSpring(springForce);
            anim.setStartVelocity(500);
            anim2.setStartVelocity(500);
            anim.setMaxValue(2);
            anim2.setMaxValue(2);
            anim.start();
            anim2.start();
        });

        binding.dot2.setOnClickListener(v -> {
            AnimatorSet set = new AnimatorSet();
            set.play(ObjectAnimator.ofFloat(v, View.SCALE_X, 2, 1))
                    .with(ObjectAnimator.ofFloat(v, View.SCALE_Y, 2, 1));
            set.setDuration(300);
            set.setInterpolator(new OvershootInterpolator());
            set.start();
        });
    }

    private void circularReveal(View view) {
        if (view.getVisibility() == View.GONE) {
            AnimateUtil.circularRevealShow(view);
            AnimateUtil.fadeIn(view);
        } else {
            AnimateUtil.circularRevealHide(view, View.GONE);
            AnimateUtil.fadeOut(view, View.GONE);
        }
    }

    private void fling(View view) {
        // 投掷动画 水平滑动ScrollView
        FlingAnimation fling = new FlingAnimation(view, DynamicAnimation.SCROLL_X);
        fling.setStartVelocity(1000).start();
    }

    private void spring(View view) {
        // 弹簧动画
        final SpringAnimation anim = new SpringAnimation(view, DynamicAnimation.TRANSLATION_Y);
        final SpringForce springForce = new SpringForce()
                .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
                .setStiffness(SpringForce.STIFFNESS_LOW)
                .setFinalPosition(0);
        anim.setSpring(springForce);
        anim.setStartVelocity(5000);
        anim.start();
    }

    @Override
    public void onClick(View v) {
        circularReveal(binding.hScrollView);
    }
}
