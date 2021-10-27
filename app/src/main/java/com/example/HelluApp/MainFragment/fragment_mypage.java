package com.example.HelluApp.MainFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.HelluApp.R;
import com.example.HelluApp.User;
import com.example.HelluApp.login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_mypage extends Fragment {

    //로그아웃 버튼 선언
    Button logout_button;
    Button change_button;
    //CircleImageView profile_img;

    //firebase auth object 가져와서 선언

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference("User");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();


    //fragment_mypage 화면을 보여줌
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment 레이아웃에 프래그먼트 뿌리기
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        //View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        //마이페이지에 무슨 이메일로 로그인했는지 보여줌(textviewUserEmail에 찍어줌)
        TextView textViewUserEmail = view.findViewById(R.id.textviewUserEmail);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser userEmail = firebaseAuth.getCurrentUser();
        textViewUserEmail.setText(userEmail.getEmail()+"으로 로그인");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        String Uid = user.getUid();

        //firebase 정의
        rDatabase = FirebaseDatabase.getInstance().getReference("User");

        //마이페이지에 무슨 닉네임으로 로그인했는지 보여줌(textviewviewUserName에 찍어줌)
        TextView textViewUserName = view.findViewById(R.id.textviewUserName);
        CircleImageView profile_img = view.findViewById(R.id.iv_profile);

        //loadData();
        if (user != null){
            rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
                //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
                //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user1 = dataSnapshot.getValue(User.class);

                    //값 받아오기
                    String Nickname = user1.Nickname;

                    //텍스트뷰에 받아온 문자열 대입하기
                    textViewUserName.setText("닉네임: " + Nickname);
                    Picasso.get().load(user1.ProfileUrl).into(profile_img);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
                }
            });
        }

        //로그아웃 버튼
        logout_button = (Button) view.findViewById(R.id.logout_button);
        change_button = (Button) view.findViewById(R.id.change_button);

        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        //유저가 있다면, null이 아니면 계속 진행
        if(firebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            Intent intent = new Intent(view.getContext(), login.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                firebaseAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(view.getContext(), login.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), MypageChange.class);
                startActivity(intent);
            }
        });

        return view;
    }

    void loadData(){
        SharedPreferences preferences=this.getActivity().getSharedPreferences("photo", Context.MODE_PRIVATE);
        rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
            //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
            //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                user1.ProfileUrl=preferences.getString("profileUrl", null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
    //fragment 생성할때 생긴 코드
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
