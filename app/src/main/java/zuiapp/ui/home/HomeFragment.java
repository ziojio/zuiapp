package zuiapp.ui.home;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidz.app.AppFragment;
import androidz.dialog.LoadingDialog;
import androidz.util.OnDebouncingClickListener;
import composex.ui.ComposeActivity;
import timber.log.Timber;
import zuiapp.databinding.ActivityHomeBinding;
import zuiapp.ui.activity.AnimationActivity;
import zuiapp.ui.activity.HttpActivity;
import zuiapp.ui.activity.MaterialActivity;
import zuiapp.ui.activity.PagerActivity;
import zuiapp.ui.activity.RxJavaActivity;
import zuiapp.ui.activity.ViewActivity;
import zuiapp.ui.edit.EditActivity;
import zuiapp.ui.web.WebViewActivity;
import zuiapp.util.DebugLifecycleObserver;

public class HomeFragment extends AppFragment implements View.OnClickListener {
    private ActivityHomeBinding binding;
    private PendingIntent pendingIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new DebugLifecycleObserver());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onBinding();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onBinding() {
        binding.execFunction.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                Timber.d("execFunction");


                Intent intent = new Intent(requireActivity(), ViewActivity.class);
                intent.putExtra("key" + new Random().nextInt(100), new Random().nextInt());
                Bundle bundle = intent.getExtras();
                bundle.get("");
                Timber.d(bundle.toString());
                pendingIntent = PendingIntent.getActivity(requireActivity(), 1, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

                // FragmentContainerActivity.show(requireActivity(), new FuncFragment());

                // Intent intent = new Intent(requireActivity(), HummerActivity.class);
                // intent.putExtra(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL, new NavPage("http://10.1.88.195:8000/index.js"));
                // startActivity(intent);

                // startActivity(new Intent(requireActivity(), Camera2Activity.class));
            }
        });
        binding.intent.setOnClickListener(new OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                try {
                    Timber.d("pendingIntent: " + pendingIntent);
                    if (pendingIntent != null) {
                        Intent intent = new Intent(requireActivity(), ViewActivity.class);
                        intent.putExtra("keykey", new Random().nextInt());

                        pendingIntent.send(requireActivity(), 2, intent);
                    }
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.snackbar.setOnClickListener(this::showSnackbar);

        binding.webview.setOnClickListener(this);
        binding.dialog.setOnClickListener(this);
        binding.compose.setOnClickListener(this);
        binding.http.setOnClickListener(this);
        binding.animation.setOnClickListener(this);
        binding.dataBinding.setOnClickListener(this);
        binding.view.setOnClickListener(this);
        binding.edit.setOnClickListener(this);
        binding.viewpager.setOnClickListener(this);
        binding.rxJava.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.webview) {
            startActivity(new Intent(requireContext(), WebViewActivity.class));
        } else if (v == binding.http) {
            startActivity(new Intent(requireContext(), HttpActivity.class));
        } else if (v == binding.dataBinding) {
            startActivity(new Intent(requireContext(), MaterialActivity.class));
        } else if (v == binding.viewpager) {
            startActivity(new Intent(requireContext(), PagerActivity.class));
        } else if (v == binding.animation) {
            startActivity(new Intent(requireContext(), AnimationActivity.class));
        } else if (v == binding.view) {
            startActivity(new Intent(requireContext(), ViewActivity.class));
        } else if (v == binding.compose) {
            startActivity(new Intent(requireContext(), ComposeActivity.class));
        } else if (v == binding.rxJava) {
            startActivity(new Intent(requireContext(), RxJavaActivity.class));
        } else if (v == binding.edit) {
            startActivity(new Intent(requireContext(), EditActivity.class));
        } else if (v == binding.dialog) {
            // ViewBindingDialog dialog = new ViewBindingDialog();
            LoadingDialog dialog = new LoadingDialog();
            dialog.show(getParentFragmentManager(), "123");
        }
    }

    private void showSnackbar(View v) {
        Timber.d("showSnackbar ");
        Snackbar.make(v, "Snackbar", Snackbar.LENGTH_SHORT)
                .setAction("Ok", v1 -> showToast("Snackbar Ok"))
                .setAnchorView(v)
                .show();
    }

    private void doFun() {
        String str = "A";
        string_info(str);
        str = "&";
        string_info(str);
        str = "℃";
        string_info(str);
        str = "℉";
        string_info(str);
        str = "好";
        string_info(str);
        str = "个";
        string_info(str);
        str = "鞞";
        string_info(str);
        str = "👌";
        string_info(str);
        str = "🀄";
        string_info(str);
    }

    private void string_info(String str) {
        Timber.d("------------------------------------------");
        Timber.d("str " + str);
        Timber.d("str.length " + str.length());
        Timber.d("str.bytes " + str.getBytes().length);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Timber.d("str.codePoints " + str.codePoints().findFirst().getAsInt());
            Timber.d("str.codePoints hex " + Integer.toHexString(str.codePoints().findFirst().getAsInt()));
        }
        Timber.d("str.CharArray " + Arrays.toString(str.toCharArray()));
        Timber.d("str.CharArray " + str.toCharArray().length);
    }

}

