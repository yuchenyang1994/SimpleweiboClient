package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_login;
    private Button btn_register;
    private EditText ed_username;
    private EditText ed_password;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.login_title);
        }
        initview();
        registerlistener();
    }

    private void registerlistener() {
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    private void initview() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        ed_username = (EditText) findViewById(R.id.ed_uername);
        ed_password = (EditText) findViewById(R.id.ed_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = ed_username.getText().toString();
                String password = ed_password.getText().toString();
                request(username, password);
                break;
            case R.id.btn_register:
                Intent intent = new Intent("register");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void request(String username, String password) {
        requestQueue = Volley.newRequestQueue(this);
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonob = new JSONObject(map);
        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.POST, HomePartent.getUrl() + "users/dologin", jsonob, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i("debug",jsonObject.toString());
                    JSONObject jsonrspons = new JSONObject(jsonObject.toString());
                    boolean message = jsonrspons.getBoolean("message");
                    int user_id = jsonrspons.getInt("user_id");
                    Log.d("bug",String.valueOf(message));
                    if (message==true) {
                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putInt("user_id", user_id);
                        editor.commit();
                        Intent intent = new Intent("index");
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonreq);
    }


}
