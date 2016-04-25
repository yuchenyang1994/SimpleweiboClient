package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ycy on 16-4-14.
 */
public class WriteWeibo extends AppCompatActivity implements View.OnClickListener {
    protected EditText ed_text_blog;
    protected Button btn_write_blog;
    protected Button btn_back_blog;
    protected RequestQueue requestQueue;
    protected ImageView imageView;
    private File blogimage;
    private Uri imageuri;
    public static final int CROP_PHOTO = 1;
    public static final int CROP_CAPTURE =2;
    public static final int TAKE_PHOTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writeblog);
        setTitle("写博客");
        initview();
        registerListen();
    }

    private void registerListen() {
        btn_write_blog.setOnClickListener(this);
        btn_back_blog.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    private void initview() {
        btn_write_blog = (Button) findViewById(R.id.btn_write_blog);
        btn_back_blog = (Button) findViewById(R.id.btn_write_back);
        ed_text_blog = (EditText) findViewById(R.id.ed_write_blog);
        imageView = (ImageView) findViewById(R.id.iv_upload_image);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_write_blog:
                sendBlog();
                break;
            case R.id.btn_write_back:
                Intent intent = new Intent("index");
                startActivity(intent);
                break;
            case R.id.iv_upload_image:
                blogimage = new File(Environment.getExternalStorageDirectory(), "uploadout.png");
                imageuri = Uri.fromFile(blogimage);
                Intent outimageintent = new Intent(Intent.ACTION_GET_CONTENT);
                outimageintent.setDataAndType(imageuri, "image/*");
                outimageintent.putExtra("scale", true);
                startActivityForResult(outimageintent, CROP_PHOTO);
                break;
            default:
                break;
        }
    }

    private void sendBlog() {
        String content = ed_text_blog.getText().toString();
        SharedPreferences pref = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        int user_id = pref.getInt("user_id", 0);
        String imagebase64 = null;
        if (blogimage.exists()){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile(blogimage.getPath());
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            imagebase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            blogimage.delete();
        }
        requestQueue = Volley.newRequestQueue(this);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id", user_id);
        map.put("content", content);
        map.put("blog_image",imagebase64);
        JSONObject jsonreobj = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                HomePartent.getUrl() + "users/writeblog", jsonreobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject jsrespon = new JSONObject(jsonObject.toString());
                            boolean message = jsrespon.getBoolean("message");
                            if (message) {
                                Toast.makeText(WriteWeibo.this, "发表成功", Toast.LENGTH_SHORT).show();
                                Thread.sleep(1000);
                                Intent intent  = new Intent("index");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(WriteWeibo.this, "发表失败", Toast.LENGTH_SHORT).show();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WriteWeibo.this, "请求出错", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    ContentResolver contentResolver = getContentResolver();
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                        if (blogimage.exists()){
                            blogimage.delete();
                        }
                        blogimage.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(blogimage);
                        if (bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream)){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_CAPTURE:

                break;
            case TAKE_PHOTO:

                break;
        }
    }
}
