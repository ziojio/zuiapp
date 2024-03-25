package uiapp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import uiapp.databinding.ActivityAnimationBinding;
import uiapp.ui.base.BaseActivity;

import androidz.util.AnimateUtil;
import timber.log.Timber;

public class AnimationActivity extends BaseActivity {
    private ActivityAnimationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnimationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("Animation");

        binding.dot.setOnClickListener(v -> {
            SpringForce springForce = new SpringForce(1)
                    .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                    .setStiffness(SpringForce.STIFFNESS_MEDIUM);
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
            set.play(ObjectAnimator.ofFloat(v, View.SCALE_X, 1.5f, 1))
                    .with(ObjectAnimator.ofFloat(v, View.SCALE_Y, 1.5f, 1));
            // set.setDuration(300);
            set.setInterpolator(new OvershootInterpolator());
            set.start();
        });

        touchFling();

        SurfaceView surfaceView;

    }

    private void circularReveal(View view) {
        if (view.getVisibility() == View.GONE) {
            AnimateUtil.circularRevealIn(view);
            AnimateUtil.fadeIn(view);
        } else {
            AnimateUtil.circularRevealOut(view);
            AnimateUtil.fadeOut(view);
        }
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

    @SuppressLint("ClickableViewAccessibility")
    private void touchFling() {
        GestureDetector detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                Timber.d("onDown: x=" + e.getRawX() + ", y=" + e.getRawY());
                return true;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent e) {
                Timber.d("onShowPress: x=" + e.getRawX() + ", y=" + e.getRawY());
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                Timber.d("onSingleTapUp: x=" + e.getRawX() + ", y=" + e.getRawY());
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {
                Timber.d("onLongPress: x=" + e.getRawX() + ", y=" + e.getRawY());
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                Timber.d("onScroll: -------------------------------------------");
                Timber.d("onScroll: distanceX=" + distanceX + ", distanceY=" + distanceY);

                return true;
            }

            @Override
            public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                Timber.d("onFling: velocityX=" + velocityX + ", velocityY=" + velocityY);
                scrollFling(binding.hScrollView, velocityX);
                return true;
            }
        });

        binding.text.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }

    private void scrollFling(View view, float velocityX) {
        // 投掷动画 水平滑动ScrollView
        FlingAnimation fling = new FlingAnimation(view, DynamicAnimation.SCROLL_X);
        fling.setStartVelocity(velocityX).start();
    }
}
