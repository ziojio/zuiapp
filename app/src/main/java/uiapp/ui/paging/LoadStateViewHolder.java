package uiapp.ui.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import uiapp.R;
import uiapp.databinding.HolderLoadStateBinding;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

class LoadStateViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar mProgressBar;
    private TextView mErrorMsg;
    private Button mRetry;

    LoadStateViewHolder(@NonNull ViewGroup parent, @NonNull View.OnClickListener retryCallback) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_load_state, parent, false));

        HolderLoadStateBinding binding = HolderLoadStateBinding.bind(itemView);
        mProgressBar = binding.progressBar;
        mErrorMsg = binding.errorMsg;
        mRetry = binding.retryButton;
        mRetry.setOnClickListener(retryCallback);
    }

    public void bind(LoadState loadState) {
        if (loadState instanceof LoadState.Error) {
            LoadState.Error loadStateError = (LoadState.Error) loadState;
            mErrorMsg.setText(loadStateError.getError().getLocalizedMessage());
        }
        mProgressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
        mRetry.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        mErrorMsg.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
    }
}