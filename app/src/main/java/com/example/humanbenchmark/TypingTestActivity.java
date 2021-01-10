package com.example.humanbenchmark;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.humanbenchmark.util.APIUtils;
import com.example.humanbenchmark.util.SharedHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class TypingTestActivity extends AppCompatActivity {
    private TextView tv_in;
    private TextView tt_timer;
    private TextView tt_cpm;//character per minute
    private TextView tt_sec_timer;
    private Button try_again_button;
    private String TAG = "typingTest";
    private int count = 1;
    private int success = 0;
    private int time;
    private int score;
    private Context mContext;
    private SharedHelper sh;
    Timer timer;
    private boolean flag = false;//是否按下第一个字符
    SpannableString ss;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);
        tv_in = findViewById(R.id.txtParagraph);
        tt_timer = findViewById(R.id.tt_title);
        tt_sec_timer = findViewById(R.id.tt_second_title);
        tt_cpm = findViewById(R.id.txtTimer);
        try_again_button = findViewById(R.id.try_again);
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        timer = new Timer();
        ss = new SpannableString(tv_in.getText().toString());
        tv_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
        tv_in.requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        switch (event.getAction()) {
            case KeyEvent.ACTION_UP:
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_ENTER:
                        Log.d("test", "[Dispatch] Enter bar pressed! " + event);
                        break;
                    case KeyEvent.KEYCODE_SPACE:
                        tt_cpm.setText(time == 0 ? 0 + "" : (int) (success * 60 / time) + "");
                        if (count <= tv_in.length()) {
                            if (!flag) {
                                flag = true;
                                startMethod();
                            }
                            if (' ' == Character.toLowerCase(tv_in.getText().charAt(count - 1))) {
                                success++;
                                ss.setSpan(new BackgroundColorSpan(Color.GREEN), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                                ss.setSpan(new ForegroundColorSpan(Color.BLACK), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            } else {
                                ss.setSpan(new BackgroundColorSpan(Color.RED), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                                ss.setSpan(new ForegroundColorSpan(Color.WHITE), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            }
                            Log.d("test", "[Dispatch] Space bar pressed! " + event);
                            count++;
                            tv_in.setText(ss);
                        }
                        if (count == tv_in.length() + 1) {
                            finishTest();
                        }
                        break;
//                        return true;
                    case KeyEvent.KEYCODE_BACK:
                    case KeyEvent.KEYCODE_DEL:
                        tt_cpm.setText(time == 0 ? 0 + "" : (int) (success * 60 / time) + "");
                        Log.d("test", "[Dispatch] back bar pressed! " + event);
                        if (!flag) {
                            flag = true;
                            startMethod();
                        }
                        if (count > 1) {
                            count--;
                            BackgroundColorSpan[] objectSpans = ss.getSpans(count - 1, count, BackgroundColorSpan.class);
                            if (objectSpans.length == 0) {//bug
                                break;
                            } else {
                                if (objectSpans[0].getBackgroundColor() == Color.GREEN) {
                                    success--;
                                }
                            }
                            ss.setSpan(new BackgroundColorSpan(Color.WHITE), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            ss.setSpan(new ForegroundColorSpan(Color.BLACK), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            tv_in.setText(ss);
                        }
                        break;
                    default:
                        tt_cpm.setText(time == 0 ? 0 + "" : (int) (success * 60 / time) + "");
                        if (!flag) {
                            flag = true;
                            startMethod();
                        }
                        char ch = Character.toLowerCase(event.getDisplayLabel());
                        Log.i(TAG, "输入了" + ch);
                        if (count <= tv_in.length()) {
                            if (ch == Character.toLowerCase(tv_in.getText().charAt(count - 1))) {
                                success++;
                                ss.setSpan(new BackgroundColorSpan(Color.GREEN), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                                ss.setSpan(new ForegroundColorSpan(Color.BLACK), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            } else {
                                ss.setSpan(new BackgroundColorSpan(Color.RED), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                                ss.setSpan(new ForegroundColorSpan(Color.WHITE), count - 1, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
                            }
                            Log.d("test", "[Dispatch] Space bar pressed! " + event);
                            count++;
                            tv_in.setText(ss);
                        }
                        if (count == tv_in.length() + 1) {
                            finishTest();
                        }
                        break;
                }

            case KeyEvent.ACTION_DOWN:
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void finishTest() {
        Toast toast = Toast.makeText(TypingTestActivity.this, "测试完成!", Toast.LENGTH_LONG);
        PostScore();
        toast.show();
        timer.cancel();
        score = time == 0 ? 0: (int) (success * 60 / time);
        tt_cpm.setText(score + "CPM");
        tt_timer.setText("click to try again.");
        tv_in.setVisibility(View.GONE);
        try_again_button.setVisibility(View.VISIBLE);
        hideKeyBoard();
    }

    private void startMethod() {
        tt_sec_timer.setVisibility(View.INVISIBLE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tt_timer.setText(String.format("%02d : %02d", time / 60, time % 60));
                        tt_cpm.setText(time == 0 ? 0 + "" : (int) (success * 60 / time) + "");
                    }
                });

                Log.i("TAG", "这是普通方式的TimerTask");
            }
        }, 0, 1000);
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    public void tryAgain(View view) {
        success = 0;
        count = 1;
        flag = false;
        timer = new Timer();
        time = 0;
        ss = new SpannableString(tv_in.getText().toString());
        try_again_button.setVisibility(View.GONE);
        tv_in.setVisibility(View.VISIBLE);
        tv_in.setText(ss);
        tt_timer.setText(R.string.tt_title);
        tt_cpm.setText(R.string.tt_head);
        tt_sec_timer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();

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
                    Log.e("score", score + "");
                    String lastUrl = post_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8") + "&testId=3&score=" + URLEncoder.encode(score + "", "utf-8");
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
                    Toast.makeText(TypingTestActivity.this, "上传成绩失败", Toast.LENGTH_SHORT).show();
                    break;

                case 0x005:
                    Toast.makeText(TypingTestActivity.this, "上传成绩成功", Toast.LENGTH_SHORT).show();

                    break;
                case 0x006:
                    Toast.makeText(TypingTestActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(TypingTestActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(TypingTestActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };
}
