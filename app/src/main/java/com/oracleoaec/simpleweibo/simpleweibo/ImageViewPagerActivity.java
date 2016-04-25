package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.RotateDownTransformer;
import com.ToxicBakery.viewpager.transforms.ZoomInTransformer;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.BlogImageAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.BlogImageViewPagerAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;
import com.oracleoaec.simpleweibo.simpleweibo.enity.BlogImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ycy on 16-4-22.
 */
public class ImageViewPagerActivity extends AppCompatActivity
{
    private List<View> viewList;
    private ViewPager viewPager;
    private List<BlogImage> blogImages;
    private RequestQueue imageQueue;
    private int blog_id;
    private BlogImageViewPagerAdapter adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.viewpaper_activity);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        Intent intent = getIntent();
        blog_id = intent.getIntExtra("blog_id", 0);
        viewPager = (ViewPager) findViewById(R.id.image_viewpager);
        imageQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HomePartent.getUrl() + "user/getblogimage/" + blog_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    blogImages = new ArrayList<BlogImage>();
                    JSONArray imagearray = new JSONArray(jsonArray.toString());
                    for (int i = 0; i <imagearray.length() ; i++)
                    {
                        JSONObject jsobj = jsonArray.getJSONObject(i);
                        String blog_image = jsobj.getString("blog_image");
                        int image_id = jsobj.getInt("id");
                        blogImages.add(new BlogImage(image_id,blog_image));
                    }
                    viewList = new ArrayList<View>();
                    for (int i = 0; i <blogImages.size() ; i++)
                    {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.imagepager_layout,null);
                        ImageView iv_blogimage = (ImageView) view.findViewById(R.id.iv_blogimage_viewpager);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        imageLoader.displayImage(blogImages.get(i).getBlogImageUrl(), iv_blogimage);
                        TextView thisnum = (TextView) view.findViewById(R.id.tv_thisnum);
                        TextView allnum = (TextView) view.findViewById(R.id.tv_allnum);
                        thisnum.setText(String.valueOf(i+1));
                        allnum.setText(String.valueOf(blogImages.size()));
                        viewList.add(view);
                    }
                    adpter = new BlogImageViewPagerAdapter(viewList);
                    viewPager.setAdapter(adpter);
                    viewPager.setPageTransformer(true,new ZoomInTransformer());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        imageQueue.add(jsonArrayRequest);
    }
}
