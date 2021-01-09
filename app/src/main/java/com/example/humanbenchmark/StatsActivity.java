package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class StatsActivity extends AppCompatActivity {

    private TextView name;
    private TextView score;
    private TextView perc;
    private ImageButton play_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Bundle data = getIntent().getBundleExtra("data");
        bindViews();
        int pos = data.getInt("pos");
        String name_s = data.getString("name");
        String score_s = data.getString("score");
        String percent_s = data.getString("percent");
        name.setText(name_s);
        score.setText(score_s);
        perc.setText(percent_s);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                switch (pos){
                    case 0:
                        startActivity(new Intent(StatsActivity.this, RtActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(StatsActivity.this, AtActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(StatsActivity.this, TypingTestActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(StatsActivity.this, NmActivity.class));
                        break;
                    default:
                        Toast.makeText(StatsActivity.this,"SomeThing Wrong ",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void bindViews(){
        name = findViewById(R.id.name);
        score = findViewById(R.id.score);
        perc = findViewById(R.id.perc);
        play_button = findViewById(R.id.play_button);
    }
}