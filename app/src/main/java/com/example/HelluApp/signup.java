package com.example.HelluApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//신규회원으로 Email과 암호를 적고 가입한다.
public class signup extends AppCompatActivity {

    //이메일, 패스워드, 로그인버튼, 회원가입버튼
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    EditText editTextTextName;
    Button signup_button;
    TextView to_sign_up;

    //define firebase object
    private FirebaseAuth firebaseAuth;

    //데이터베이스에서 데이터를 읽거나 쓰려면 DatabaseReference의 인스턴스가 필요
    private DatabaseReference rDatabase;

    @Override

    //activity_signup 화면을 보여줌
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextTextName = (EditText) findViewById(R.id.editTextTextName);
        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        signup_button = (Button) findViewById(R.id.signup_button);
        to_sign_up = (TextView) findViewById(R.id.to_sign_up);

        //firebase정의
        rDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 MainActivity를 연다.
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = editTextTextEmailAddress.getText().toString().trim();
                String Password = editTextTextPassword.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    String Email = user.getEmail();
                                    String Uid = user.getUid();
                                    String Nickname = editTextTextName.getText().toString().trim();
                                    String ProfileUrl = "https://mblogthumb-phinf.pstatic.net/20150417_264/ninevincent_14291992723052lDb3_JPEG/kakao_11.jpg?type=w2";
                                    String Walking = "0";
                                    User user1 = new User(Nickname, Email, Uid, ProfileUrl, Walking);
                                    HashMap<String, Object> user_data = user1.usertomap();

                                    /*
                                    //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                    HashMap<Object,String> hashMap = new HashMap<>();
                                    hashMap.put("Name",Email); //키, 값
                                    hashMap.put("Email",Nickname);
                                    hashMap.put("Uid",Uid);
                                     */

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("User");
                                    reference.child(Uid).setValue(user_data);

                                    ////가입이 이루어졌을시 가입 화면을 빠져나감.
                                    Intent intent = new Intent(signup.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(signup.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                        });

            }
        });

        //로그인 화면에서 sign in 버튼을 누르면 로그인 화면으로 넘어감
        TextView TextView = findViewById(R.id.to_sign_in);
        TextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }

        });

    }
}