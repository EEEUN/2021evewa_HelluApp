package com.example.HelluApp.Walking;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HelluApp.R;

import java.util.ArrayList;

//fragment_walking_ranking.xml과 walking_ranking_item.xml을 이어주는 어댑터

public class walking_ranking_adapter extends RecyclerView.Adapter<walking_ranking_adapter.ViewHolder> {

    private ArrayList<walking_ranking_item> UserList;

    @NonNull
    @Override
    //onCreateViewHolder(): RecyclerView는 ViewHolder를 새로 만들어야 할 때마다 이 메서드를 호출합니다.
    public walking_ranking_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    //onBindViewHolder(): RecyclerView는 ViewHolder를 데이터와 연결할 때 이 메서드를 호출합니다.
    public void onBindViewHolder(@NonNull walking_ranking_adapter.ViewHolder holder, int position) {
        holder.onBind(UserList.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFriendList(ArrayList<walking_ranking_item> list) {
        this.UserList = list;
        notifyDataSetChanged();
    }

    @Override
    //getItemCount(): RecyclerView는 데이터 세트 크기를 가져올 때 이 메서드를 호출합니다.
    public int getItemCount() {
        return UserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank_num;
        ImageView rank_profile;
        TextView rank_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rank_num = itemView.findViewById(R.id.rank_num);
            rank_profile = itemView.findViewById(R.id.rank_profile);
            rank_name = itemView.findViewById(R.id.rank_name);
        }

        void onBind(walking_ranking_item item) {
            rank_num.setText(Integer.toString(item.getRank_num()));
            rank_profile.setImageResource(item.getRank_ResourceId());
            rank_name.setText(item.getRank_Name());
        }
    }
}