package com.oracleoaec.simpleweibo.simpleweibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private AnimationDrawable anmi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initview();
        getuserpre();
    }

    private void initview()
    {
        imageView = (ImageView) findViewById(R.id.imageView);
        anmi = (AnimationDrawable) imageView.getBackground();
        anmi.start();
    }

    public void getuserpre()
    {
        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
        int user_id = pref.getInt("user_id",0);
        if (user_id == 0 || pref == null){
            Intent intent = new Intent("login");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
        else {
            Intent intent = new Intent("index");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }
}
