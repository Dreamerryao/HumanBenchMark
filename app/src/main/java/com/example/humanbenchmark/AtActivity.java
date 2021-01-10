package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.RelativeLayout;
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
import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class AtActivity extends AppCompatActivity {
    private final String TAG = "AtActivity";
    CircleImageView aim_image;
    CircleImageView aim_player_img;
    ConstraintLayout AtLayout;
    TextView head;
    TextView title;
    TextView second_title;
    TextView AtRemain;
    TextView AtCounter;
    Timer timer;
    long startTime;
    ConstraintSet c;
    Context context;
    private Context mContext;
    private SharedHelper sh;
    private int cnt;
    private int realH;
    private int realW;
    private int rH;
    private int rW;
    private int useTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at);
        timer = new Timer();
        aim_image = findViewById(R.id.aim_image);
        head = findViewById(R.id.AtHead);
        title = findViewById(R.id.AtTitle);
        second_title = findViewById(R.id.AtSecondTitle);
        AtRemain = findViewById(R.id.AtRemain);
        AtCounter = findViewById(R.id.AtCounter);
        aim_player_img = findViewById(R.id.aim_image2);
        AtLayout = findViewById(R.id.AtLayout);
        context = this;
        cnt = 30;
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        aim_image.setOnClickListener(
                v -> {
                    head.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    second_title.setVisibility(View.INVISIBLE);
                    aim_image.setVisibility(View.INVISIBLE);
                    AtRemain.setVisibility(View.VISIBLE);
                    AtCounter.setText(cnt + "");
                    AtCounter.setVisibility(View.VISIBLE);
                    c = new ConstraintSet();
                    c.clone(AtLayout);
                    c.connect(aim_player_img.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, rW);
                    c.connect(aim_player_img.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, rH);
                    c.applyTo(AtLayout); // set new constraints
                    aim_player_img.setVisibility(View.VISIBLE);
                    startTime = SystemClock.elapsedRealtime();
                }
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        realW = AtLayout.getWidth();
        realH = AtLayout.getHeight();
        rW = (int) (Math.random() * (realW - 150)) + 10;
        rH = (int) (Math.random() * (realH - 150)) + 10;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(aim_player_img.getLayoutParams());
        layoutParams.topMargin = rH;
        layoutParams.rightMargin = rW;
        aim_player_img.setLayoutParams(layoutParams);

    }

    public void clickImg(View view) {
        cnt--;
        AtCounter.setText(cnt + "");
        if (cnt == 0) {
            cnt = 30;
            AtRemain.setVisibility(View.INVISIBLE);
            AtCounter.setVisibility(View.INVISIBLE);
            aim_player_img.setVisibility(View.INVISIBLE);
            aim_image.setVisibility(View.VISIBLE);
            useTime = (int) ((SystemClock.elapsedRealtime() - startTime) / 30);
            PostScore();
            title.setText("Average time per target is " + useTime + "ms");
            second_title.setText("Click the target above to try again");
            head.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);
            second_title.setVisibility(View.VISIBLE);
        } else {
            rW = (int) (Math.random() * (realW - 150)) + 10;
            rH = (int) (Math.random() * (realH - 150)) + 10;
            c = new ConstraintSet();
            c.clone(AtLayout);
            c.connect(aim_player_img.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, rW);
            c.connect(aim_player_img.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, rH);
            c.applyTo(AtLayout); // set new constraints
        }
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
                    String lastUrl = post_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8") + "&testId=2&score=" + URLEncoder.encode(useTime + "", "utf-8");
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
                    Toast.makeText(AtActivity.this, "上传成绩失败", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(AtActivity.this, "上传成绩成功", Toast.LENGTH_SHORT).show();

                    break;
                case 0x006:
                    Toast.makeText(AtActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(AtActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(AtActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };
}