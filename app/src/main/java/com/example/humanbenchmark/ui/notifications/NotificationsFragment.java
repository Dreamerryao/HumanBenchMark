package com.example.humanbenchmark.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanbenchmark.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private final String TAG = "wow";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Card> cardList;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        cardList = new ArrayList<>();

        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        Log.d(TAG,cardList.toString());
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(),cardList);
        prepareCards();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        recyclerViewAdapter.setOnItemClickListener(new ClickListener<Card>(){
            @Override
            public void onItemClick(Card data) {
                Toast.makeText(getActivity(), data.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
        return root;
    }
    private void prepareCards(){
        Card card = new Card("Reaction Time",R.drawable.react_time);
        cardList.add(card);
        card = new Card("Aim Trainer",R.drawable.aim_trainer);
        cardList.add(card);
        card = new Card("Typing",R.drawable.typing);
        cardList.add(card);
        card = new Card("Number Memory",R.drawable.number_memory);
        cardList.add(card);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}