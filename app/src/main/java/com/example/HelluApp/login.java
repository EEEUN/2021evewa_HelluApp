package com.example.HelluApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

//회원가입이 되어 있다면 로그인. 신규회원가입 버튼을 누르면 신규회원가입 이동
public class login extends AppCompatActivity {

    public static Activity loginActivity;

    //이메일, 패스워드, 로그인버튼, 회원가입버튼
    EditText editTextTextEmailAddress;
    EditText editTextTextPassword;
    Button login_button;
    TextView to_sign_up;

    //define firebase object
    FirebaseAuth firebaseAuth;

    @Override
    //activity_login 화면을 보여줌
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //로그인 부분
        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        login_button = (Button) findViewById(R.id.login_button);

        //회원가입 넘어가는 부분
        to_sign_up = (TextView) findViewById(R.id.to_sign_up);

        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 MainActivity를 연다.
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); //추가해 줄 ProfileActivity
        }



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextTextEmailAddress.getText().toString().trim();
                String pwd = editTextTextPassword.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(login.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        //로그인 화면에서 sign up 버튼을 누르면 회원가입 화면으로 넘어감
        TextView TextView = findViewById(R.id.to_sign_up);
        TextView.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signup.class);
                startActivity(intent);
            }

        });
    }
}