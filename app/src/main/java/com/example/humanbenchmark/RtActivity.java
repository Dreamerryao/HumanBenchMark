package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class RtActivity extends AppCompatActivity {
    private final String TAG = "RtActivity";
    Timer timer;
    long startTime;
    String elapsedTime;
    Vibrator vibrator;
    TextView RtHead, RtTitle;
    ConstraintLayout myLayout;
    TimerTask myTimerTask;
    private Context mContext;
    private SharedHelper sh;
    private int useTime;
    private int state = 0;//初始状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rt);
        timer = new Timer();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        RtHead = findViewById(R.id.RtHead);
        RtTitle = findViewById(R.id.RtTitle);
        myLayout = findViewById(R.id.RtLayout);
    }

    final Runnable run = new Runnable() {
        @Override
        public void run() {
            if (state == 1) {
                vibrator.vibrate(300);
                myLayout.setBackgroundColor(getResources().getColor(R.color.green));
                startTime = SystemClock.elapsedRealtime();
                state = 2;
            }

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
                Log.d(TAG, "按下");
                switch (state) {
                    case -2:
                        state = -1;
                        break;
                    case 1:
                        RtHead.setText(R.string.too_short);
                        RtTitle.setVisibility(View.VISIBLE);
                        RtTitle.setText(R.string.rt_again);
                        myLayout.setBackgroundColor(getResources().getColor(R.color.rt_blue));
                        timer = null;
                        myTimerTask = null;
                        state = -2;
                        break;
                    case 2:
                        useTime = (int) (SystemClock.elapsedRealtime() - startTime);
                        elapsedTime = "Your reaction time is " + useTime + " ms.";
                        PostScore();
                        RtHead.setText(elapsedTime);
                        RtTitle.setVisibility(View.VISIBLE);
                        RtTitle.setText(R.string.rt_again);
                        myLayout.setBackgroundColor(getResources().getColor(R.color.rt_blue));
                        state = -2;
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
                Log.d(TAG, "抬起");
                switch (state) {
                    case -1:
                        state = 0;
                        RtHead.setText(R.string.react_time_test);
                        RtTitle.setText(R.string.rt_start_title);
                        RtTitle.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                        state = 1;
//                        RtHead.setVisibility(View.INVISIBLE);
                        RtHead.setText(R.string.rt_waiting);
                        RtTitle.setVisibility(View.INVISIBLE);
                        myLayout.setBackgroundColor(getResources().getColor(R.color.red));

                        if (timer == null) {
                            timer = new Timer();
                        }
                        myTimerTask = new TimerTask() {
                            public void run() {
                                runOnUiThread(run);
                            }
                        };
                        if (timer != null && myTimerTask != null)
                            timer.schedule(myTimerTask, (int) (Math.random() * 5000 + 1000));
                        break;
                    default:
                        break;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

    public void PostScore() {
        new Thread() {
            public void run() {
                int msg_what = 0x006;
                String post_url = APIUtils.PO_URL;
                //拼装url
                //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                try {
                    Log.e("userId", sh.get(SharedHelper.USERID));
                    Log.e("score", useTime + "");
                    String lastUrl = post_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8") + "&testId=1&score=" + URLEncoder.encode(useTime + "", "utf-8");
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
                    Toast.makeText(RtActivity.this, "上传成绩失败", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(RtActivity.this, "上传成绩成功", Toast.LENGTH_SHORT).show();

                    break;
                case 0x006:
                    Toast.makeText(RtActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(RtActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(RtActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };
}