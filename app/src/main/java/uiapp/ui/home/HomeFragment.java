package uiapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidz.app.LoadingDialog;
import androidz.util.ClickUtil;
import androidz.util.OnDebouncingClickListener;
import composex.ui.ComposeActivity;
import timber.log.Timber;
import uiapp.R;
import uiapp.databinding.ActivityHomeBinding;
import uiapp.ui.activity.AnimationActivity;
import uiapp.ui.activity.DataBaseActivity;
import uiapp.ui.activity.RxJavaActivity;
import uiapp.ui.activity.WebSocketActivity;
import uiapp.ui.base.BaseFragment;
import uiapp.ui.databinding.DataBindingActivity;
import uiapp.ui.edit.EditActivity;
import uiapp.ui.http.HttpActivity;
import uiapp.ui.paging.Paging3Activity;
import uiapp.util.DebugLifecycleObserver;
import uiapp.web.WebActivity;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new DebugLifecycleObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ActivityHomeBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.bind(view);

        Arrays.asList(binding.snackbar, binding.webview,
                binding.database, binding.dialog,
                binding.compose, binding.http,
                binding.animation, binding.dataBinding,
                binding.edit, binding.rxJava,
                binding.webSocket).forEach(v -> v.setOnClickListener(this));

        binding.execFunction.setOnClickListener((OnDebouncingClickListener) v -> {
            Timber.d("execFunction");
            // logBuildInfo();
            startActivity(new Intent(requireContext(), Paging3Activity.class));
        });
        binding.edit.setOnClickListener((OnDebouncingClickListener) v -> {
            binding.keyboard
                    .setEditText(binding.editText)
                    .setNineGridNumber()
                    .show();
        });
        binding.popup.setOnClickListener(new View.OnClickListener() {
            HomePopupWindow popupWindow;

            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    popupWindow = new HomePopupWindow(requireContext());
                }
                popupWindow.showPopupWindow(v);
                // popupWindow.showPopupWindow(100, 100);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!ClickUtil.isSingleClick(v)) {
            return;
        }
        Context context = requireContext();
        int id = v.getId();
        if (id == R.id.webview) {
            startActivity(new Intent(context, WebActivity.class));
        } else if (id == R.id.compose) {
            startActivity(new Intent(context, ComposeActivity.class));
        } else if (id == R.id.database) {
            startActivity(new Intent(context, DataBaseActivity.class));
        } else if (id == R.id.http) {
            startActivity(new Intent(context, HttpActivity.class));
        } else if (id == R.id.webSocket) {
            startActivity(new Intent(context, WebSocketActivity.class));
        } else if (id == R.id.dataBinding) {
            startActivity(new Intent(context, DataBindingActivity.class));
        } else if (id == R.id.animation) {
            startActivity(new Intent(context, AnimationActivity.class));
        } else if (id == R.id.rxJava) {
            startActivity(new Intent(context, RxJavaActivity.class));
        } else if (id == R.id.edit) {
            startActivity(new Intent(context, EditActivity.class));
        } else if (id == R.id.snackbar) {
            showSnackbar(v);
        } else if (id == R.id.dialog) {
            new LoadingDialog(requireActivity()).show();
        }
    }

    private void showSnackbar(View v) {
        Timber.d("showSnackbar ");
        Snackbar.make(v, "Snackbar", Snackbar.LENGTH_SHORT)
                // .setBackgroundTint(requireContext().getColor(R.color.white))
                // .setTextColor(requireContext().getColor(R.color.deep_purple_100))
                .setAction("Ok", v1 -> showToast("Snackbar Ok"))
                .setAnchorView(v)
                .show();
    }

    private void logBuildInfo() {
        Timber.d("Build: ");
        String ver = System.getProperty("java.vm.version");
        Timber.d("java.vm.version: " + ver);
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                Timber.d(field.getName() + ": " + field.get(null));
            }
            Timber.d("Build.VERSION: ");
            fields = Build.VERSION.class.getDeclaredFields();
            for (Field field : fields) {
                Timber.d(field.getName() + ": " + field.get(null));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}

