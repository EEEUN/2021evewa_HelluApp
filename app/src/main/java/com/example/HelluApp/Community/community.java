package com.example.HelluApp.Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.HelluApp.DailyStamp.daily_stamp_camera;
import com.example.HelluApp.DailyStamp.daily_stamp_write;
import com.example.HelluApp.R;
import com.example.HelluApp.Community.community_user;
import com.example.HelluApp.Walking.walking_walking;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class community extends AppCompatActivity {

    //하단 네비게이션
    private BottomNavigationView mBottomNV;

    //커뮤니티.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        //getFragmentManager().beginTransaction().replace(R.id.content_layout, new community_user()).commit();

        //하단 네비게이션 화면 선택하면 눌리는 함수
        mBottomNV = findViewById(R.id.community_navigation);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.community_user);


    }

    //BottomNavigation 페이지 변경
    private void BottomNavigate(int id) {
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.community_chat) {
                fragment = new community_chatting();

            } else{
                fragment = new community_user();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }


}