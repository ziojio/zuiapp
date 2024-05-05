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
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;

import javax.inject.Inject;

import androidz.LoadingDialog;
import composex.ui.ComposeActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
import uiapp.databinding.ActivityHomeBinding;
import uiapp.databinding.ItemHomeFunBinding;
import uiapp.di.MainHandler;
import uiapp.ui.activity.AnimationActivity;
import uiapp.ui.activity.DataBaseActivity;
import uiapp.ui.activity.RxJavaActivity;
import uiapp.ui.activity.WebSocketActivity;
import uiapp.ui.adapter.BaseAdapter;
import uiapp.ui.adapter.BindingViewHolder;
import uiapp.ui.base.BaseFragment;
import uiapp.ui.databinding.DataBindingActivity;
import uiapp.ui.edit.EditActivity;
import uiapp.ui.http.HttpActivity;
import uiapp.ui.ktx.KotlinActivity;
import uiapp.ui.paging.Paging3Activity;
import uiapp.util.KeyboardWatcher;
import uiapp.web.WebActivity;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment {
    private static final String execute = "execute";
    private static final String popup = "popup";
    private static final String snackbar = "snackbar";
    private static final String webview = "webview";
    private static final String database = "database";
    private static final String dialog = "dialog";
    private static final String compose = "compose";
    private static final String http = "http";
    private static final String ktx = "ktx";
    private static final String animation = "animation";
    private static final String dataBinding = "dataBinding";
    private static final String edit = "edit";
    private static final String rxJava = "rxJava";
    private static final String paging = "paging";
    private static final String webSocket = "webSocket";

    @Inject
    MainHandler mHandler;
    HomePopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ActivityHomeBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.bind(view);

        String[] strings = new String[]{
                execute, snackbar, popup,
                database, dialog, compose,
                animation, dataBinding, edit,
                http, ktx, rxJava,
                webview, paging, webSocket
        };
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerview.setAdapter(new BaseAdapter<String, BindingViewHolder<ItemHomeFunBinding>>(strings) {

            @NonNull
            @Override
            public BindingViewHolder<ItemHomeFunBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                return new BindingViewHolder<>(ItemHomeFunBinding.inflate(inflater, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull BindingViewHolder<ItemHomeFunBinding> holder, int position) {
                String cmd = getItem(position);
                TextView textView = holder.binding.execute;
                textView.setText(cmd);
                textView.setOnClickListener(v -> {
                    switch (cmd) {
                        case execute -> {
                            Timber.d("execute");
                            textView.setText(android.R.string.ok);
                            textView.setText(androidz.R.string.loading_message);
                            logBuildInfo();
                        }
                        case snackbar -> {
                            showSnackbar(textView);
                        }
                        case popup -> {
                            if (popupWindow == null) {
                                popupWindow = new HomePopupWindow(requireActivity());
                            }
                            popupWindow.showPopupWindow(holder.itemView);
                            // popupWindow.showPopupWindow(100, 100);
                        }
                        case webview -> {
                            startActivity(new Intent(requireActivity(), WebActivity.class));
                        }
                        case database -> {
                            startActivity(new Intent(requireActivity(), DataBaseActivity.class));
                        }
                        case dialog -> {
                            new LoadingDialog(requireActivity()).show();
                        }
                        case compose -> {
                            startActivity(new Intent(requireActivity(), ComposeActivity.class));
                        }
                        case http -> {
                            startActivity(new Intent(requireActivity(), HttpActivity.class));
                        }
                        case ktx -> {
                            startActivity(new Intent(requireActivity(), KotlinActivity.class));
                        }
                        case animation -> {
                            startActivity(new Intent(requireActivity(), AnimationActivity.class));
                        }
                        case dataBinding -> {
                            startActivity(new Intent(requireActivity(), DataBindingActivity.class));
                        }
                        case edit -> {
                            startActivity(new Intent(requireActivity(), EditActivity.class));
                        }
                        case rxJava -> {
                            startActivity(new Intent(requireActivity(), RxJavaActivity.class));
                        }
                        case paging -> {
                            startActivity(new Intent(requireActivity(), Paging3Activity.class));
                        }
                        case webSocket -> {
                            startActivity(new Intent(requireActivity(), WebSocketActivity.class));
                        }
                    }
                });
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

