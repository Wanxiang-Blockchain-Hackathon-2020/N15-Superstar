package com.example.chinacompetition.PostJobDir;

import java.util.HashMap;

public class JobsListData {

    String num, id, subject, category, term, cost, content, bidderNum,stage,userId;

    public JobsListData(){

    }

    public JobsListData(String num, String id, String subject, String category, String term, String cost, String content, String bidderNum) {
        this.num = num;
        this.id = id;
        this.subject = subject;
        this.category = category;
        this.term = term;
        this.cost = cost;
        this.content = content;
        this.bidderNum = bidderNum;
    }

    public JobsListData(String num, String id, String subject, String category, String term, String cost, String content, String bidderNum, String userId) {
        this.num = num;
        this.id = id;
        this.subject = subject;
        this.category = category;
        this.term = term;
        this.cost = cost;
        this.content = content;
        this.bidderNum = bidderNum;
        this.userId = userId;
    }

    public HashMap makeMap(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("num", num);
        hashMap.put("id", id);
        hashMap.put("subject", subject);
        hashMap.put("category", category);
        hashMap.put("term", term);
        hashMap.put("cost", cost);
        hashMap.put("content", content);
        hashMap.put("bidderNum", bidderNum);
        return hashMap;
    }

    public HashMap makeMap2(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("num", num);
        hashMap.put("id", id);
        hashMap.put("subject", subject);
        hashMap.put("category", category);
        hashMap.put("term", term);
        hashMap.put("cost", cost);
        hashMap.put("content", content);
        hashMap.put("bidderNum", bidderNum);
        hashMap.put("userId", userId);

        return hashMap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBidderNum() {
        return bidderNum;
    }

    public void setBidderNum(String bidderNum) {
        this.bidderNum = bidderNum;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
