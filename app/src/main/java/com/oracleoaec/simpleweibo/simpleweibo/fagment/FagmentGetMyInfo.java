package com.oracleoaec.simpleweibo.simpleweibo.fagment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.oracleoaec.simpleweibo.simpleweibo.Adapter.FriendInfoAdapter;
import com.oracleoaec.simpleweibo.simpleweibo.R;
import com.oracleoaec.simpleweibo.simpleweibo.Utils.HomePartent;
import com.oracleoaec.simpleweibo.simpleweibo.enity.Friend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ycy on 16-4-12.
 */
public class FagmentGetMyInfo extends Fragment implements View.OnClickListener {
    public static final int TAKE_PHOTO=1;
    public static final int CROP_PHOTO=1000;
    public static final int Update_text = 1;
    public static final int Update_image = 2;
    protected ImageView userimage;
    protected TextView userinfo;
    protected Button btn_updatephoto;
    protected Button btn_logout;
    protected Context context;
    protected String name = "loding...";
    protected String sex = "Loding....";
    protected String photo;
    protected Bitmap userbitmap;
    private RequestQueue requestQueue;
    private RequestQueue uploadeQueue;
    protected Handler handler;
    private int user_id;
    private Uri imageuri;
    private File imagefile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = pref.getInt("user_id",0);
        View view = inflater.inflate(R.layout.fagment_myinfo,container,false);
        initview(view);
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Update_text:
                        userinfo.setText("用户名：" + name + "性别：" + sex);
                        break;
                    case Update_image:
                        userimage.setImageBitmap(userbitmap);
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestQueue = Volley.newRequestQueue(context);
                Message message = new Message();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(HomePartent.getUrl() + "users/getuserinfo/" + user_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONObject json = new JSONObject(jsonObject.toString());
                            name = json.getString("username");
                            sex = json.getString("sex");
                            photo = json.getString("photo");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("error",volleyError.toString());
                    }
                });
                requestQueue.add(jsonObjectRequest);
                message.what = Update_text;
                handler.sendMessage(message);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message message = new Message();
                    URL url = new URL(photo);
                    InputStream inputStream = url.openStream();
                    userbitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    message.what = Update_image;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
    }




    private void initview(View view)
    {
        userimage = (ImageView) view.findViewById(R.id.fagment_myinfo_imv);
        userinfo = (TextView) view.findViewById(R.id.myinfo_tv_username);
        btn_updatephoto = (Button) view.findViewById(R.id.btn_upload_photo);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_updatephoto.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upload_photo:
                imagefile = new File(Environment.getExternalStorageDirectory(),"out.png");
                imageuri = Uri.fromFile(imagefile);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(imageuri, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
                intent.putExtra("scale", true);
                startActivityForResult(intent,CROP_PHOTO);
                break;
            case R.id.btn_logout:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE).edit();
                editor.putInt("user_id", 0);
                editor.commit();
                Intent intent1 = new Intent("login");
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                break;
            case CROP_PHOTO:
                ContentResolver contentResolver = getActivity().getContentResolver();
                Uri uri = data.getData();
                Log.d("uri", uri.toString());
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    if (imagefile.exists()){
                        imagefile.delete();
                    }
                    imagefile.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(imagefile);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG,90,fileOutputStream)){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    Log.d("image",imagefile.toString());


                    userimage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateuserphoto();
                break;
            default:
                break;
        }
    }

    private void updateuserphoto()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(imagefile.getPath());
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        String s = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("user_id",user_id);
        map.put("user_photo",s);
        JSONObject object = new JSONObject(map);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl() + "user/uploadphoto", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("message",jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error",volleyError.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
