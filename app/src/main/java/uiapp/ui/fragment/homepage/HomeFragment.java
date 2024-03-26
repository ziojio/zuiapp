package uiapp.ui.fragment.homepage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.inject.Inject;

import androidz.app.LoadingDialog;
import composex.ui.ComposeActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
import uiapp.R;
import uiapp.databinding.ActivityHomeBinding;
import uiapp.di.MainHandler;
import uiapp.ui.activity.AnimationActivity;
import uiapp.ui.activity.DataBaseActivity;
import uiapp.ui.activity.KotlinActivity;
import uiapp.ui.activity.RxJavaActivity;
import uiapp.ui.activity.WebSocketActivity;
import uiapp.ui.base.BaseFragment;
import uiapp.ui.databinding.DataBindingActivity;
import uiapp.ui.edit.EditActivity;
import uiapp.ui.http.HttpActivity;
import uiapp.ui.paging.Paging3Activity;
import uiapp.util.KeyboardWatcher;
import uiapp.web.WebActivity;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Inject
    MainHandler mHandler;

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
                binding.compose, binding.http, binding.ktx,
                binding.animation, binding.dataBinding,
                binding.edit, binding.rxJava, binding.paging,
                binding.webSocket).forEach(v -> v.setOnClickListener(this));

        binding.execFunction.setOnClickListener(v -> {
            Timber.d("execFunction");
            logBuildInfo();

            if (v instanceof TextView tv) {
                tv.append("...");
            }
        });
        binding.edit.setOnClickListener(v -> {
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
                    popupWindow = new HomePopupWindow(requireActivity());
                }
                popupWindow.showPopupWindow(v);
                // popupWindow.showPopupWindow(100, 100);
            }
        });

        KeyboardWatcher.with(requireActivity()).setListener(new KeyboardWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {
                Timber.d("onSoftKeyboardOpened: keyboardHeight=" + keyboardHeight);
            }

            @Override
            public void onSoftKeyboardClosed() {
                Timber.d("onSoftKeyboardClosed");
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.webview) {
            startActivity(new Intent(requireActivity(), WebActivity.class));
        } else if (id == R.id.compose) {
            startActivity(new Intent(requireActivity(), ComposeActivity.class));
        } else if (id == R.id.ktx) {
            startActivity(new Intent(requireActivity(), KotlinActivity.class));
        } else if (id == R.id.database) {
            startActivity(new Intent(requireActivity(), DataBaseActivity.class));
        } else if (id == R.id.http) {
            startActivity(new Intent(requireActivity(), HttpActivity.class));
        } else if (id == R.id.webSocket) {
            startActivity(new Intent(requireActivity(), WebSocketActivity.class));
        } else if (id == R.id.dataBinding) {
            startActivity(new Intent(requireActivity(), DataBindingActivity.class));
        } else if (id == R.id.animation) {
            startActivity(new Intent(requireActivity(), AnimationActivity.class));
        } else if (id == R.id.rxJava) {
            startActivity(new Intent(requireActivity(), RxJavaActivity.class));
        } else if (id == R.id.edit) {
            startActivity(new Intent(requireActivity(), EditActivity.class));
        } else if (id == R.id.paging) {
            startActivity(new Intent(requireActivity(), Paging3Activity.class));
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
            Timber.e(e);
        }
    }

}

