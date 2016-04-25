package com.oracleoaec.simpleweibo.simpleweibo;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.IndexFagmentAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by ycy on 16-4-12.
 */
public class IndexActivity extends AppCompatActivity {
    protected ViewPager viewPager;
    protected PagerSlidingTabStrip pagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.index_name);
            supportActionBar.openOptionsMenu();
        }
        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
        if (pref.getInt("user_id",0)==0){
            Intent intent = new Intent("login");
            startActivity(intent);
        }
        setContentView(R.layout.activity_index);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        initview();
        inittab();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_writeblog:
                Intent intent = new Intent("writeblog");
                startActivity(intent);
                break;
            case R.id.menu_setting:
                Intent intent1 = new Intent("friendinfo");
                startActivity(intent1);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    private void inittab() {
        viewPager.setAdapter(new IndexFagmentAdapter(getSupportFragmentManager()));
        pagers.setShouldExpand(true);
        pagers.setBackgroundResource(R.color.colorPrimary);
        pagers.setIndicatorColorResource(R.color.Indent);
        pagers.setTextColorResource(R.color.white);
        pagers.setIndicatorHeight(10);
        pagers.setViewPager(viewPager);
    }

    private void initview() {
        viewPager = (ViewPager) findViewById(R.id.index_viewpaper);
        pagers = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    }

}
