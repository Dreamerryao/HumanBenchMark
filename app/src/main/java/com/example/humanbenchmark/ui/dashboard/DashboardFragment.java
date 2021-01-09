package com.example.humanbenchmark.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanbenchmark.AtActivity;
import com.example.humanbenchmark.NmActivity;
import com.example.humanbenchmark.R;
import com.example.humanbenchmark.RtActivity;
import com.example.humanbenchmark.StatsActivity;
import com.example.humanbenchmark.TypingTestActivity;
import com.example.humanbenchmark.ui.dashboard.RecyclerViewAdapter;
import com.example.humanbenchmark.ui.notifications.Card;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private LineChart lineChart;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<stats_item> statsList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        statsList = new ArrayList<>();
        recyclerView = (RecyclerView)root.findViewById(R.id.dashboard_recyclerView);
        Log.d("Dreamerryao",statsList.toString());
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),statsList);
        prepareStats();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(new ClickListener<ImageButton>(){
            @Override
            public void onItemClick(stats_item item) {
                Bundle data = new Bundle();
                data.putInt("pos",item.getPos());
                data.putString("name",item.getName());
                data.putString("score",item.getScore());
                data.putString("percent","超过了"+item.getPerc()+"的人");
                data.putIntegerArrayList("statsArray",new ArrayList<>(Arrays.asList(1, 2, 3,4,5,6,1,0,0,2)));
                Log.d("Dreamerryao",item.getPos()+"");
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
//        lineChart = (LineChart)root.findViewById(R.id.lineChart);
//        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3,4,5,6,1,0,0,2));
//        initLineChart(list);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
    private void prepareStats(){
        int pos = 0;
        stats_item item = new stats_item("Reaction Time",pos++,"300ms","??");
        statsList.add(item);
        stats_item ATs_item = new stats_item("Aim Trainer",pos++,"297ms","80%");
        statsList.add(ATs_item);
        stats_item tp_item = new stats_item("Typing",pos++,"??","??");
        statsList.add(tp_item);
        stats_item nm_item = new stats_item("Number Memory",pos++,"??","??");
        statsList.add(nm_item);
        recyclerViewAdapter.notifyDataSetChanged();
    }
//    /**
//     * 初始化曲线图表
//     *
//     * @param list 数据集
//     */
//    private void initLineChart(final List<Integer> list)
//    {
//        //显示边界
//        lineChart.setDrawBorders(false);
//        //设置数据
//        List<Entry> entries = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++)
//        {
//            entries.add(new Entry(i, (float) list.get(i)));
//        }
//        //一个LineDataSet就是一条线
//        LineDataSet lineDataSet = new LineDataSet(entries, "");
//        //线颜色
//        lineDataSet.setColor(Color.parseColor("#F15A4A"));
//        //线宽度
//        lineDataSet.setLineWidth(1.6f);
//        //不显示圆点
////        lineDataSet.setDrawCircles(false);
//        lineDataSet.setDrawCircles(true);
//        //线条平滑
//        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
//        //设置折线图填充
//        lineDataSet.setDrawFilled(true);
//        LineData data = new LineData(lineDataSet);
//        //无数据时显示的文字
//        lineChart.setNoDataText("暂无数据");
//        //折线图不显示数值
//        data.setDrawValues(false);
//        //得到X轴
//        XAxis xAxis = lineChart.getXAxis();
//        //设置X轴的位置（默认在上方)
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        //设置X轴坐标之间的最小间隔
//        xAxis.setGranularity(1f);
//        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
//        xAxis.setLabelCount(list.size() / 6, false);
//        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
//        xAxis.setAxisMinimum(0f);
//        xAxis.setAxisMaximum((float) list.size());
//        //不显示网格线
//        xAxis.setDrawGridLines(false);
//        // 标签倾斜
////        xAxis.setLabelRotationAngle(45);
//        //设置X轴值为字符串
////        xAxis.setValueFormatter(new IAxisValueFormatter()
////        {
////            @Override
////            public String getFormattedValue(float value, AxisBase axis)
////            {
////                int IValue = (int) value;
////                CharSequence format = DateFormat.format("MM/dd",
////                        System.currentTimeMillis()-(long)(list.size()-IValue)*24*60*60*1000);
////                return format.toString();
////            }
////        });
//        //得到Y轴
//        YAxis yAxis = lineChart.getAxisLeft();
//        YAxis rightYAxis = lineChart.getAxisRight();
//        //设置Y轴是否显示
//        rightYAxis.setEnabled(false); //右侧Y轴不显示
//        //设置y轴坐标之间的最小间隔
//        //不显示网格线
//        yAxis.setDrawGridLines(false);
//        //设置Y轴坐标之间的最小间隔
//        yAxis.setGranularity(1);
//        //设置y轴的刻度数量
//        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
//        yAxis.setLabelCount(Collections.max(list) + 2, false);
//        //设置从Y轴值
//        yAxis.setAxisMinimum(0f);
//        //+1:y轴多一个单位长度，为了好看
//        yAxis.setAxisMaximum(Collections.max(list) + 1);
//
//        //y轴
////        yAxis.setValueFormatter(new IAxisValueFormatter()
////        {
////            @Override
////            public String getFormattedValue(float value, AxisBase axis)
////            {
////                int IValue = (int) value;
////                return String.valueOf(IValue);
////            }
////        });
//        //图例：得到Lengend
//        Legend legend = lineChart.getLegend();
//        //隐藏Lengend
////        legend.setEnabled(false);
//        legend.setEnabled(true);
//        //隐藏描述
//
//        Description description = new Description();
//        description.setEnabled(false);
//        lineChart.setDescription(description);
//        //折线图点的标记
////        MyMarkerView mv = new MyMarkerView(this);
////        lineChart.setMarker(mv);
//        //设置数据
//        lineChart.setData(data);
//        //图标刷新
//        lineChart.invalidate();
//    }
}