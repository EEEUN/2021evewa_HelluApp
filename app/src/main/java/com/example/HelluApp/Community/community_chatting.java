package com.example.HelluApp.Community;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.HelluApp.DailyStamp.daily_stamp_write;
import com.example.HelluApp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class community_chatting extends Fragment {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

    int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_community_chatting,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new community_chatting.CommunityChattingRecyclerViewAdapter());


        return view;
    }

    class CommunityChattingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public List<String> chatting_room;
        public List<String> chatting_photo;
        public List<String> chatting_id;
        //users_model users_models2;

        public CommunityChattingRecyclerViewAdapter(){
            chatting_room = new ArrayList<>();
            chatting_photo = new ArrayList<>();
            chatting_id = new ArrayList<>();
            chatting_room.add("조깅 운동 클럽");
            chatting_room.add("1:1 트레이너 대화방");
            chatting_room.add("다이어트 종합반");
            chatting_photo.add("https://cdn.pixabay.com/photo/2016/07/11/03/57/jogging-1509003_960_720.jpg");
            chatting_photo.add("https://mblogthumb-phinf.pstatic.net/20150417_264/ninevincent_14291992723052lDb3_JPEG/kakao_11.jpg?type=w2");
            chatting_photo.add("https://cdn.pixabay.com/photo/2017/09/16/19/21/salad-2756467_960_720.jpg");
            chatting_id.add("chat");
            chatting_id.add("chat2");
            chatting_id.add("chat3");
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatting_list_item,parent,false);
            return new community_chatting.CommunityChattingRecyclerViewAdapter.CustomViewHolder(view);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position){
            FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Glide.with(holder.itemView.getContext())
                                .load(chatting_photo.get(position)).apply(new RequestOptions().circleCrop()).into(((community_chatting.CommunityChattingRecyclerViewAdapter.CustomViewHolder) holder).imageView);
                    ((CustomViewHolder) holder).textView_name.setText(chatting_room.get(position));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view){
                            Intent intent = new Intent(view.getContext(), community_message.class);
                            intent.putExtra("page_num", chatting_id.get(position));
                            ActivityOptions activityOptions = null;
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
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
            return chatting_room.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView_name;

            public CustomViewHolder(View view){
                super(view);

                imageView = (ImageView) view.findViewById(R.id.user_photo);
                textView_name = (TextView) view.findViewById(R.id.user_name);
            }
        }
    }

}