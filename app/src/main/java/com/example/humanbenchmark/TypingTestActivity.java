package com.example.humanbenchmark;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import java.text.Format;
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
        toast.show();
        timer.cancel();
        tt_cpm.setText(time == 0 ? 0 + "CPM" : (int) (success * 60 / time) + "CPM");
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
}
