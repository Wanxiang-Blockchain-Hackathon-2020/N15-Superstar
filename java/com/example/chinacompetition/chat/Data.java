package com.example.chinacompetition.chat;

public class Data {


    private String title;
    private String memo;
    private String path;

    public Data(String title, String memo, String path) {
        this.title = title;
        this.memo = memo;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
