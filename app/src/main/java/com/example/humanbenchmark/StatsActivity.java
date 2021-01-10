package com.example.humanbenchmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    private TextView name;
    private TextView score;
    private TextView perc;
    private ImageButton play_button;
    private LineChart lineChart;

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
                switch (pos) {
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
                        Toast.makeText(StatsActivity.this, "SomeThing Wrong ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3,4,5,6,1,0,0,2));
        initLineChart(list);
    }

    private void bindViews() {
        name = findViewById(R.id.name);
        score = findViewById(R.id.score);
        perc = findViewById(R.id.perc);
        play_button = findViewById(R.id.play_button);
        lineChart = findViewById(R.id.lineChart);
    }
        /**
     * 初始化曲线图表
     *
     * @param list 数据集
     */
    private void initLineChart(final List<Integer> list)
    {
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            entries.add(new Entry(i, (float) list.get(i)));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#F15A4A"));
        //线宽度
        lineDataSet.setLineWidth(1.6f);
        //不显示圆点
//        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircles(true);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        LineData data = new LineData(lineDataSet);
        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据");
        //折线图不显示数值
        data.setDrawValues(false);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(list.size() / 6, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float) list.size());
        //不显示网格线
        xAxis.setDrawGridLines(false);
        // 标签倾斜

//        xAxis.setLabelRotationAngle(45);
        //设置X轴值为字符串
//        xAxis.setValueFormatter(new IAxisValueFormatter()
//        {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis)
//            {
//                int IValue = (int) value;
//                CharSequence format = DateFormat.format("MM/dd",
//                        System.currentTimeMillis()-(long)(list.size()-IValue)*24*60*60*1000);
//                return format.toString();
//            }
//        });
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(false);
        //设置Y轴坐标之间的最小间隔
        yAxis.setGranularity(1);
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        yAxis.setLabelCount(Collections.max(list) + 2, false);
        //设置从Y轴值
        yAxis.setAxisMinimum(0f);
        //+1:y轴多一个单位长度，为了好看
        yAxis.setAxisMaximum(Collections.max(list) + 1);

        //y轴
//        yAxis.setValueFormatter(new IAxisValueFormatter()
//        {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis)
//            {
//                int IValue = (int) value;
//                return String.valueOf(IValue);
//            }
//        });
        //图例：得到Lengend
        Legend legend = lineChart.getLegend();
        //隐藏Lengend
        legend.setEnabled(false);
        //隐藏描述

        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
        //折线图点的标记
//        MyMarkerView mv = new MyMarkerView(this);
//        lineChart.setMarker(mv);
        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
    }
}