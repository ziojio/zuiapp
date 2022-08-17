package uiapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseListAdapter<MODEL> extends ListAdapter<MODEL, BaseViewHolder> {
    protected OnItemClickListener<MODEL> mOnItemClickListener;
    protected OnItemLongClickListener<MODEL> mOnItemLongClickListener;

    public BaseListAdapter(@NonNull DiffUtil.ItemCallback<MODEL> diffCallback) {
        super(diffCallback);
    }

    public void setOnItemClickListener(OnItemClickListener<MODEL> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<MODEL> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void add(MODEL model) {
        List<MODEL> list = new ArrayList<>(getList());
        list.add(model);
        submitList(list);
    }

    public void remove(MODEL model) {
        if (getCurrentList().contains(model)) {
            List<MODEL> list = new ArrayList<>(getList());
            list.remove(model);
            submitList(list);
        }
    }

    public boolean contains(MODEL model) {
        return getList().contains(model);
    }

    public List<MODEL> getList() {
        return getCurrentList();
    }

    /**
     * 当外部持有列表更新时使用此方法，而不是 {@link #submitList(List)}
     */
    public void refreshList(@Nullable List<MODEL> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    protected abstract @LayoutRes
    int getLayoutRes(int viewType);

    protected abstract void onBindItem(MODEL model, BaseViewHolder holder, int position);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(viewType), parent, false);
        BaseViewHolder holder = new BaseViewHolder(view);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemClickListener.onItemClick(getItem(position), position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null) {
                int position = holder.getBindingAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(getItem(position), position);
                return true;
            }
            return false;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        onBindItem(getItem(position), holder, position);
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }

}