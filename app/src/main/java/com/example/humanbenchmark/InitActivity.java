package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.humanbenchmark.util.APIUtils;
import com.example.humanbenchmark.util.GetData;
import com.example.humanbenchmark.util.SharedHelper;
import com.example.humanbenchmark.util.TimeUtils;


import net.sf.json.JSONObject;

import java.util.HashMap;

public class InitActivity extends AppCompatActivity {
    private String key;
    private SharedHelper sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        sh = new SharedHelper(getApplicationContext());
        new Thread() {
            @Override
            public void run() {
                // send init message to server
                HashMap<String, String> argus = new HashMap<>();
                // Api url
                String url = APIUtils.INIT_URL;
                String res = GetData.getFormbodyPostData(url, argus, true);
                if (res == null) {
                    handler.sendEmptyMessage(0x002);
                    return;
                }

                // judge if success
                if (res.indexOf("success") != -1) {
                    handler.sendEmptyMessage(0x003);
                } else {
                    handler.sendEmptyMessage(0x002);
                }

            }
        }.start();
    }

    // init to server
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0x002) {
                Toast.makeText(InitActivity.this, "服务器连接失败，请检查你的网络状况！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0x003) {
                // get extra paraments
                Toast.makeText(InitActivity.this, "服务器连接成功！", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(InitActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };
}