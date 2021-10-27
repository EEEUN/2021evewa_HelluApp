
package com.example.HelluApp.DailyStamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.HelluApp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class daily_stamp extends AppCompatActivity {

    //하단 네비게이션
    private BottomNavigationView dBottomNV;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    TextView tv_recommended_kcal;
    TextView tv_today_kcal;
    String getDate;
    int CODE_ALBUM_REQUEST = 111;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    //매일 인증.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_stamp);

        // 헤헤 성공했어 헤헤헿
        recyclerView = findViewById(R.id.daily_recyclerview); // 아디 연결
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // Post 객체를 담을 어레이 리스트 (어댑터쪽으로)
        tv_recommended_kcal = findViewById(R.id.recommended_kcal_textview);
        tv_today_kcal = findViewById(R.id.today_kcal_textview);


        String Uid = user.getUid(); //각 사용자 Uid로 구별
        mAuth = FirebaseAuth.getInstance(); //파이어베이스 사용자 연동
        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("User_Write"); // DB 테이블 연결
        databaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                    Post post = snapshot.getValue(Post.class); // 만들어뒀던 Post 객체에 데이터를 담는다.
                    tv_today_kcal.setText(post.kcal);
                    arrayList.add(post); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException()); // 에러문 출력
            }
        });

        //하루 권장 칼로리(아직 손 안댐)
        databaseReference = database.getReference("User_Plan"); // DB 테이블 연결
        databaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                String basalMeta = dataSnapshot.child("기초대사량").getValue(String.class);
                tv_recommended_kcal.setText(basalMeta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException()); // 에러문 출력
            }
        });

        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
        //여기까지 새로 추가한 부분


        //하단 네비게이션 화면을 선택하면 실행됨
        dBottomNV = findViewById(R.id.daily_bottom_navigation);
        dBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });

    }

    //BottomNavigation 화면 전환 ex) 카메라 아이콘 누르면 카메라창 열림
    private void BottomNavigate(int id) {

        if (id == R.id.daily_camera) {
            Intent intent = new Intent(getApplicationContext(), daily_stamp_camera.class);
            startActivity(intent);
        } else if (id == R.id.daily_write) {
            Intent intent = new Intent(getApplicationContext(), daily_stamp_write.class);
            startActivity(intent);
        } else {
            // 네비게이션 바에서 갤러리를 누르면 바로 갤러리로 연동하는걸로 코드 수정함.
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent, 1);
            /*
            Intent intent = new Intent(getApplicationContext(), daily_stamp_gallery.class);
            startActivity(intent);
             */

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, CODE_ALBUM_REQUEST);
        }
    }
}


