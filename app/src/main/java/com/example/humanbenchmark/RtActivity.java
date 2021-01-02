package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class RtActivity extends AppCompatActivity {
    private final String TAG="RtActivity";
    Timer timer;
    long startTime;
    String elapsedTime;
    Vibrator vibrator;
    TextView RtHead,RtTitle;
    ConstraintLayout myLayout;
    TimerTask myTimerTask;


    private int state = 0;//初始状态
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rt);
        timer = new Timer();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        RtHead = findViewById(R.id.RtHead);
        RtTitle = findViewById(R.id.RtTitle);
        myLayout = findViewById(R.id.RtLayout);
    }
    final Runnable run = new Runnable() {
        @Override
        public void run() {
            if(state==1){
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
                Log.d(TAG,"按下");
                switch (state){
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
                        elapsedTime = "Your reaction time is " + (int)(SystemClock.elapsedRealtime() - startTime) + " ms.";
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
                Log.d(TAG,"抬起");
                switch (state){
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
                        if(timer != null && myTimerTask != null )
                        timer.schedule(myTimerTask,(int)(Math.random() * 5000 + 1000));
                        break;
                    default:
                        break;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

}