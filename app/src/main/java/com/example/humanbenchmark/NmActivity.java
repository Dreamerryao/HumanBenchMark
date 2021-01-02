package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    CountDownTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nm);
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
                if(Integer.parseInt(user_input.getText().toString()) == Integer.parseInt(res)) {
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_LONG).show();
                    user_input.setText("");
                    level++;
                    nm_level.setText("Level:"+level);
                    res = "";
                    for(int i = 1;i<=level;i++){
                        res += numbers.charAt((int)(Math.random()*9));
                    }
                    nm_head.setText(res);
                    nm_head.setVisibility(View.VISIBLE);
                    nm_level.setVisibility(View.VISIBLE);
                    time_left.setVisibility(View.VISIBLE);
                    cdt.start();
                }
                else {
                    nm_head.setText("Your score is Level"+level+"!");
                    nm_head.setVisibility(View.VISIBLE);
                    nm_title.setText("click to try again");
                    nm_head.setVisibility(View.VISIBLE);
                    nm_retry.setVisibility(View.VISIBLE);

                }
            }
        });
        cdt = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                time_left.setText((int)(millisUntilFinished / 1000) + "s");
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
        for(int i = 1;i<=level;i++){
            res += numbers.charAt((int)(Math.random()*9));
        }
        nm_head.setText(res);
        nm_start.setVisibility(View.GONE);
        nm_title.setVisibility(View.GONE);
//        nm_enter.setVisibility(View.VISIBLE);
//        user_input.setVisibility(View.VISIBLE);
        nm_level.setText("Level:"+level);
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
}