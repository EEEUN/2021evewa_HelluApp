package com.example.HelluApp;

import java.util.HashMap;

public class User {

    public String Nickname;
    public String Email;
    public String Uid;
    public String ProfileUrl;
    public String Walking;

    public HashMap<String, Object> usermap = new HashMap<>();

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(user.class)
    }

    public User(String Nickname, String Email, String Uid, String ProfileUrl, String Walking){
        this.Nickname = Nickname;
        this.Email = Email;
        this.Uid = Uid;
        this.ProfileUrl = ProfileUrl;
        this.Walking = Walking;
    }

    public HashMap<String, Object> usertomap(){
        HashMap<String, Object> user_result = new HashMap<>();
        user_result.put("Nickname", Nickname); //키, 값
        user_result.put("Email", Email);
        user_result.put("Uid", Uid);
        user_result.put("ProfileUrl", ProfileUrl);
        user_result.put("Walking", Walking);

        return user_result;
    }
}
