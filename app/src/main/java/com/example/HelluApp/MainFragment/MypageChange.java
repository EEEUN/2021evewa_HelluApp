package com.example.HelluApp.MainFragment;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.HelluApp.User;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MypageChange extends AppCompatActivity {

    private static final String TAG = "daily_stamp_write";

    Button change_button;
    EditText nickname_text;
    CircleImageView profile_img;

    Uri imgUri;//선택한 프로필 이미지 경로 Uri

    private DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference("User");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
    private String Uid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_change);

        nickname_text = (EditText)findViewById(R.id.et_name);
        change_button = (Button) findViewById(R.id.change_button);
        profile_img = (CircleImageView) findViewById(R.id.iv_profile);

        loadData();
        rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
            //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
            //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                nickname_text.setText(user1.Nickname);
                Picasso.get().load(user1.ProfileUrl).into(profile_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
        change_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( nickname_text.getText().toString().length() == 0 ) {
                    Toast.makeText(MypageChange.this, "변경할 닉네임을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    String change_nickname = nickname_text.getText().toString();
                    DatabaseReference usersRef = rDatabase.child(Uid);
                    Map<String, Object> nickUpdates = new HashMap<>();
                    nickUpdates.put("Nickname", change_nickname);
                    usersRef.updateChildren(nickUpdates);

                    saveData();
                }
            }
        });
        profile_img.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
    }
    /*public void clickImage(View view) {
        //프로필 이미지 선택하도록 Gallery 앱 실행
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                imgUri = data.getData();
                Log.d(TAG, "uri:" + imgUri);

                //Glide.with(this).load(imgUri).into(ivProfile);
                //Glide는 이미지를 읽어와서 보여줄때 내 device의 외장메모리에 접근하는 퍼미션이 요구됨.
                //(퍼미션이 없으면 이미지가 보이지 않음.)
                //Glide를 사용할 때는 동적 퍼미션 필요함.

                //Picasso 라이브러리는 퍼미션 없어도 됨.
                Picasso.get().load(imgUri).into(profile_img);

            }
        }
    }
    void saveData(){
        //이미지를 선택하지 않았을 수도 있으므로
        if(imgUri==null) return;

        //Firebase storage에 이미지 저장하기 위해 파일명 만들기(날짜를 기반으로)
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf= new SimpleDateFormat("yyyMMddhhmmss"); //20191024111224
        String fileName= sdf.format(new Date())+".png";

        //Firebase storage에 저장하기
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        //파일 업로드
        UploadTask uploadTask=imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //이미지 업로드가 성공되었으므로...
                //곧바로 firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //파라미터로 firebase의 저장소에 저장되어 있는
                        //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
                        rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
                            //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
                            //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user1 = dataSnapshot.getValue(User.class);
                                //user1.ProfileUrl= uri.toString();
                                Toast.makeText(MypageChange.this, "프로필 저장 완료", Toast.LENGTH_SHORT).show();

                                //1. Firebase Database에 nickName, profileUrl을 저장
                                //firebase DB관리자 객체 소환
                                //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
                                String profile = uri.toString();

                                DatabaseReference usersRef = rDatabase.child(Uid);
                                Map<String, Object> profileUpdates = new HashMap<>();
                                profileUpdates.put("ProfileUrl", profile);
                                usersRef.updateChildren(profileUpdates);

                                        //닉네임을 key 식별자로 하고 프로필 이미지의 주소를 값으로 저장

                                //2. 내 phone에 nickName, profileUrl을 저장
                                SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
                                SharedPreferences.Editor editor=preferences.edit();

                                editor.putString("nickName",user1.Nickname);
                                editor.putString("profileUrl", user1.ProfileUrl);

                                editor.apply();
                                //저장이 완료되었으니 ChatActivity로 전환
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
                            }
                        });
                    }
                });
            }
        });
    }

    void loadData(){
        SharedPreferences preferences=getSharedPreferences("photo",MODE_PRIVATE);
        rDatabase.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            //리스너는 이벤트 발생 시점에 데이터베이스에서 지정된 위치에 있던 데이터를 포함하는 DataSnapshot을 수신한다.
            //스냅샷에 대해 getValue()를 호출하면 데이터의 자바 객체 표현이 반환된다.
            //해당 위치에 데이터가 없는 경우 getValue()를 호출하면 null이 반환된다.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                user1.Nickname=preferences.getString("nickName", null);
                user1.ProfileUrl=preferences.getString("profileUrl", null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
