package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ycy on 16-4-12.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    protected Spinner spinner;
    protected EditText username;
    protected EditText password;
    protected Button btn_registe;
    protected Button btn_back;
    protected String sex;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("注册");
        setContentView(R.layout.activity_register);
        initview();
        registelistener();
    }

    private void registelistener()
    {
        btn_registe.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    private void initview()
    {
        username = (EditText) findViewById(R.id.ed_register_username);
        password = (EditText) findViewById(R.id.ed_password_registe);
        btn_registe = (Button) findViewById(R.id.register_btn_regist);
        btn_back = (Button) findViewById(R.id.register_btn_back);
        spinner = (Spinner) findViewById(R.id.spinner_sex);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_btn_regist:
                String user_name = username.getText().toString();
                String pass_word = password.getText().toString();
                doregister(user_name,pass_word,sex);
                break;
            case R.id.register_btn_back:
                Intent intent = new Intent("login");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void doregister(String user_name, String pass_word,String sex)
    {
        requestQueue = Volley.newRequestQueue(this);
        Map<String,String> map = new HashMap<String,String>();
        map.put("username",user_name);
        map.put("password",pass_word);
        map.put("sex",sex);
        JSONObject jso = new JSONObject(map);
        JsonObjectRequest jsreq = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl() + "users/doregister", jso, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject jsrspons = new JSONObject(jsonObject.toString());
                    boolean message = jsrspons.getBoolean("message");
                    Log.d("bug",String.valueOf(message));
                    if (message){
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT);
                        Intent intent  = new Intent("login");
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"用户名已经有人注册了哦，换一个试试吧",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegisterActivity.this,"网络请求出错",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsreq);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sex = RegisterActivity.this.getResources().getStringArray(R.array.sex)[i];
        Log.d("sex",sex);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        sex = "保密";
    }
}
