package com.example.HelluApp.Walking;

//유저 데이터에 들어갈 내용 클래스

public class walking_ranking_item {

    int rank_num;
    String rank_name;
    int rank_resourceId;

    public walking_ranking_item(int rank_num, int rank_resourceId, String rank_name) {
        this.rank_num = rank_num;
        this.rank_name = rank_name;
        this.rank_resourceId = rank_resourceId;
    }

    public int getRank_num(){return rank_num;}

    public int getRank_ResourceId() {
        return rank_resourceId;
    }


    public String getRank_Name() {
        return rank_name;
    }

    public void setRank_num(int num) {this.rank_num = num; }

    public void setRank_Name(String name) {
        this.rank_name = name;
    }

    public void setRank_ResourceId(int resourceId) {
        this.rank_resourceId = resourceId;
    }
}