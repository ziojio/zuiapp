package com.ziojio.zuiapp.ui.activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.ziojio.zuiapp.R;
import com.ziojio.zuiapp.databinding.ActivityPagerBinding;
import com.ziojio.zuiapp.databinding.ItemViewholderBinding;

import java.util.ArrayList;
import java.util.List;

import androidz.recyclerview.BaseAdapter;
import androidz.recyclerview.BaseViewHolder;

public class PagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPagerBinding binding = ActivityPagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        adapter.submitList(list);
        binding.viewpager.setAdapter(adapter);
        binding.dots.attachTo(binding.viewpager);
    }

    static class ViewPagerAdapter extends BaseAdapter<String> {
        @Override
        protected int getLayoutRes(int viewType) {
            return R.layout.item_viewholder;
        }

        @Override
        protected void onBindItem(String s, BaseViewHolder holder, int position) {
            ItemViewholderBinding binding = ItemViewholderBinding.bind(holder.itemView);
            Glide.with(holder.itemView).load(s).error(R.mipmap.ic_launcher).into(binding.image);
        }
    }

}
