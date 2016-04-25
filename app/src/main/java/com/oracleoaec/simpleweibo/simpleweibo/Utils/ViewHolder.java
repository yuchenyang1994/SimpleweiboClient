package com.oracleoaec.simpleweibo.simpleweibo.Utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ycy on 16-4-13.
 */
public class ViewHolder {
    protected final SparseArray<View> mviews;
    protected View mConverView;

    private ViewHolder(Context context, ViewGroup parent, int layoutid, int position)
    {
        this.mviews = new SparseArray<View>();
        mConverView = LayoutInflater.from(context).inflate(layoutid, parent,
                false);
        //setTag
        mConverView.setTag(this);
    }

    public static ViewHolder get(Context context,View converView,ViewGroup parent,int laoyoutid,int postion){
        if (converView == null)
        {
            return new ViewHolder(context, parent, laoyoutid, postion);
        }
        return (ViewHolder) converView.getTag();
    }

    public <T extends View> T getView(int viewId)
    {

        View view = mviews.get(viewId);
        if (view == null)
        {
            view = mConverView.findViewById(viewId);
            mviews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView()
    {
        return mConverView;
    }
}
