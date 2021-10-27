package com.example.HelluApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class plan_choose extends AppCompatActivity {
    float max_result;
    int index;
    
    String Gender = "";                 //성별
    String Age = "";                    //나이
    String EditHeight = "";             //신장, 키 (현재 내 키를 쓴다는 의미)
    String EditWeight = "";             //체중, 몸무게 (현재 내 체중을 쓴다는 의미)
    String LoseWeight = "";             //목표감량체중 (5를 써준다면 5키로를 빼는것)
    String PresentWeight = "";          //현재체중 (EditWeight와 값이 똑같을 것)
    String GoalWeight = "";             //목표체중 (PresentWeight - LoseWeight 한 값)
    String numberOfWeekOfExercise = ""; // 운동 계획
    String normalActivity = "";         // 평소 활동량
    String amountOfExercise = "";       // 하루 목표 운동량
    ArrayList<String> mealTime = new ArrayList<>();          //식사 시간
    String purposeOfExercise = "";      // 운동 목적(Motivation)
    String exercise;                    // 추천 운동
    ArrayList<String> meal_feedback = new ArrayList<>();   // 식단가이드(피드백)
    String exerciseURL;            // 추천 운동 영상 링크
    ArrayList<String> totalExerciseURL = new ArrayList<>();    // 추천 운동 영상 묶음
    Button result_btn;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    // 파이어베이스 유저 아이디 가져오는 코드인 듯? From. daily_stamp_write.java
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String Uid = user.getUid();         // user의 고유 랜덤 값(?)을 문자열로 반환해줌

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_plan_choose);

        result_btn = findViewById(R.id.pch_result_button);

        result_btn.setOnClickListener(v -> {
            Checked();

            // 현재 체중이랑 그냥 체중은 값은 다르고 용도가 다름
            HashMap<String, Object> Plan_result = new HashMap<>();
            Plan_result.put("성별", Gender);
            Plan_result.put("나이", Age);
            Plan_result.put("신장", EditHeight);
            Plan_result.put("체중", EditWeight);
            Plan_result.put("목표 감량 체중", LoseWeight);
            Plan_result.put("현재 체중", PresentWeight);
            Plan_result.put("목표 체중", GoalWeight);
            Plan_result.put("운동 주 횟수", numberOfWeekOfExercise);
            Plan_result.put("하루 목표 운동량", amountOfExercise);
            Plan_result.put("평소 활동량", normalActivity);
            Plan_result.put("식사 시간", mealTime);
            Plan_result.put("운동 목적", purposeOfExercise);
            Plan_result.put("식단 가이드", meal_feedback);

            // 선호 운동 출력
            prefer_Exercises(Gender, Age, normalActivity, purposeOfExercise);
            // 기초대사량 계산
            String BEE = CalculateBEE(Gender, EditWeight, EditHeight, Age);
            Plan_result.put("기초대사량", BEE);

            // All_Plan에 저장
            databaseReference.child("All_Plan").push().setValue(Plan_result);
            // User_Plan/UID에 저장
            databaseReference.child("User_Plan").child(Uid).setValue(Plan_result);

            // 다음 화면으로 넘어감.
            Intent intent = new Intent(getApplicationContext(), plan_choose_result.class);
            startActivity(intent);
        });
    }

    public void Checked() {
        //성별 선택 (단일 선택)
        RadioButton optionG1 = findViewById(R.id.man);                  //optionG1 옵션젠더1=맨
        RadioButton optionG2 = findViewById(R.id.woman);                //optionG2 옵션젠더2=우먼

        //나이 입력
        EditText optionAge = findViewById(R.id.editAge);                //optionAge 나이 입력해주는 텍스트

        //신장, 체중 입력
        EditText optionHeight = findViewById(R.id.editHeight);         //optionHeight 신장 입력해주는 텍스트
        EditText optionWeight = findViewById(R.id.editWeight);          //optionWeight 체중 입력해주는 텍스트

        //목표
        EditText optionLose = findViewById(R.id.editGoal);              //optionLose 얼만큼 뺄건지 목표감량체중
        TextView optionPresW = findViewById(R.id.present_weight);       //optionPresW 현재체중(optionWeight랑 값이 똑같음)
        TextView optionGoalW = findViewById(R.id.goal_weight);          //optionGoalW 목표체중(현재체중-목표감량체중 값)

        // 1. 운동 주 횟수(단일 선택)
        RadioButton option11 = findViewById(R.id.q1_radio_Button1);
        RadioButton option12 = findViewById(R.id.q1_radio_Button2);
        RadioButton option13 = findViewById(R.id.q1_radio_Button3);
        RadioButton option14 = findViewById(R.id.q1_radio_Button4);
        RadioButton option15 = findViewById(R.id.q1_radio_Button5);
        RadioButton option16 = findViewById(R.id.q1_radio_Button6);

        // 2. 하루 목표 운동량(단일 선택)
        RadioButton option21 = findViewById(R.id.q2_radio_Button1);
        RadioButton option22 = findViewById(R.id.q2_radio_Button2);
        RadioButton option23 = findViewById(R.id.q2_radio_Button3);
        RadioButton option24 = findViewById(R.id.q2_radio_Button4);

        // 3. 평소 활동량(단일 선택)
        RadioButton option41 = findViewById(R.id.q4_radio_Button1);
        RadioButton option42 = findViewById(R.id.q4_radio_Button2);
        RadioButton option43 = findViewById(R.id.q4_radio_Button3);
        RadioButton option44 = findViewById(R.id.q4_radio_Button4);
        RadioButton option45 = findViewById(R.id.q4_radio_Button5);

        // 4. 식사 시간(다중 선택)
        CheckBox option51 = findViewById(R.id.q5_radio_Button1);
        CheckBox option52 = findViewById(R.id.q5_radio_Button2);
        CheckBox option53 = findViewById(R.id.q5_radio_Button3);
        CheckBox option54 = findViewById(R.id.q5_radio_Button4);
        CheckBox option55 = findViewById(R.id.q5_radio_Button5);
        CheckBox option56 = findViewById(R.id.q5_radio_Button6);

        // 5. 운동 목적(단일 선택)
        RadioButton option61 = findViewById(R.id.q6_radio_Button1);
        RadioButton option62 = findViewById(R.id.q6_radio_Button2);
        RadioButton option63 = findViewById(R.id.q6_radio_Button3);
        RadioButton option64 = findViewById(R.id.q6_radio_Button4);
        RadioButton option65 = findViewById(R.id.q6_radio_Button5);

        // 6. 식단 가이드(다중 선택)
        CheckBox option71 = findViewById(R.id.q7_radio_Button1);
        CheckBox option72 = findViewById(R.id.q7_radio_Button2);
        CheckBox option73 = findViewById(R.id.q7_radio_Button3);
        CheckBox option74 = findViewById(R.id.q7_radio_Button4);
        CheckBox option75 = findViewById(R.id.q7_radio_Button5);
        CheckBox option76 = findViewById(R.id.q7_radio_Button6);

        //성별
        if (optionG1.isChecked()) {
            Gender = "남";
        } else if (optionG2.isChecked()) {
            Gender = "여";
        }

        //나이
        if (optionAge != null) {
            Age = optionAge.getText().toString().trim();
        } else {
            Toast.makeText(plan_choose.this, "나이를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //신장, 체중
        if (optionHeight != null || optionWeight != null) {
            EditHeight = optionHeight.getText().toString().trim();
            EditWeight = optionWeight.getText().toString().trim();
        } else {
            Toast.makeText(plan_choose.this, "키와 체중을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //목표감량 체중, 아직 계산은 안했음
        if (optionLose != null) {
            LoseWeight = optionLose.getText().toString().trim();

            //현재 체중 = 입력한 체중값
            PresentWeight = EditWeight;

            //문자를 숫자로 바꿈(형변환)
            double d_presentWeight = Double.parseDouble(PresentWeight);
            double d_loseWeight = Double.parseDouble(LoseWeight);
            double d_goalWeight = d_presentWeight - d_loseWeight;

            //목표 체중 = 형변환한 목표 체중값 저장
            GoalWeight = Double.toString(d_goalWeight);
        } else {
            Toast.makeText(plan_choose.this, "목표감량 체중을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

        // 1. 운동 주 횟수
        if (option11.isChecked()) {
            numberOfWeekOfExercise = "1~2일";
        } else if (option12.isChecked()) {
            numberOfWeekOfExercise = "2~3일";
        } else if (option13.isChecked()) {
            numberOfWeekOfExercise = "3~4일";
        } else if (option14.isChecked()) {
            numberOfWeekOfExercise = "4~5일";
        } else if (option15.isChecked()) {
            numberOfWeekOfExercise = "5~6일";
        } else if (option16.isChecked()) {
            numberOfWeekOfExercise = "6~7일";
        }

        // 2. 하루 목표 운동량(단일 선택)
        if (option21.isChecked()) {
            amountOfExercise = "30분 미만";
        } else if (option22.isChecked()) {
            amountOfExercise = "30분~1시간";
        } else if (option23.isChecked()) {
            amountOfExercise = "1시간~2시간";
        } else if (option24.isChecked()) {
            amountOfExercise = "2시간 이상";
        }

        // 3. 평소 활동량
        if (option41.isChecked()) {
            normalActivity = "운동을 하지 않는다.";
        } else if (option42.isChecked()) {
            normalActivity = "30분";
        } else if (option43.isChecked()) {
            normalActivity = "1시간";
        } else if (option44.isChecked()) {
            normalActivity = "2시간";
        } else if (option45.isChecked()) {
            normalActivity = "3시간 이상";
        }

        // 4. 식사 시간
        if (option51.isChecked()) {
            mealTime.add("아침");
        }
        if (option52.isChecked()) {
            mealTime.add("늦은 아침");
        }
        if (option53.isChecked()) {
            mealTime.add("점심");
        }
        if (option54.isChecked()) {
            mealTime.add("늦은 점심");
        }
        if (option55.isChecked()) {
            mealTime.add("저녁");
        }
        if (option56.isChecked()) {
            mealTime.add("늦은 저녁");
        }

        // 5. 운동 목적
        if (option61.isChecked()) {
            purposeOfExercise = "건강";
        } else if (option62.isChecked()) {
            purposeOfExercise = "다이어트";
        } else if (option63.isChecked()) {
            purposeOfExercise = "균형";
        } else if (option64.isChecked()) {
            purposeOfExercise = "스트레스 해소";
        } else if (option65.isChecked()) {
            purposeOfExercise = "체력 단련";
        }

        // 6. 식단가이드
        if (option71.isChecked()) {
            meal_feedback.add("밥을 많이 드시는군요. 탄수화물 과다 섭취는 인슐린을 지나치게 분비시켜 체내 지방 및 콜레스테롤을 " +
                    "축적시키므로 당뇨나 비만, 고혈압 등을 유발할 수 있습니다. 탄수화물 하루 권장섭취량은 250~300g 이며 이를 칼로리로 " +
                    "계산하면 1000~1200kcal 정도이니 참고해서 탄수화물 섭취를 줄여보시길 바랍니다.");
        }
        if (option72.isChecked()) {
            meal_feedback.add("빵을 많이 드시는군요. 빵과 같은 밀가루 음식은 설탕, 베이킹파우더, 버터, 마가린 등이 들어가는 고탄수화물 " +
                    "식품이기 때문에 혈당을 빠르게 치솟게 해 비만이나 당뇨, 지방간 등을 유발할 수 있습니다. 다이어트와 건강개선을 위해서는 " +
                    "밀가루 음식을 최대한 줄이시는 것이 좋습니다.");
        }
        if (option73.isChecked()) {
            meal_feedback.add("라면을 많이 드시는군요. 라면은 나트륨이 많은 음식으로 유명합니다. 특히 라면 국물은 사실상 나트륨 덩어리와 " +
                    "다름 없는데요. 나트륨 과다 섭취와 관련된 4대 만성질환(고혈압, 심장병, 만성 신장병, 뇌경색)뿐만 아니라 위암까지도 " +
                    "유발할 수 있습니다. 라면은 최대한 줄이고, 만약 먹는다면 국물은 먹지 않는 것이 좋습니다.");
        }
        if (option74.isChecked()) {
            meal_feedback.add("패스트푸드는 햄버거, 감자튀김, 치킨, 피자 등이 대표적입니다. 이들은 과한 칼로리, 포화지방, 트랜스 지방, " +
                    "콜레스테롤 등이 많아 영양불균형을 초래합니다. 염증을 유발하는 가공식품은 만성염증을 유발하기 때문에 최대한 줄이시는 " +
                    "것이 좋습니다. ");
        }
        if (option75.isChecked()) {
            meal_feedback.add("배달음식의 경우 자극적인 음식이 많아 나트륨 과다 섭취 가능성이 큽니다. 나트륨 과다 섭취는 " +
                    "고혈압, 뇌졸중, 당뇨, 비만 등 만성질환을 유발합니다. 다이어트나 건강 개선을 위해서는 배달음식을 최대한 줄이고 " +
                    "가정식을 먹는 것이 좋습니다.");
        }
        if (option76.isChecked()) {
            meal_feedback.add("군것질이나 간식을 많이 드시는군요. 하루 동안 간식을 자주 먹을수록 더 많은 전체 칼로리를 소비하게 되고, " +
                    "이것은 체중증가로 이어질 수 있습니다. 군것질을 줄이기 위해서는 포만감이 오래 가는 고단백 식사하기, 음식 일지 쓰기, " +
                    "물 자주 마시기, 운동하기 등의 방법이 있습니다.");
        }
    }

    // AI에 필요한 입력 값: 성별, 나이(구간), 평소 활동량, 운동 목적
    public void prefer_Exercises(String Gender, String Age, String Time_spent, String Motivation){
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("model_surveyAI", DownloadType.LOCAL_MODEL, conditions)
                .addOnSuccessListener(model -> {
                    // Download complete. Depending on your app, you could enable the ML
                    // feature, or switch from the local model to the remote model, etc.

                    // The CustomModel object contains the local path of the model file,
                    // which you can use to instantiate a TensorFlow Lite interpreter.
                    File modelFile = model.getFile();
                    if (modelFile != null) {
                        Interpreter interpreter = new Interpreter(modelFile);

                        // 모델의 Input
                        // 입력값 모두 0으로 초기화
                        float[][] input = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
                        float[][] output = new float[1][7]; // 모델의 Output

                        input = surveyValueRetn(Gender, Age, Time_spent, Motivation, input);

                        interpreter.run(input, output); // 모델 실행

                        // 모델 결과값 출력하기
                        for (int i = 0; i < output[0].length; i++) {
                            // 결과값 중에 최댓값 선정하기
                            if(i == 0){
                                index = 0;
                                max_result = output[0][i];
                            }
                            if(max_result < output[0][i]){
                                index = i;
                                max_result = output[0][i];
                            }
                        }

                        String explanation = "";

                        switch(index){
                            case 0:
                                exercise = "체육관 운동";
                                explanation = "헬스장 루틴 정리 영상";
                                exerciseURL = "https://www.youtube.com/watch?v=zjfGs_iIIE8";
                                break;

                            case 1:
                                exercise = "웨이트 트레이닝";
                                explanation = "초보자 전신 운동 영상";
                                exerciseURL = "https://www.youtube.com/watch?v=Ah72gR5DrxU";
                                break;

                            case 2:
                                exercise = "수영";
                                explanation = "수영 초보자를 위한 기초 강습";
                                exerciseURL = "https://www.youtube.com/watch?v=bPWqO20Xzco";
                                break;

                            case 3:
                                exercise = "팀 스포츠";
                                explanation = "동적 스트레칭 영상";
                                exerciseURL = "https://www.youtube.com/watch?v=gMKrEgAdmmo";
                                break;

                            case 4:
                                exercise = "조깅";
                                explanation = "조깅 영상";
                                exerciseURL = "https://www.youtube.com/watch?v=6zsHvi7aqjE";
                                break;

                            case 5:
                                exercise = "요가";
                                explanation = "23분 기초 요가 스트레칭";
                                exerciseURL = "https://www.youtube.com/watch?v=OBTl49bVk94";
                                break;

                            case 6:
                                exercise = "줌바 댄스";
                                explanation = "줌바 댄스 강의 영상";
                                exerciseURL = "https://www.youtube.com/watch?v=eK90xs8qPVw";
                                break;

                            default:
                                break;
                        }

                        // 운동 가이드
                        totalExerciseURL.add("운동 전 스트레칭 영상");
                        totalExerciseURL.add("https://www.youtube.com/watch?v=yyjOhsNEqtE");
                        totalExerciseURL.add(explanation);
                        totalExerciseURL.add(exerciseURL);

                        // 파베 저장 코드
                        databaseReference.child("User_Plan").child(Uid).child("추천운동").setValue(exercise);
                        databaseReference.child("User_Plan").child(Uid).child("운동 가이드").setValue(totalExerciseURL);

                        interpreter.close();    // 인터프리터 종료
                    }
                });
    }

    public String CalculateBEE(String Gender, String strWeight, String strHeight, String strAge){
        double resultBEE = 0;
        // 소수점 두 번째 자리까지
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if(Gender.equals("여")){
            resultBEE = 655.1 + (9.56 * Double.parseDouble(strWeight)) + (1.85 * Double.parseDouble(strHeight)) - (4.68 * Integer.parseInt(strAge));
        }else if(Gender.equals("남")){
            resultBEE = 66.5 + (13.75 * Double.parseDouble(strWeight)) + (5 * Double.parseDouble(strHeight)) - (6.76 * Integer.parseInt(strAge));
        }

        String BEE = decimalFormat.format(resultBEE);
        return BEE;
    }

    float[][] surveyValueRetn(String Gender, String Age, String Time_spent,
                              String Motivation, float[][] input){
        // Gender
        if(Gender.equals("여")){
            input[0][0] = 1;
        }else if (Gender.equals("남")){
            input[0][1] = 1;
        }

        // Age
        int intAge = Integer.parseInt(Age);

        if(intAge >= 15 && intAge <= 18){
            input[0][2] = 1;
        }else if(intAge >= 19 && intAge <= 25){
            input[0][3] = 1;
        }else if(intAge >= 26 && intAge < 30){
            input[0][4] = 1;
        }else if(intAge >= 30 && intAge < 40){
            input[0][5] = 1;
        }else if(intAge >= 40){
            input[0][6] = 1;
        }

        // Time_spent
        switch (Time_spent) {
            case "1시간":
                input[0][7] = 1;
                break;
            case "2시간":
                input[0][8] = 1;
                break;
            case "3시간 이상":
                input[0][9] = 1;
                break;
            case "30분":
                input[0][10] = 1;
                break;
            case "운동을 하지 않는다.":
                input[0][11] = 1;
                break;
        }

        // Motivation
        switch (Motivation) {
            case "건강":
                input[0][12] = 1;
                break;
            case "균형":
                input[0][13] = 1;
                break;
            case "체력 단련":
                input[0][14] = 1;
                break;
            case "다이어트":
                input[0][15] = 1;
                break;
            case "스트레스 해소":
                input[0][16] = 1;
                break;
        }
        return input;
    }
}