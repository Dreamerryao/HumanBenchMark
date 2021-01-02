package com.example.humanbenchmark.ui.notifications;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.humanbenchmark.R;

public class TypingTestActivity extends AppCompatActivity {
    private TextView tv_in;
    private String TAG = "typingtest";
    private int count=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tt);
        tv_in = findViewById(R.id.tv1);
        tv_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        //1.首先获得用户输入的字符
        char ch=Character.toLowerCase(event.getDisplayLabel());
        Log.i(TAG, "输入了"+ch);
        if(ch==tv_in.getText().charAt(count-1) && event.getAction()==KeyEvent.ACTION_DOWN){
            //将文字更改类型
            SpannableString ss=new SpannableString(tv_in.getText().toString());
            ss.setSpan(new ForegroundColorSpan(Color.RED), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符的颜色
            ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, count, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//更改了字符
            count++;
            tv_in.setText(ss);
        }
        if(count==tv_in.length()){
            Toast toast=Toast.makeText(TypingTestActivity.this,"成功！",Toast.LENGTH_LONG);
            toast.show();
        }

        return super.onKeyDown(keyCode, event);
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
}
