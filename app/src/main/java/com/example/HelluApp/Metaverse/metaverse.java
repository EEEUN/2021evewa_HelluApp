package com.example.HelluApp.Metaverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.HelluApp.DailyStamp.daily_stamp;
import com.example.HelluApp.R;

public class metaverse extends AppCompatActivity {
    RecyclerView recyclerView;
    metaverse_noteAdapter adapter;
    public static final int REQUEST_CODE_MENU = 101;

    //메타버스.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metaverse);



       recyclerView = findViewById(R.id.meta_recylerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new metaverse_noteAdapter();

        adapter.addItem(new metaverse_note(0, "부담 없는 다이어트", "쿼카 선생님"));
        adapter.addItem(new metaverse_note(1, "한 달 집중해서 다이어트", "해달 선생님"));
        adapter.addItem(new metaverse_note(2, "근육량 늘리기", "고양이 선생님"));
        adapter.addItem(new metaverse_note(3, "운동 습관 만들기", "강아지 선생님"));
        adapter.addItem(new metaverse_note(4, "스트레칭반", "다람쥐 선생님"));
        adapter.addItem(new metaverse_note(5, "운동 체험반", "카피바라 선생님"));


        recyclerView.setAdapter(adapter);

        //메타버스 "방 입장하기"을 누르면 운동 설명+링크 입력창+입력버튼 나타나는 화면 띄워주기
//        Button button = findViewById(R.id.trainingClassEnterButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), metaverse_trainingroom.class);
//                //startActivity(intent);
//                startActivityForResult(intent, REQUEST_CODE_MENU);
//            }
//        });

    }

}