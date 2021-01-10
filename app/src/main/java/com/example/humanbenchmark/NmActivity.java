package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class NmActivity extends AppCompatActivity {
    private TextView nm_head;
    private TextView nm_title;
    private Button nm_start;
    private TextView nm_level;
    private EditText user_input;
    private Button nm_retry;
    private Button nm_enter;
    private String numbers = "0123456789";
    private int level = 1;
    private String res = "";
    private TextView time_left;
    private Context mContext;
    private SharedHelper sh;
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nm);
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        nm_head = findViewById(R.id.NmHead);
        nm_title = findViewById(R.id.NmTitle);
        nm_start = findViewById(R.id.nm_start);
        nm_enter = findViewById(R.id.enter_button);
        nm_retry = findViewById(R.id.nm_retry);
        user_input = findViewById(R.id.user_input);
        time_left = findViewById(R.id.NmTimeLeft);
        nm_level = findViewById(R.id.NmLevel);
        nm_enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user_input.setVisibility(View.GONE);
                nm_enter.setVisibility(View.GONE);
                if (res != "" && (Integer.parseInt(user_input.getText().toString()) == Integer.parseInt(res))) {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG).show();
                    user_input.setText("");
                    level++;
                    nm_level.setText("Level:" + level);
                    res = "";
                    for (int i = 1; i <= level; i++) {
                        res += numbers.charAt((int) (Math.random() * 9));
                    }
                    nm_head.setText(res);
                    nm_head.setVisibility(View.VISIBLE);
                    nm_level.setVisibility(View.VISIBLE);
                    time_left.setVisibility(View.VISIBLE);
                    cdt.start();
                } else {
                    PostScore();
                    nm_head.setText("Your score is Level" + level + "!");
                    nm_head.setVisibility(View.VISIBLE);
                    nm_title.setText("click to try again");
                    nm_head.setVisibility(View.VISIBLE);
                    nm_retry.setVisibility(View.VISIBLE);

                }
            }
        });
        cdt = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                time_left.setText((int) (millisUntilFinished / 1000) + "s");
            }

            public void onFinish() {
                time_left.setText("");
                time_left.setVisibility(View.GONE);
                nm_head.setVisibility(View.GONE);
                nm_enter.setVisibility(View.VISIBLE);
                user_input.setVisibility(View.VISIBLE);
                user_input.setFocusable(true);
                user_input.setFocusableInTouchMode(true);
                user_input.requestFocus();
            }
        };
    }

    public void handleNmStart(View view) {
        res = "";
        for (int i = 1; i <= level; i++) {
            res += numbers.charAt((int) (Math.random() * 9));
        }
        nm_head.setText(res);
        nm_start.setVisibility(View.GONE);
        nm_title.setVisibility(View.GONE);
//        nm_enter.setVisibility(View.VISIBLE);
//        user_input.setVisibility(View.VISIBLE);
        nm_level.setText("Level:" + level);
        nm_level.setVisibility(View.VISIBLE);
        time_left.setVisibility(View.VISIBLE);
        cdt.start();
    }

    public void handleNmRetry(View view) {
        level = 1;
        res = "";
        user_input.setText("");
        nm_retry.setVisibility(View.GONE);
        nm_level.setVisibility(View.GONE);
        time_left.setVisibility(View.GONE);
        nm_enter.setVisibility(View.GONE);
        user_input.setVisibility(View.GONE);
        nm_head.setText(R.string.nm_head);
        nm_head.setVisibility(View.VISIBLE);
        nm_start.setVisibility(View.VISIBLE);
        nm_title.setText(R.string.nm_title);
        nm_title.setVisibility(View.VISIBLE);
    }

    //
//    private void updateLevel() {
//        level++;
//        String temp = "Level: " + level;
//        level.setText(temp);
//    }
    public void PostScore() {
        new Thread() {
            public void run() {
                int msg_what = 0x006;
                String post_url = APIUtils.PO_URL;
                //拼装url
                //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                try {
                    Log.e("userId", sh.get(SharedHelper.USERID));
                    Log.e("score", level + "");
                    String lastUrl = post_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8") + "&testId=4&score=" + URLEncoder.encode(level + "", "utf-8");
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
                        if (map2.containsKey("resCode") && (map2.get("resCode").equals("400"))) {
                            msg_what = 0x005;
                        } else {
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x002:
                    Toast.makeText(NmActivity.this, "上传成绩失败", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(NmActivity.this, "上传成绩成功", Toast.LENGTH_SHORT).show();

                    break;
                case 0x006:
                    Toast.makeText(NmActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(NmActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(NmActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };
}