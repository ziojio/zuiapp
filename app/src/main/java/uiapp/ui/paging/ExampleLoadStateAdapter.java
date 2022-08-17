package uiapp.ui.paging;

import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;

class ExampleLoadStateAdapter extends LoadStateAdapter<LoadStateViewHolder> {
    private View.OnClickListener mRetryCallback;

    ExampleLoadStateAdapter(View.OnClickListener retryCallback) {
        mRetryCallback = retryCallback;
    }

    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent, @NotNull LoadState loadState) {
        return new LoadStateViewHolder(parent, mRetryCallback);
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder holder, @NotNull LoadState loadState) {
        holder.bind(loadState);
    }
}