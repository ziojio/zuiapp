package zuiapp.ui.activity;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import timber.log.Timber;
import zuiapp.databinding.ActivityTouchBinding;
public class TouchActivity extends BaseActivity {
    private ActivityTouchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTouchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.setTitle("TouchDemo");

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
                fling(binding.hScrollView, velocityX);
                return true;
            }
        });
        binding.text.setOnTouchListener((v, event) -> detector.onTouchEvent(event));
    }

    private void fling(View view, float velocityX) {
        // 投掷动画 水平滑动ScrollView
        FlingAnimation fling = new FlingAnimation(view, DynamicAnimation.SCROLL_X);
        fling.setStartVelocity(velocityX).start();
    }
}
