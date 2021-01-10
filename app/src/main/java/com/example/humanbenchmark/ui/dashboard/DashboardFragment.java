package com.example.humanbenchmark.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanbenchmark.AtActivity;
import com.example.humanbenchmark.MainActivity;
import com.example.humanbenchmark.NmActivity;
import com.example.humanbenchmark.R;
import com.example.humanbenchmark.RtActivity;
import com.example.humanbenchmark.SignUpActivity;
import com.example.humanbenchmark.StatsActivity;
import com.example.humanbenchmark.TypingTestActivity;
import com.example.humanbenchmark.ui.dashboard.RecyclerViewAdapter;
import com.example.humanbenchmark.ui.notifications.Card;
import com.example.humanbenchmark.util.APIUtils;
import com.example.humanbenchmark.util.SharedHelper;
import com.github.mikephil.charting.charts.LineChart;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DashboardFragment extends Fragment {

    private LineChart lineChart;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<stats_item> statsList;
    private TextView user_name;
    private TextView login_time;
    private Context mContext;
    private SharedHelper sh;
    private boolean firstPost;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getActivity().getApplicationContext();
        sh = new SharedHelper(mContext);
        firstPost = true;
        statsList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.dashboard_recyclerView);
        user_name = root.findViewById(R.id.title);
        login_time = root.findViewById(R.id.title_time);
        user_name.setText(sh.get(SharedHelper.USERNAME));
        login_time.setText(sh.get(SharedHelper.LOGINTIME));
        Log.d("Dreamerryao", statsList.toString());
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), statsList);
//        prepareStats();
        int pos = 0;
        stats_item item = new stats_item("Reaction Time", pos++, "??", "??");
        statsList.add(item);
        stats_item ATs_item = new stats_item("Aim Trainer", pos++, "??", "??");
        statsList.add(ATs_item);
        stats_item tp_item = new stats_item("Typing", pos++, "??", "??");
        statsList.add(tp_item);
        stats_item nm_item = new stats_item("Number Memory", pos++, "??", "??");
        statsList.add(nm_item);
        InitPost();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter.setOnItemClickListener(new ClickListener<ImageButton>() {
            @Override
            public void onItemClick(stats_item item) {
                Bundle data = new Bundle();
                data.putInt("pos", item.getPos());
                data.putString("name", item.getName());
                data.putString("score", item.getScore());
                data.putString("percent", "超过了" + item.getPerc() + "的人");
                data.putIntegerArrayList("statsArray", new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 1, 0, 0, 2)));
                Log.d("Dreamerryao", item.getPos() + "");
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);

        return root;
    }

    private void InitPost() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                int msg_what = 0x006;
                String login_url = APIUtils.GET_URL;

                //拼装url
                //URLEncoder.encode对汉字进行编码，服务器进行解码设置，解决中文乱码
                try {
                    String lastUrl = login_url + "?userId=" + URLEncoder.encode(sh.get(SharedHelper.USERID), "utf-8");
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
                        int[] result = Arrays.stream(data.split("\n"))
                                .mapToInt(Integer::valueOf)
                                .toArray();

                        statsList.get(0).setScore(result[0] == 999999999 ? "??" : result[0] + "ms");
                        statsList.get(0).setPerc(result[1] + "%");
                        statsList.get(1).setScore(result[2] == 999999999 ? "??" : result[2] + "ms");
                        statsList.get(1).setPerc(result[3] + "%");
                        statsList.get(2).setScore(result[4] + "CPM");
                        statsList.get(2).setPerc(result[5] + "%");
                        statsList.get(3).setScore("Level" + result[6]);
                        statsList.get(3).setPerc(result[7] + "%");

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
//                    Toast.makeText(getActivity(), "获取成绩列表成功", Toast.LENGTH_SHORT).show();
                    recyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 0x006:
                    Toast.makeText(getActivity(), "无法解析的响应", Toast.LENGTH_SHORT).show();
                    break;
                case 0x007:
                    Toast.makeText(getActivity(), "服务器连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(getActivity(), "Invalid msg.what", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        ;
    };

    private void prepareStats() {
        int pos = 0;
        stats_item item = new stats_item("Reaction Time", pos++, "??", "??");
        statsList.add(item);
        stats_item ATs_item = new stats_item("Aim Trainer", pos++, "??", "??");
        statsList.add(ATs_item);
        stats_item tp_item = new stats_item("Typing", pos++, "??", "??");
        statsList.add(tp_item);
        stats_item nm_item = new stats_item("Number Memory", pos++, "??", "??");
        statsList.add(nm_item);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e("Drea","resume了sb");
        InitPost();
    }


}