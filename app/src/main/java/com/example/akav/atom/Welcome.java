package com.example.akav.atom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        @SuppressLint("HandlerLeak") Handler h=new Handler(){
            @Override
            public void handleMessage(Message msg){
                Intent i=new Intent().setClass(Welcome.this,MainActivity.class);
                startActivity(i);
            }
        };
        h.sendEmptyMessageDelayed(0,3000);
    }
}
