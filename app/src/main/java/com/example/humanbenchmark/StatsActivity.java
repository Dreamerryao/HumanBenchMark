package com.example.humanbenchmark;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.humanbenchmark.util.APIUtils;
import com.example.humanbenchmark.util.SharedHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StatsActivity extends AppCompatActivity {

    private TextView name;
    private TextView score;
    private TextView perc;
//    private ImageButton play_button;
    private LineChart lineChart;
    private int pos;
    private SharedHelper sh;
    private Context mContext;
    private List<Integer> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Bundle data = getIntent().getBundleExtra("data");
        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);
        bindViews();
        pos = data.getInt("pos");
        String name_s = data.getString("name");
        String score_s = data.getString("score");
        String percent_s = data.getString("percent");
        name.setText(name_s);
        score.setText(score_s);
        perc.setText(percent_s);
//        play_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                switch (pos) {
//                    case 0:
//                        startActivity(new Intent(StatsActivity.this, RtActivity.class));
//                        break;
//                    case 1:
//                        startActivity(new Intent(StatsActivity.this, AtActivity.class));
//                        break;
//                    case 2:
//                        startActivity(new Intent(StatsActivity.this, TypingTestActivity.class));
//                        break;
//                    case 3:
//                        startActivity(new Intent(StatsActivity.this, NmActivity.class));
//                        break;
//                    default:
//                        Toast.makeText(StatsActivity.this, "SomeThing Wrong ", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
        InitPost();
//        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3,4,5,6,1,0,0,2));
//        initLineChart(list);
    }

    private void bindViews() {
        name = findViewById(R.id.name);
        score = findViewById(R.id.score);
        perc = findViewById(R.id.perc);
//        play_button = findViewById(R.id.play_button);
        lineChart = findViewById(R.id.lineChart);
    }

    private void InitPost() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                int msg_what = 0x006;
                String stat_url = APIUtils.STATS_URL;

                //拼装url
                //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                try {
                    String lastUrl = stat_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8")+"&testId="+URLEncoder.encode((pos+1)+"", "utf-8");
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
                        if(data.equals("")){
                            list = new ArrayList<>(Arrays.asList(0));
                        }else{
                            int[] result = Arrays.stream(data.split("\n"))
                                    .mapToInt(Integer::valueOf)
                                    .toArray();
                            list = Arrays.stream(result).boxed().collect(Collectors.toList());
                        }

                        msg_what = 0x005;
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

                case 0x005:
                    initLineChart(list);
                    break;
                case 0x006:
                    Toast.makeText(StatsActivity.this, "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(StatsActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(StatsActivity.this, "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };

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
//        yAxis.setLabelCount(Collections.max(list) + 2, false);
        yAxis.setLabelCount(10, true);
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