package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.oracleoaec.simpleweibo.simpleweibo.enity.BlogImage;

import java.util.List;

/**
 * Created by ycy on 16-4-22.
 */
public class BlogImageViewPagerAdapter extends PagerAdapter {
    private List<View> blogImages;


    public BlogImageViewPagerAdapter(List<View> blogImages) {
        this.blogImages = blogImages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(blogImages.get(position));
        return blogImages.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if (blogImages!=null&&blogImages.size()>0){
            return blogImages.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
