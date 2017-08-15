package com.android.mystock.common.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;//数据源
        this.mItemLayoutId = itemLayoutId;//item布局
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<T> list) {
        this.mDatas = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);//将getView方法分离出来
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();//返回相应的item的view

    }

    public abstract void convert(ViewHolder helper, T item);//抽象方法:获得相应的item的view

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);//获得对应的view
    }

}
