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
import com.example.humanbenchmark.util.SharedHelper;

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

public class SignUpActivity extends AppCompatActivity {
    private Button ok_button;
    private Context mContext;
    private EditText edit_name;
    private EditText edit_pwd;
    private EditText edit_pwd_again;
    private SharedHelper sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mContext = getApplicationContext();
        sh= new SharedHelper(mContext);
        bindViews();
    }
    private void bindViews() {
        ok_button = findViewById(R.id.sign_button);
        edit_name = findViewById(R.id.input_signup_name);
        edit_pwd = findViewById(R.id.input_signup_pwd);
        edit_pwd_again = findViewById(R.id.input_signup_pwd_again);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        int msg_what = 0x003;
                        String username= edit_name.getText().toString();
                        String password= edit_pwd.getText().toString();
                        String pwd_again = edit_pwd_again.getText().toString();
                        if(pwd_again.equals(password)){
                            String signUrl = APIUtils.SIGN_URL;
                            //拼装url
                            //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                            try {
                                Log.e("Drea",username+"???");
                                Log.e("Drea",password+"???");
                                String lastUrl = signUrl + "?account="+ URLEncoder.encode(username, "utf-8")+"&password="+password;
                                URL url = new URL(lastUrl);
                                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();//开发访问此连接
                                //设置访问时长和相应时长
                                urlConn.setConnectTimeout(5*1000);//设置连接时间为5秒
                                urlConn.setReadTimeout(5*1000);//设置读取时间为5秒
                                int code = urlConn.getResponseCode();//获得相应码
                                Log.e("Drea",code+"???");
                                if(code == 200){//相应成功，获得相应的数据
                                    InputStream is = urlConn.getInputStream();//得到数据流（输入流）
                                    byte[] buffer = new byte[1024];
                                    int length = 0;
                                    String data = "";
                                    while((length = is.read(buffer)) != -1){
                                        String str = new String(buffer,0,length);
                                        data += str;
                                    }
                                    Log.e("Drea",data);
                                    // use properties to restore the map
                                    Properties props = new Properties();
                                    props.load(new StringReader(data.substring(1, data.length() - 2).replace(",", "\n")));
                                    Map<String, String> map2 = new HashMap<String, String>();
                                    for (Map.Entry<Object, Object> e : props.entrySet()) {
                                    Log.e("Fuck",e.getKey().toString());
                                    Log.e("Fuckk",e.getValue().toString());
                                        map2.put(e.getKey().toString(), e.getValue().toString());
                                    }
//                                Log.e("Drea",map2.toString());
//                                if(map2.containsKey("resCode")){
//                                    Log.e("fufsda",map2.get("resCode"));
//                                }
                                    //解析json，展示在ListView（GridView）
                                    if(map2.containsKey("resCode")&&(map2.get("resCode").equals("100"))){
                                        sh.save(SharedHelper.USERNAME, username);
                                        sh.save(SharedHelper.PASSWORD, password);
                                        sh.save(SharedHelper.USERID,map2.get("userId"));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                                        sh.save(SharedHelper.LOGINTIME,df.format(new Date()));
                                        msg_what = 0x005;
                                    }
                                    else if(map2.containsKey("resCode")&&(map2.get("resCode").equals("101"))){
                                        msg_what = 0x002;
                                    }
                                }
                                else{
                                    msg_what = 0x007;
                                }


                            } catch (MalformedURLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
//                        JSONObject login_json = JSONObject.fromObject(login_res);
//                        Toast.makeText(mContext, "登陆成功", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(mContext, MainActivity.class));
                        }
                        handler.sendEmptyMessage(msg_what);
                    }
                }.start();
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x003:
                    Toast.makeText(SignUpActivity.this, "两次输入的密码不一致，请重新核对", Toast.LENGTH_SHORT).show();
                    break;
                case 0x002:
                    Toast.makeText(SignUpActivity.this, "该账号已被注册", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(SignUpActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case 0x006:
                    Toast.makeText(SignUpActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(SignUpActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(SignUpActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        };
    };
}