package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.CommonAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.ViewHolder;
import com.oracleoaec.simpleweibo.simpleweibo.enity.BlogImage;

import java.util.List;

/**
 * Created by ycy on 16-4-21.
 */
public class BlogImageAdapter extends CommonAdapter {
    public BlogImageAdapter(Context context, List mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = ViewHolder.get(context, view, viewGroup, R.layout.blogimage_layout, i);
        if (mdatas!=null){
            BlogImage blogImage = (BlogImage) mdatas.get(i);
            ImageView blogimageView = viewHolder.getView(R.id.iv_blogImage);
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(blogImage.getBlogImageUrl(), blogimageView);
            return viewHolder.getConvertView();
        }
        return null;

    }
}
