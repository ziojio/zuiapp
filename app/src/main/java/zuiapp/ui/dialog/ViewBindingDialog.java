package zuiapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidz.app.AppDialog;
import zuiapp.databinding.DialogBindingBinding;
import zuiapp.util.DebugLifecycleObserver;

public class ViewBindingDialog extends AppDialog {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new DebugLifecycleObserver());
    }

    @Override
    public View createView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogBindingBinding binding = DialogBindingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


}
