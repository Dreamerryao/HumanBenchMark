package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
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

import java.util.Timer;

import de.hdodenhof.circleimageview.CircleImageView;

public class AtActivity extends AppCompatActivity {
    private final String TAG="AtActivity";
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
    private int cnt;
    private int realH;
    private int realW;
    private int rH;
    private int rW;
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
        aim_image.setOnClickListener(
                v -> {
                   head.setVisibility(View.INVISIBLE);
                   title.setVisibility(View.INVISIBLE);
                   second_title.setVisibility(View.INVISIBLE);
                   aim_image.setVisibility(View.INVISIBLE);
                   AtRemain.setVisibility(View.VISIBLE);
                   AtCounter.setText(cnt+"");
                   AtCounter.setVisibility(View.VISIBLE);
                    c = new ConstraintSet();
                    c.clone(AtLayout);
                    c.connect(aim_player_img.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,rW);
                    c.connect(aim_player_img.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,rH);
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
        rW = (int)(Math.random()*(realW-150))+10;
        rH = (int)(Math.random()*(realH-150))+10;
        ConstraintLayout.LayoutParams layoutParams = new  ConstraintLayout.LayoutParams(aim_player_img.getLayoutParams());
        layoutParams.topMargin = rH;
        layoutParams.rightMargin = rW;
        aim_player_img.setLayoutParams(layoutParams);

//        Log.d("h_bl", "屏幕宽度（像素）：" + realW);
//        Log.d("h_bl", "屏幕高度（像素）：" + realH);
//        Log.d("h_bl", "marginTop：" + rH);
//        Log.d("h_bl", "marginRight：" + rW);
    }

    public void clickImg(View view) {
        cnt--;
        AtCounter.setText(cnt+"");
        if(cnt == 0){
            cnt = 30;
            AtRemain.setVisibility(View.INVISIBLE);
            AtCounter.setVisibility(View.INVISIBLE);
            aim_player_img.setVisibility(View.INVISIBLE);
            aim_image.setVisibility(View.VISIBLE);
            title.setText("Average time per target is "+(int)((SystemClock.elapsedRealtime() - startTime)/30)+"ms");
            second_title.setText("Click the target above to try again");
            head.setVisibility(View.INVISIBLE);
            title.setVisibility(View.VISIBLE);
            second_title.setVisibility(View.VISIBLE);
        }
        else{
            rW = (int)(Math.random()*(realW-150))+10;
            rH = (int)(Math.random()*(realH-150))+10;
            c = new ConstraintSet();
            c.clone(AtLayout);
            c.connect(aim_player_img.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,rW);
            c.connect(aim_player_img.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,rH);
            c.applyTo(AtLayout); // set new constraints
        }
    }
}