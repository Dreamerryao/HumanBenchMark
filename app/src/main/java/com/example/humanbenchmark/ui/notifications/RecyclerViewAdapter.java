package com.example.humanbenchmark.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.humanbenchmark.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private Context context;
    private List<Card> cardList;
    private ClickListener<Card> clickListener;

    RecyclerViewAdapter(Context context,List<Card> cardList){
        this.context = context;//上下文
        this.cardList = cardList;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_adapter_layout,parent,false);
//        View view = View.inflate(context, R.layout.recyclerview_adapter_layout, null);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, final int position) {

        final Card card = cardList.get(position);

        holder.title.setText(card.getName());
        holder.image.setBackgroundResource(card.getImage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(card);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public void setOnItemClickListener(ClickListener<Card> cardClickListener) {
        this.clickListener = cardClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView image;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

interface ClickListener<T> {
    void onItemClick(T data);
}