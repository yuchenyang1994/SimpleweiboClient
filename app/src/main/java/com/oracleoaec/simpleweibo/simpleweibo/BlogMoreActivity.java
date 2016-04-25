package com.oracleoaec.simpleweibo.simpleweibo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.AnswerAdpter;
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.BlogImageAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.TickButton;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Answers;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Blog;
import com.oracleoaec.simpleweibo.simpleweibo.enity.BlogImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycy on 16-4-15.
 */
public class BlogMoreActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    protected static final int Update_text = 1;
    protected ListView answerlistview;
    protected ImageView userphotoview;
    protected TextView tv_username;
    protected TextView tv_blogcontent;
    protected EditText editText;
    protected Button btn_addanswer;
    protected View btn_addfriend;
    protected GridView gridView_blog_image;
    protected Handler handler;
    protected RequestQueue requestQueue;
    protected List<Answers> answersList;
    protected int blog_id;
    protected int friends_id;
    protected int user_id;
    protected String userphotourl;
    protected AnswerAdpter answerAdpter;
    protected List<BlogImage> blogImageData;
    protected RequestQueue imageQueue;
    protected TickButton tickButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("微博详情");
        setContentView(R.layout.activivity_blog_more);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        Intent intent = getIntent();
        blog_id = intent.getIntExtra("blog_id", 0);
        SharedPreferences pref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = pref.getInt("user_id", 0);
        initview();
        registerlistener();
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        initmessage();
                        initrequestAnswer();
                    }
                });

            }
        }).start();
        imageQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HomePartent.getUrl() + "user/getblogimage/" + blog_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    blogImageData = new ArrayList<BlogImage>();
                    JSONArray imagearray = new JSONArray(jsonArray.toString());
                    for (int i = 0; i <imagearray.length() ; i++)
                    {
                        JSONObject jsobj = jsonArray.getJSONObject(i);
                        String blog_image = jsobj.getString("blog_image");
                        int image_id = jsobj.getInt("id");
                        blogImageData.add(new BlogImage(image_id,blog_image));
                    }
                    gridView_blog_image.setAdapter(new BlogImageAdapter(BlogMoreActivity.this,blogImageData));
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

    private void initrequestAnswer() {
//        answerQueue = Volley.newRequestQueue(this);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(HomePartent.getUrl() + "user/getanswer/" + blog_id, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    answersList = new ArrayList<Answers>();
                    JSONArray array = new JSONArray(jsonArray.toString());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        int userid = object.getInt("fromUser_id");
                        String username = object.getString("from_User_name");
                        String content = object.getString("content");
                        String resTime = object.getString("resTime");
                        Answers answers = new Answers(userid, username, content, resTime);
                        answersList.add(answers);
                    }
                    Collections.reverse(answersList);
                    answerAdpter = new AnswerAdpter(BlogMoreActivity.this, answersList);
                    answerlistview.setAdapter(answerAdpter);
                    Log.d("list", answersList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", volleyError.toString());
                Toast.makeText(BlogMoreActivity.this, "请求出现错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(arrayRequest);
    }

    private void initmessage() {
        requestQueue = Volley.newRequestQueue(BlogMoreActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(HomePartent.getUrl() + "users/getblogs/" +
                blog_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject jsonobj = new JSONObject(jsonObject.toString());
                            String username = jsonobj.getString("user_name");
                            String content = jsonobj.getString("content");
                            friends_id = jsonObject.getInt("user_id");
                            userphotourl = jsonobj.getString("user_photo");
                            tv_username.setText(username);
                            tv_blogcontent.setText(content);
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.displayImage(userphotourl,userphotoview);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void registerlistener() {
        btn_addanswer.setOnClickListener(this);
        editText.addTextChangedListener(this);
        btn_addfriend.setOnClickListener(this);
        gridView_blog_image.setOnItemClickListener(this);
    }

    private void initview() {
        answerlistview = (ListView) findViewById(R.id.listview_answer);
        userphotoview = (ImageView) findViewById(R.id.iv_weibomore_uphoto);
        tv_username = (TextView) findViewById(R.id.tv_weibomore_username);
        tv_blogcontent = (TextView) findViewById(R.id.tv_blogmore_content);
        editText = (EditText) findViewById(R.id.ed_textanswer);
        btn_addanswer = (Button) findViewById(R.id.btn_add_answer);
        btn_addfriend = (View) findViewById(R.id.btn_addfriends);
        tickButton = new TickButton(getResources().getDimensionPixelSize(R.dimen.stroke_width));
        btn_addfriend.setBackground(tickButton);
        gridView_blog_image = (GridView) findViewById(R.id.gridView_blog_image);
        btn_addanswer.setEnabled(false);
        btn_addanswer.setBackgroundResource(R.color.white);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_answer:
                String content = editText.getText().toString();
                Map<String, Object> map = new HashMap<>();
                map.put("fromUser_id", user_id);
                map.put("blog_id", blog_id);
                map.put("content", content);
                JSONObject jsonre = new JSONObject(map);
                Log.d("de", jsonre.toString());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl() + "user/addanswer", jsonre, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject jsrespon = new JSONObject(jsonObject.toString());
                            boolean message = jsrespon.getBoolean("message");
                            if (message) {
                                Toast.makeText(BlogMoreActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                answerAdpter.notifyDataSetChanged();
                                initrequestAnswer();

                            } else {
                                Toast.makeText(BlogMoreActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(BlogMoreActivity.this, "请求错误，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                break;
            case R.id.btn_addfriends:
                Map<String, Integer> friendmap = new HashMap<String, Integer>();
                friendmap.put("user_id", user_id);
                friendmap.put("friends_id", friends_id);
                Log.d("bug",friendmap.toString());
                if (user_id != friends_id) {
                    JSONObject jsrequest = new JSONObject(friendmap);
                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl()+"users/addfriend", jsrequest, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONObject object = new JSONObject(jsonObject.toString());
                                boolean message = object.getBoolean("message");
                                if (message) {
                                    Toast.makeText(BlogMoreActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    tickButton.animateTick();
                                } else {
                                    Toast.makeText(BlogMoreActivity.this, "关注失败", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(BlogMoreActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                            Log.d("Error", volleyError.toString());
                        }
                    });
                    requestQueue.add(jsonObjectRequest1);
                } else {
                    Toast.makeText(BlogMoreActivity.this, "不可以关注自己", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString() != null && editable.toString() != "") {
            btn_addanswer.setBackgroundResource(R.color.colorPrimary);
            btn_addanswer.setEnabled(true);
        } else {
            btn_addanswer.setEnabled(false);
            btn_addanswer.setBackgroundResource(R.color.white);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent("imageviewpager");
        intent.putExtra("blog_id", blog_id);
        startActivity(intent);
    }
}
