package com.example.humanbenchmark.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanbenchmark.R;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private Context context;
    private List<stats_item> statsList;
    private ClickListener<ImageButton> clickListener;

    RecyclerViewAdapter(Context context,List<stats_item> statsList){
        this.context = context;//上下文
        this.statsList = statsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final stats_item item = statsList.get(position);
        final ImageButton img = holder.image;
        holder.title.setText(item.getName());
        holder.score.setText(item.getScore());
        holder.perc.setText("超过了"+item.getPerc()+"的人");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(item);
            }
        });


    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    public void setOnItemClickListener(ClickListener<ImageButton> statsClickListener) {
        this.clickListener = statsClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageButton image;
        private TextView score;
        private TextView perc;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_name);
            image = itemView.findViewById(R.id.item_button);
            score = itemView.findViewById(R.id.item_score);
            perc = itemView.findViewById(R.id.item_perc);
        }
    }
}

interface ClickListener<T> {
    void onItemClick(stats_item data);
}