package com.example.alexander.mytest2;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MenuActivity extends AppCompatActivity {

    ImageButton playerbtn;
    ImageButton transferbtn;
    ImageButton settingsbtn;
    ImageButton closebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        playerbtn = (ImageButton)findViewById(R.id.playerbtn);
        transferbtn = (ImageButton)findViewById(R.id.transferbtn);
        settingsbtn = (ImageButton)findViewById(R.id.settingsbtn);
        closebtn = (ImageButton)findViewById(R.id.closebtn);


        playerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
       transferbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAffinity(MenuActivity.this);
            }
        });


        playerbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   playerbtn.setBackgroundColor(Color.parseColor("#a0d02c2c"));//#a0d02c2c
                }
                else
                {
                    playerbtn.setBackgroundColor(Color.parseColor("#14c0c0c0"));
                }
                return false;
            }
        });

        transferbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    transferbtn.setBackgroundColor(Color.parseColor("#a0d02c2c"));//#a0d02c2c
                }
                else
                {
                    transferbtn.setBackgroundColor(Color.parseColor("#14c0c0c0"));
                }
                return false;
            }
        });

        settingsbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    settingsbtn.setBackgroundColor(Color.parseColor("#a0d02c2c"));//#a0d02c2c
                }
                else
                {
                    settingsbtn.setBackgroundColor(Color.parseColor("#14c0c0c0"));
                }
                return false;
            }
        });

        closebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closebtn.setBackgroundColor(Color.parseColor("#a0d02c2c"));//#a0d02c2c
                }
                else
                {
                    closebtn.setBackgroundColor(Color.parseColor("#14c0c0c0"));
                }
                return false;
            }
        });

    }
}
