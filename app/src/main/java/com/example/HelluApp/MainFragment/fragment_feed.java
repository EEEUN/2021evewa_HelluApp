package com.example.HelluApp.MainFragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.HelluApp.DailyStamp.daily_stamp;
import com.example.HelluApp.DailyStamp.daily_stamp_write;
import com.example.HelluApp.MainFragment.feed.feed_my_posts;
import com.example.HelluApp.MainFragment.feed.feed_my_top_posts;
import com.example.HelluApp.MainFragment.feed.feed_recent;
import com.example.HelluApp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class fragment_feed extends Fragment {
    Fragment fragment_recent;
    Fragment fragment_mypost;
    Fragment fragment_mytoppost;

    //피드.java
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ActionBar actionBar = getActivity().getActionBar();
        //actionBar = requireActivity().getActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        FloatingActionButton imageButton = view.findViewById(R.id.fab);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), daily_stamp_write.class);
                startActivity(intent);
            }
        });

        fragment_recent = new feed_recent();
        fragment_mypost = new feed_my_posts();
        fragment_mytoppost = new feed_my_top_posts();

        getFragmentManager().beginTransaction().replace(R.id.container_in_feed, fragment_recent).commit();

        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Recent"));
        tabs.addTab(tabs.newTab().setText("My Posts"));
        tabs.addTab(tabs.newTab().setText("My Top Posts"));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("feed", "선택된 탭: " + position);
                Fragment selected = null;
                if (position == 0) {
                    selected = fragment_recent;
                } else if (position == 1) {
                    selected = fragment_mypost;
                } else if (position == 2) {
                    selected = fragment_mytoppost;
                }
                getFragmentManager().beginTransaction().replace(R.id.container_in_feed, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}