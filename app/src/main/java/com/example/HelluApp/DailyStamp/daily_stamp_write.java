package com.example.HelluApp.DailyStamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.HelluApp.R;
import com.example.HelluApp.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class daily_stamp_write<daily_recyclerview> extends AppCompatActivity {
    private static final String TAG = "daily_stamp_write";
    private static final String REQUIRED = "Required";
    private Uri filePath;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    FirebaseUser user = mAuth.getCurrentUser();

    String Uid;
    String Author;

    String getDate;
    String Title;
    String Content;
    String Image_path;
    String filename;
    String Kcal;
    String Kcal_today;

    Button save_button;         //매일인증 저장하기 버튼
    Button gallery;             //갤러리 열기 버튼

    RecyclerView daily_recyclerview;
    ImageView daily_imageView;
    TextView tv_recommended_kcal;
    TextView tv_today_kcal;
    int CODE_ALBUM_REQUEST = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);        //이것도 plan_choose.java에 있어서 가져왔습니다.
        setContentView(R.layout.activity_daily_stamp_write);

        daily_recyclerview = findViewById(R.id.daily_recyclerview);

        //버튼을 누르면 갤러리로 넘어가는 코드 by 예린
        gallery = findViewById(R.id.daily_write_image_button);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, CODE_ALBUM_REQUEST);

            }
        });

        save_button = findViewById(R.id.daily_write_save_button);
        save_button.setOnClickListener(view -> {

            //모르지만 예린이 코드 훔쳐오기
            Input_daily();

            //이미지 업로드 부분
            picture_upload();
        });

    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //리사이클러뷰일 때 사용하는 사진 선택. 실패

        daily_imageView = findViewById(R.id.image_select_preview);

        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 111 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Image_path = getRealPathFromUri(filePath);
            Toast.makeText(getApplicationContext(), "이미지가 첨부되었습니다.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            Log.d(TAG, "getRealPathFromUri:" + Image_path);
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                daily_imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Uri -> Path(파일경로)
    //카메라 권한 설정했던 것처럼 앱 설정 들어가서 스토리지 권한 허용해주기!
    private String getRealPathFromUri(Uri uri)
    {
        String[] proj=  {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String url = cursor.getString(columnIndex);
        cursor.close();
        return  url;
    }

    //upload the file
    private void picture_upload() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now) + ".jpg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://eveproject-d838a.appspot.com").child("daily_stamp/" + filename);

            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(taskSnapshot -> {
                        progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                        Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                    })
                    //실패시
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                    })
                    //진행중
                    .addOnProgressListener(taskSnapshot -> {
                        @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                        double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //dialog에 진행률을 퍼센트로 출력해 준다
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    });
        } else {
            //Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void Input_daily() {

        //날짜 저장
        long now = System.currentTimeMillis(); //현재 시간 가져오기
        Date mDate = new Date(now); // Date 형식으로 고치기
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm"); //가지고 싶은 형태
        getDate = simpleDate.format(mDate);

        //제목
        EditText optionTitle = findViewById(R.id.daily_write_title);
        //내용
        EditText optionContent = findViewById(R.id.daily_write_content);

        //칼로리
        EditText optionKcal = findViewById(R.id.daily_write_kcal);

        tv_recommended_kcal = findViewById(R.id.recommended_kcal_textview);
        tv_today_kcal = findViewById(R.id.today_kcal_textview);

        //제목 입력
        if (optionTitle != null) {
            Title = optionTitle.getText().toString().trim();
        } else {
            Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //내용 입력
        if (optionContent != null) {
            Content = optionContent.getText().toString().trim();
        } else {
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //칼로리 입력
        if(optionKcal != null){
            Kcal = optionKcal.getText().toString().trim();
        } else {
            Toast.makeText(this, "칼로리를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        String Uid = user.getUid();
        databaseReference.child("User").child(Uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + Uid + " is unexpectedly null");

                        } else {
                            // Write new post
                            writeNewPost(Uid, user.Nickname, Title, Content, Image_path, getDate, filename, Kcal);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });

    }

    private void writeNewPost(String Uid, String Nickname, String Title, String Content, String Image_path, String getDate, String filename, String Kcal) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = databaseReference.child("User_Write").push().getKey();
        Post post = new Post(Uid, Nickname, Title, Content, Image_path, getDate, filename, Kcal);
        Map<String, Object> postValues = post.posttomap();

        Map<String, Object> childUpdates = new HashMap<>();
        // realtime Database 들어가면 Post는 글만 모아둔 곳
        // 즉, Post : 모든 글 (키 값으로 글 내용을 분류한 것임)
        // Post -> 글 분류 키값 -> 글 내용
        childUpdates.put("/All_Write/" + key, postValues);

        // realtime Database 들어가면 User_Write는 각 user의 uid로 분류하여 글을 모아둔 곳.
        // User_Write -> 각 user의 Uid -> 글 분류 키값 -> 글 내용
        childUpdates.put("/User_Write/" + Uid + "/" + key, postValues);

        databaseReference.updateChildren(childUpdates);
    }

}
