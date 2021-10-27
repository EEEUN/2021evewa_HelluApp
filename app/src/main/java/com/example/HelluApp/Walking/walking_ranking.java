package com.example.HelluApp.Walking;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.HelluApp.Community.community_message;
import com.example.HelluApp.Community.community_user;
import com.example.HelluApp.Community.users_model;
import com.example.HelluApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class walking_ranking extends Fragment {
    private String Nickname;
    private String Email;
    private String Profile;
    private String Uid;
    private String Walking;
    private List<String> users_uids = new ArrayList<>();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private String currentUid = user.getUid();
    private String check = "";

    int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_walking_ranking,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new walking_ranking.WalkingRankingRecyclerViewAdapter());


        return view;
    }

    class WalkingRankingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public List<users_model> users_models;
        //users_model users_models2;

        public WalkingRankingRecyclerViewAdapter(){
            users_models = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users_models.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                        if(!Objects.equals(snapshot.getKey(), "0o8pHc0N1qS3FRZHy2UfD4z1HO82")){
                            users_uids.add(snapshot.getKey());
                            users_models.add(snapshot.child(users_uids.get(i)).getValue(users_model.class));

                            i++;
                        }
                    }
                    notifyDataSetChanged();
                    for(int k = 0;k < users_uids.size(); k++){
                        if(users_uids.get(k).equals(currentUid)) {
                            users_uids.remove(currentUid);
                            users_uids.add(0, currentUid);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item,parent,false);
            return new walking_ranking.WalkingRankingRecyclerViewAdapter.CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position){
            FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Nickname = dataSnapshot.child(users_uids.get(position)).child("Nickname").getValue(String.class);
                    Email = dataSnapshot.child(users_uids.get(position)).child("Email").getValue(String.class);
                    Profile = dataSnapshot.child(users_uids.get(position)).child("ProfileUrl").getValue(String.class);
                    Uid = dataSnapshot.child(users_uids.get(position)).child("Uid").getValue(String.class);
                    Walking = dataSnapshot.child(users_uids.get(position)).child("Walking").getValue(String.class);
                    Glide.with(holder.itemView.getContext())
                            .load(Profile).apply(new RequestOptions().circleCrop()).into(((walking_ranking.WalkingRankingRecyclerViewAdapter.CustomViewHolder) holder).imageView);
                    ((walking_ranking.WalkingRankingRecyclerViewAdapter.CustomViewHolder) holder).textView1.setText(Nickname);
                    ((walking_ranking.WalkingRankingRecyclerViewAdapter.CustomViewHolder) holder).textView2.setText(Walking+" 걸음");
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view){
                            if (users_uids.get(position) == currentUid) {
                                Intent intent = new Intent(view.getContext(), walking_ranking_checking.class);
                                ActivityOptions activityOptions = null;
                                activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromleft, R.anim.fromleft);
                                startActivity(intent, activityOptions.toBundle());
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        @Override
        public int getItemCount(){
            return users_models.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView1;
            public TextView textView2;

            public CustomViewHolder(View view){
                super(view);

                imageView = (ImageView) view.findViewById(R.id.rank_profile);
                textView1 = (TextView) view.findViewById(R.id.rank_name);
                textView2 = (TextView) view.findViewById(R.id.rank_num);
            }
        }
    }

}