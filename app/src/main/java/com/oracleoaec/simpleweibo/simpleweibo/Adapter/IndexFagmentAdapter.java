package com.oracleoaec.simpleweibo.simpleweibo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oracleoaec.simpleweibo.simpleweibo.fagment.FagmentGetAll;
import com.oracleoaec.simpleweibo.simpleweibo.fagment.FagmentGetFriends;
import com.oracleoaec.simpleweibo.simpleweibo.fagment.FagmentGetMyInfo;

/**
 * Created by ycy on 16-4-12.
 */
public class IndexFagmentAdapter extends FragmentPagerAdapter {
    protected String[] title = {"广场","朋友圈","我"};
    protected FagmentGetAll fagmentGetAll;
    protected FagmentGetFriends fagmentGetFriends;
    protected FagmentGetMyInfo fagmentGetMyInfo;
    public IndexFagmentAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                fagmentGetAll = new FagmentGetAll();
                return fagmentGetAll;
            case 1:
                fagmentGetFriends = new FagmentGetFriends();
                return fagmentGetFriends;
            case 2:
                fagmentGetMyInfo = new FagmentGetMyInfo();
                return fagmentGetMyInfo;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
