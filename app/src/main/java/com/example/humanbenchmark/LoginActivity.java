package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.humanbenchmark.util.APIUtils;
import com.example.humanbenchmark.util.GetData;
import com.example.humanbenchmark.util.SharedHelper;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LoginActivity extends AppCompatActivity {
    private Button btn_login, btn_register;
    private Context mContext;
    private EditText edit_name;
    private EditText edit_pwd;
    private SharedHelper sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        bindViews();
    }

    private void bindViews() {
        edit_name = findViewById(R.id.input_login_name);
        edit_pwd = findViewById(R.id.input_login_pwd);
        btn_login = findViewById(R.id.login_btn_login);
        btn_register = findViewById(R.id.login_btn_register);
        edit_name.setText(sh.get(SharedHelper.USERNAME));
        edit_pwd.setText(sh.get(SharedHelper.PASSWORD));
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                Toast.makeText(mContext, "switch to register activity~ ", Toast.LENGTH_SHORT).show();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int msg_what = 0x006;
                        String username = edit_name.getText().toString();
                        String password = edit_pwd.getText().toString();
//                        HashMap<String, String> arguments = new HashMap<>();
//                        arguments.put("account", username);
//                        arguments.put("password", password);
                        // try login
                        String login_url = APIUtils.LOGIN_URL;
                        //拼装url
                        //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                        try {
                            String lastUrl = login_url + "?account=" + URLEncoder.encode(username, "utf-8") + "&password=" + password;
                            URL url = new URL(lastUrl);
                            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();//开发访问此连接
                            //设置访问时长和相应时长
                            urlConn.setConnectTimeout(5 * 1000);//设置连接时间为5秒
                            urlConn.setReadTimeout(5 * 1000);//设置读取时间为5秒
                            int code = urlConn.getResponseCode();//获得相应码
                            if (code == 200) {//相应成功，获得相应的数据
                                InputStream is = urlConn.getInputStream();//得到数据流（输入流）
                                byte[] buffer = new byte[1024];
                                int length = 0;
                                String data = "";
                                while ((length = is.read(buffer)) != -1) {
                                    String str = new String(buffer, 0, length);
                                    data += str;
                                }
                                Log.e("Drea", data);
                                // use properties to restore the map
                                Properties props = new Properties();
                                props.load(new StringReader(data.substring(1, data.length() - 2).replace(",", "\n")));
                                Map<String, String> map2 = new HashMap<String, String>();
                                for (Map.Entry<Object, Object> e : props.entrySet()) {
//                                    Log.e("Fuck",e.getKey().toString());
//                                    Log.e("Fuckk",e.getValue().toString());
                                    map2.put(e.getKey().toString(), e.getValue().toString());
                                }
//                                Log.e("Drea",map2.toString());
//                                if(map2.containsKey("resCode")){
//                                    Log.e("fufsda",map2.get("resCode"));
//                                }
                                //解析json，展示在ListView（GridView）
                                if (map2.containsKey("resCode") && (map2.get("resCode").equals("201"))) {
                                    sh.save(SharedHelper.USERNAME, username);
                                    sh.save(SharedHelper.PASSWORD, password);
                                    sh.save(SharedHelper.USERID, map2.get("userId"));
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                    sh.save(SharedHelper.LOGINTIME, df.format(new Date()));
                                    msg_what = 0x005;
                                } else if (map2.containsKey("resCode") && (map2.get("resCode").equals("203"))) {
                                    msg_what = 0x001;
                                } else if (map2.containsKey("resCode") && (map2.get("resCode").equals("202"))) {
                                    msg_what = 0x002;
                                }
                            } else {
                                msg_what = 0x007;
                            }


                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
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
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case 0x006:
                    Toast.makeText(LoginActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };
}