package uiapp.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Predicate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.Map;

/**
 * 判断多个 TextView 的内容，启用或者禁用 View
 */
public final class InputTextManager implements TextWatcher, DefaultLifecycleObserver {

    /**
     * 操作按钮的View
     */
    private View mView;
    /**
     * 是否禁用后设置半透明度
     */
    private boolean mAlpha;
    /**
     * 由监听器设置
     */
    private OnInputTextListener mListener;
    /**
     * TextView集合
     */
    private Map<TextView, Predicate<CharSequence>> mViewMap;

    public InputTextManager(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
    }

    public static InputTextManager with(LifecycleOwner owner) {
        return new InputTextManager(owner);
    }

    public InputTextManager setView(View view) {
        mView = view;
        return this;
    }

    public InputTextManager setAlpha(boolean alpha) {
        mAlpha = alpha;
        return this;
    }

    public InputTextManager setListener(OnInputTextListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 添加检查的 TextView
     *
     * @param views 传入单个或者多个 TextView
     */
    public InputTextManager checkTextNotEmpty(TextView... views) {
        if (views != null) {
            if (mViewMap == null) {
                mViewMap = new HashMap<>();
            }
            for (TextView view : views) {
                // 避免重复添加
                if (!mViewMap.containsKey(view)) {
                    view.addTextChangedListener(this);
                    mViewMap.put(view, charSequence -> !TextUtils.isEmpty(charSequence));
                }
            }
            // 触发一次监听
            notifyChanged();
        }
        return this;
    }

    /**
     * 添加检查的 TextView
     *
     * @param views     传入TextView
     * @param predicate 输入文本满足的条件
     */
    public InputTextManager checkText(@NonNull Predicate<CharSequence> predicate, TextView... views) {
        if (views != null) {
            if (mViewMap == null) {
                mViewMap = new HashMap<>();
            }
            for (TextView view : views) {
                // 避免重复添加
                if (!mViewMap.containsKey(view)) {
                    view.addTextChangedListener(this);
                    mViewMap.put(view, charSequence -> !TextUtils.isEmpty(charSequence));
                }
            }
            // 触发一次监听
            notifyChanged();
        }
        return this;
    }

    /**
     * 移除 TextView 监听，避免内存泄露
     */
    public void removeViews(TextView... views) {
        if (mViewMap == null || mViewMap.isEmpty()) {
            return;
        }

        for (TextView view : views) {
            view.removeTextChangedListener(this);
            mViewMap.remove(view);
        }
        // 触发一次监听
        notifyChanged();
    }

    /**
     * 移除所有 TextView 监听，避免内存泄露
     */
    public void removeAllViews() {
        if (mViewMap == null) {
            return;
        }

        for (TextView view : mViewMap.keySet()) {
            view.removeTextChangedListener(this);
        }
        mViewMap.clear();
        mViewMap = null;
    }

    /**
     * 通知更新
     */
    public void notifyChanged() {
        if (mViewMap == null) {
            return;
        }

        // 重新遍历所有的输入
        for (Map.Entry<TextView, Predicate<CharSequence>> entry : mViewMap.entrySet()) {
            if (!entry.getValue().test(entry.getKey().getText())) {
                if (mListener != null) {
                    mListener.onTextEnable(false);
                } else {
                    setEnabled(false);
                }
                return;
            }
        }

        if (mListener != null) {
            mListener.onTextEnable(true);
        } else {
            setEnabled(true);
        }

    }

    /**
     * 设置 View 的事件
     *
     * @param enabled 启用或者禁用 View 的事件
     */
    public void setEnabled(boolean enabled) {
        if (mView == null) {
            return;
        }
        if (enabled == mView.isEnabled()) {
            return;
        }

        if (enabled) {
            //启用View的事件
            mView.setEnabled(true);
            if (mAlpha) {
                //设置不透明
                mView.setAlpha(1f);
            }
        } else {
            //禁用View的事件
            mView.setEnabled(false);
            if (mAlpha) {
                //设置半透明
                mView.setAlpha(0.5f);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        notifyChanged();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        removeAllViews();
    }

    public interface OnInputTextListener {
        void onTextEnable(boolean enable);
    }
}