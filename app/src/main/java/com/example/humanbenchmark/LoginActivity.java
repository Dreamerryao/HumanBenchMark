package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login,btn_register;
    private Context mContext;
    private EditText edit_name;
    private EditText edit_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        bindViews();
    }

    private void bindViews(){
        edit_name = findViewById(R.id.input_login_name);
        edit_pwd = findViewById(R.id.input_login_pwd);
        btn_login = findViewById(R.id.login_btn_login);
        btn_register = findViewById(R.id.login_btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                Toast.makeText(mContext,"switch to register activity~ ",Toast.LENGTH_SHORT).show();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int msg_what = 0x006;
                        String username= edit_name.getText().toString();
                        String password= edit_pwd.getText().toString();
//                        Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(mContext, MainActivity.class));
                        handler.sendEmptyMessage(msg_what);
                    }
                }.start();
            }
        });
    }
    //用于刷新界面和跳转页面

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x001:
                    Toast.makeText(LoginActivity.this, "请您先注册，再访问我们的app", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case 0x006:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case 0x007:
                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };
}