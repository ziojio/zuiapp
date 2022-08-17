package uiapp.ui.base;

import android.widget.Toast;

import androidz.app.AppFragment;

public class BaseFragment extends AppFragment {

    protected void showToast(String msg) {
        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show());
    }

}