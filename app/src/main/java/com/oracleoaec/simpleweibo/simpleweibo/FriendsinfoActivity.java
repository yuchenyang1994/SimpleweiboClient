package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.FriendInfoAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycy on 16-4-22.
 */
public class FriendsinfoActivity extends AppCompatActivity
{
    private SwipeMenuListView friendlistview;
    private RequestQueue friendlistQueue;
    private List<Friend> friendlist;
    private FriendInfoAdapter friendadpter;
    private int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("好友管理");
        setContentView(R.layout.friendinfo_activity);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        SharedPreferences pref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = pref.getInt("user_id",0);
        friendlistview = (SwipeMenuListView) findViewById(R.id.iv_friends_info);
        initfriendlist();
        setfriendlistmenu();


    }
    private void initfriendlist()
    {
        friendlistQueue = Volley.newRequestQueue(FriendsinfoActivity.this);
        friendlist = new ArrayList<Friend>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HomePartent.getUrl() + "users/getfriends/" + user_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                for (int i = 0; i < jsonArray.length() ; i++)
                {

                    try {
                        JSONObject jsobj = jsonArray.getJSONObject(i);
                        String username = jsobj.getString("username");
                        String photo = jsobj.getString("photo");
                        int id = jsobj.getInt("id");
                        Friend friend = new Friend(photo,username,id);
                        friendlist.add(friend);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                friendadpter = new FriendInfoAdapter(FriendsinfoActivity.this,friendlist);
                friendlistview.setAdapter(friendadpter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        friendlistQueue.add(jsonArrayRequest);
    }
    private void setfriendlistmenu()
    {
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openitem = new SwipeMenuItem(getApplicationContext());
                openitem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                openitem.setWidth(200);
                openitem.setTitle("删除");
                openitem.setTitleSize(18);
                openitem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openitem);
            }
        };
        friendlistview.setMenuCreator(swipeMenuCreator);
        friendlistview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Friend friend = (Friend) friendadpter.getItem(position);
                        Map<String,Object> map = new HashMap<String, Object>();
                        map.put("user_id", user_id);
                        map.put("friends_id", friend.getFriend_id());
                        JSONObject jsonObject = new JSONObject(map);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl() + "users/removefriend", jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                friendadpter.notifyDataSetChanged();
                                initfriendlist();
                                Toast.makeText(FriendsinfoActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(FriendsinfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                        friendlistQueue.add(jsonObjectRequest);

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
}
