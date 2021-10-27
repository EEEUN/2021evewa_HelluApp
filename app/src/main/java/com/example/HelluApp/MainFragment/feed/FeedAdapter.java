package com.example.HelluApp.MainFragment.feed;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.HelluApp.DailyStamp.Post;
import com.example.HelluApp.MainActivity;
import com.example.HelluApp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    private ArrayList<Post> arrayList;
    private feed_recent context;
    int count = 0;

    public FeedAdapter(ArrayList<Post> arrayList, feed_recent context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        FeedViewHolder holder = new FeedViewHolder(view);

        ImageButton starButton = view.findViewById(R.id.star_button);
        TextView starNum = view.findViewById(R.id.tv_starnum);
        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((count%2)==0) {
                    starButton.setImageResource(R.drawable.ic_baseline_star_rate_24);
                    starNum.setText("1");
                    count+=1;
                }else{
                    starButton.setImageResource(R.drawable.ic_baseline_star_outline_24);
                    starNum.setText("0");
                    count+=1;
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {

        //Glide.with(holder.itemView).load(arrayList.get(position).uid).into(holder.iv_profile); //프로필 이미지
        Glide.with(holder.itemView).load(arrayList.get(position).image_path).into(holder.iv_image); //글 이미지
        holder.tv_date.setText(arrayList.get(position).date); //날짜
        //Glide.with(holder.itemView).load(arrayList.get(position).uid).into(holder.iv_profile); //프로필 이미지
        //Glide.with(holder.itemView).load(arrayList.get(position).image_path).into(holder.iv_image); //글 이미지

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://eveproject-d838a.appspot.com");
        StorageReference storageRef = storage.getReference();
        storageRef.child("daily_stamp/" + arrayList.get(position).filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //이미지 로드 성공시
                Glide.with(holder.itemView)
                        .load(uri)
                        .into(holder.iv_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });

        holder.tv_date.setText(arrayList.get(position).date); //날짜
        holder.tv_author.setText(String.valueOf(arrayList.get(position).author)); //글 작성자
        holder.tv_title.setText(arrayList.get(position).title); //제목
        holder.tv_content.setText(String.valueOf(arrayList.get(position).content)); //내용
        //holder.tv_starnum.setText(Integer.valueOf(arrayList.get(position).content)); //별 개수
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        ImageView iv_image;
        TextView tv_title;
        TextView tv_content;
        TextView tv_author;
        TextView tv_date;
        //TextView tv_starnum;    //별 개수 나타내는 TextView

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.iv_image = itemView.findViewById(R.id.iv_image);

            this.tv_date = itemView.findViewById(R.id.tv_date);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_content = itemView.findViewById(R.id.tv_content);
            this.tv_author = itemView.findViewById(R.id.tv_author);
            //this.tv_starnum = itemView.findViewById(R.id.tv_starnum);   //별 개수 나타내는 TextView
        }
    }
}
