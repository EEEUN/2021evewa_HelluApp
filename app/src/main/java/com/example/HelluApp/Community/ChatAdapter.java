package com.example.HelluApp.Community;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.HelluApp.R;
import com.example.HelluApp.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {
    private DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference("User");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();
    private static String name = "";

    ArrayList<message_model> messageItems;
    LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<message_model> messageItems, LayoutInflater layoutInflater) {
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        message_model item = messageItems.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView=null;
        //메세지가 내 메세지인지??
        rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
            //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
            //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                name = user1.Nickname;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
        if(item.getName().equals(name)){
            itemView= layoutInflater.inflate(R.layout.chatting_right_item,viewGroup,false);
        }else{
            itemView= layoutInflater.inflate(R.layout.chatting_left_item,viewGroup,false);
        }

        //만들어진 itemView에 값들 설정
        ImageView iv= (ImageView) itemView.findViewById(R.id.profile);
        TextView tvName= (TextView) itemView.findViewById(R.id.name);
        TextView tvMsg= (TextView) itemView.findViewById(R.id.msg);
        TextView tvTime= (TextView) itemView.findViewById(R.id.time);
        try {
            tvName.setText(item.getName());
            tvMsg.setText(item.getMessage());
            tvTime.setText(item.getTime());
            Log.d("망함", item.getName());

            Glide.with(itemView).load(item.getPofileUrl()).apply(new RequestOptions().circleCrop()).into(iv);
        }catch (NullPointerException e){
            Log.d("망함", "이거 왜 안 돼"+e);
        }

        return itemView;
    }
}