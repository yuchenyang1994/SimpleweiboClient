package com.oracleoaec.simpleweibo.simpleweibo.fagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.WeiBoAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.BlogMoreActivity;
import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.ReFlushListView;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Blog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycy on 16-4-12.
 */
public class FagmentGetFriends extends Fragment implements AdapterView.OnItemClickListener {
    protected ReFlushListView listView;
    protected List<Blog> bloglist;
    protected RequestQueue requestQueue;
    protected Context context;
    private WeiBoAdapter<Blog> weiBoAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();
        bloglist = new ArrayList<Blog>();

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fagment_getfriend,container,false);
        listView = (ReFlushListView) view.findViewById(R.id.fagment_getfriend_listview);
        initRequest();
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(new ReFlushListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        bloglist.clear();
                        initRequest();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        weiBoAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }
                }.execute(null, null, null);
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Blog blog = (Blog) weiBoAdapter.getItem(i-1);
        int blog_id = blog.getBlog_id();
        Intent intent = new Intent(getActivity(), BlogMoreActivity.class);
        intent.putExtra("blog_id", blog_id);
        startActivity(intent);
    }
    private void initRequest()
    {
        SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        int user_id = pref.getInt("user_id", 0);
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("user_id", user_id);
        final JSONObject jsonb = new JSONObject(map);
        Log.d("user",jsonb.toString());
        requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HomePartent.getUrl() + "users/getfriendblogs/" + user_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    JSONArray array = new JSONArray(jsonArray.toString());
                    for (int i = 0; i <array.length() ; i++)
                    {
                        JSONObject object = array.getJSONObject(i);
                        String username = object.getString("user_name");
                        String content = object.getString("content");
                        String issueTime = object.getString("issueTime");
                        String photo = object.getString("user_photo");
                        int blog_id = object.getInt("id");
                        int user_id = object.getInt("user_id");
                        Blog blog = new Blog(issueTime,username,content,user_id);
                        blog.setBlog_id(blog_id);
                        blog.setUserphoto(photo);
                        Log.d("blog",blog.toString());
                        bloglist.add(blog);
                    }
                    Collections.reverse(bloglist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                weiBoAdapter = new WeiBoAdapter<Blog>(context,bloglist);
                listView.setAdapter(weiBoAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context,"请求出错",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }
}
