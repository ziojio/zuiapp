package uiapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<MODEL> extends RecyclerView.Adapter<BaseViewHolder> {
    protected final ArrayList<MODEL> mList;
    protected OnItemClickListener<MODEL> mOnItemClickListener;
    protected OnItemLongClickListener<MODEL> mOnItemLongClickListener;

    public BaseAdapter() {
        this.mList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener<MODEL> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<MODEL> onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void add(MODEL model) {
        mList.add(model);
        notifyItemInserted(mList.size() - 1);
    }

    public void remove(MODEL model) {
        int index = mList.indexOf(model);
        if (index >= 0) {
            mList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public boolean contains(MODEL model) {
        return mList.contains(model);
    }

    public MODEL getItem(int position) {
        return mList.get(position);
    }

    /**
     * 修改列表后，需要调用 notifyXXX 方法
     *
     * @see #notifyItemInserted(int)
     * @see #notifyItemRemoved(int)
     * @see #notifyItemChanged(int)
     */
    public List<MODEL> getList() {
        return mList;
    }

    public void submitList(@Nullable List<MODEL> list) {
        if (list == mList) return;
        int count = mList.size();
        if (count != 0) {
            mList.clear();
            notifyItemRangeRemoved(0, count);
        }
        if (list != null) {
            mList.addAll(list);
            notifyItemRangeInserted(0, mList.size());
        }
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

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M> {
        void onItemLongClick(M item, int position);
    }

}