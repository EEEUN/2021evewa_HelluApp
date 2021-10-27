package com.example.HelluApp.Community;

public class users_model {
    public static String useremail;
    public static String usernm;
    public static String userphoto;
    public static String uid;

    public users_model() {
    }

    public users_model(String useremail, String usernm, String userphoto, String uid) {
        this.useremail = useremail;
        this.usernm = usernm;
        this.userphoto = userphoto;
        this.uid = uid;
    }
}