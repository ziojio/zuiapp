package zuiapp.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import zuiapp.databinding.DialogInputNumberBinding;

public class InputMethodDialog2 extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogInputNumberBinding binding = DialogInputNumberBinding.inflate(inflater, container, false);
        binding.gridlayout.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        return binding.getRoot();
    }

}
