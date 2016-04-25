package com.oracleoaec.simpleweibo.simpleweibo.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * Created by ycy on 16-4-13.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater layoutInflater;
    protected Context context;
    protected List<T> mdatas;
    public CommonAdapter(Context context, List<T> mDatas)
    {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.mdatas = mDatas;
    }
    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mdatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

}
