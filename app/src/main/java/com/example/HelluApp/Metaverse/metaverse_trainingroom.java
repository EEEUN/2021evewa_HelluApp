package com.example.HelluApp.Metaverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.HelluApp.R;

public class metaverse_trainingroom extends AppCompatActivity {

    EditText et_trainingRoom_link;
    Button b_trainingRoom_enter;
    String TrainingRoomLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metaverse_trainingroom);

        //입력한 주소 받아오기
        //et_trainingRoom_link = findViewById(R.id.metaverse_trainingLink_editText);
//        if(et_trainingRoom_link != null){
//            TrainingRoomLink = et_trainingRoom_link.getText().toString();
//        } else{
//            //Toast.makeText(this, "트레이닝 방 주소를 입력해주세요", Toast.LENGTH_SHORT).show();
//            return;
//        }

        //트레이닝 방 입장하기 버튼 클릭 이벤트
        b_trainingRoom_enter = findViewById(R.id.metaverse_trainingLink_button);
        b_trainingRoom_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gather.town/invite?token=UQhaejD2RqaHyAjQ5qSM1FAUWmRJCn2g"));
                startActivity(intent);
            }
        });


    }
}
