package com.example.humanbenchmark.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.humanbenchmark.AtActivity;
import com.example.humanbenchmark.NmActivity;
import com.example.humanbenchmark.R;
import com.example.humanbenchmark.RtActivity;
import com.example.humanbenchmark.TypingTestActivity;

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
                Log.d(TAG, String.valueOf(data.getPos()));
                switch(data.getPos()){
                    case 0://react-time
                        Intent intent = new Intent(getActivity(), RtActivity.class);
                        startActivity(intent);
                        break;
                    case 1://aim_trainer
                        Intent aimIntent = new Intent(getActivity(), AtActivity.class);
                        startActivity(aimIntent);
                        break;
                    case 2://typing test
                        Intent intent1 = new Intent(getActivity(), TypingTestActivity.class);
                        startActivity(intent1);
                        break;
                    case 3:
                        Intent nmIntent = new Intent(getActivity(), NmActivity.class);
                        startActivity(nmIntent);
                        break;
                    default:
                        break;
                }
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);
        return root;
    }
    private void prepareCards(){
        int pos = 0;
        Card card = new Card("Reaction Time",R.drawable.react_time,pos++);
        cardList.add(card);
        card = new Card("Aim Trainer",R.drawable.aim_trainer,pos++);
        cardList.add(card);
        card = new Card("Typing",R.drawable.typing,pos++);
        cardList.add(card);
        card = new Card("Number Memory",R.drawable.number_memory,pos++);
        cardList.add(card);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}