package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.CommonAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.ViewHolder;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Blog;

import java.util.List;

/**
 * Created by ycy on 16-4-13.
 */
public class WeiBoAdapter<T> extends CommonAdapter<T> {

    public WeiBoAdapter(Context context, List<T> mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = ViewHolder.get(context,view,viewGroup, R.layout.getall_layout,i);
        Blog blog = (Blog) mdatas.get(i);
        TextView user_name = viewHolder.getView(R.id.user_name);
        ImageView imageView = viewHolder.getView(R.id.user_photo);
        TextView user_says = viewHolder.getView(R.id.user_says);
        TextView user_weibo = viewHolder.getView(R.id.user_blog);
        TextView issutime = viewHolder.getView(R.id.blog_issutime);
        user_name.setText(blog.getUsername());
        user_weibo.setText(blog.getUserblog());
        issutime.setText(blog.getIssutime());
        String userphoto = blog.getUserphoto();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(userphoto,imageView);
        return viewHolder.getConvertView();
    }
}
