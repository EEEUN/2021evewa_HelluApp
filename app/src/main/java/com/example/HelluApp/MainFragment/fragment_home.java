package com.example.HelluApp.MainFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.HelluApp.Community.community;
import com.example.HelluApp.DailyStamp.daily_stamp;
import com.example.HelluApp.PlanReview.plan_review;
import com.example.HelluApp.R;
import com.example.HelluApp.Metaverse.metaverse;
import com.example.HelluApp.Walking.walking;
import com.example.HelluApp.plan_choose;


public class fragment_home extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //홈에 있는 매일 인증(daily_stamp)버튼을 누르면 매일 인증 화면으로 넘어감
        Button imageButton = view.findViewById(R.id.daily_stamp_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), daily_stamp.class);
                startActivity(intent);
            }
        });

        //홈에 있는 플랜 선택(plan_choose)버튼을 누르면 플랜 선택 화면으로 넘어감
        imageButton = view.findViewById(R.id.plan_choose_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), plan_choose.class);
                startActivity(intent);
            }
        });

        //홈에 있는 소통(community)버튼을 누르면 커뮤니티 화면으로 넘어감
        imageButton = view.findViewById(R.id.community_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), community.class);
                startActivity(intent);
            }
        });

        //홈에 있는 ai 플랜 다시보기(plan_reivew)버튼을 누르면 ai 플랜 다시보기 화면으로 넘어감
        imageButton = view.findViewById(R.id.plan_review_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), plan_review.class);
                startActivity(intent);
            }
        });


        //홈에 있는 걷기 운동(walking)버튼을 누르면 걷기 운동 화면으로 넘어감
        imageButton = view.findViewById(R.id.walking_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), walking.class);
                startActivity(intent);
            }
        });

        //홈에 있는 역대 기록(metaverse)버튼을 누르면 역대 기록 화면으로 넘어감
        imageButton = view.findViewById(R.id.metaverse_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), metaverse.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    /*
    private void getFragmentDailyList(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragment = new daily_stamp_allpost_frag();
        fragmentTransaction.add(R.id.content_layout, fragment);
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();

    }
     */
}
