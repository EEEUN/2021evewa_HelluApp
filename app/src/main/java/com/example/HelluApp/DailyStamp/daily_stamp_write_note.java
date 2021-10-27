package com.example.HelluApp.DailyStamp;
//이 파일은 지금 사용되지 않습니다.
public class daily_stamp_write_note {
    int _id;                    //uid(로그인한 사용자)
    String title;               //제목
    String contents;            //내용
    String picture;             //이미지 경로
    String createDateStr;       //날짜

    public daily_stamp_write_note(int _id, String title, String contents, String picture, String createDateStr) {
        this._id = _id;
        this.title = title;
        this.contents = contents;
        this.picture = picture;
        this.createDateStr = createDateStr;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTitle(){ return title; }

    public void setTitle(String title){ this.title = title;}

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
