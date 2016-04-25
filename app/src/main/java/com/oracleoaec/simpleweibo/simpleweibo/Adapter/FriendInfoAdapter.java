package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.CommonAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.ViewHolder;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Friend;

import java.util.List;

/**
 * Created by ycy on 16-4-22.
 */
public class FriendInfoAdapter extends CommonAdapter {
    public FriendInfoAdapter(Context context, List mDatas) {
        super(context, mDatas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = ViewHolder.get(context,view,viewGroup, R.layout.friendinfo_layout,i);
        Friend friend = (Friend) mdatas.get(i);
        TextView friendname = viewHolder.getView(R.id.tv_friend_name);
        ImageView imageView = viewHolder.getView(R.id.iv_friend_photo);
        friendname.setText(friend.getFriendname());
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(friend.getFriendphoto(),imageView);
        return viewHolder.getConvertView();
    }
}
