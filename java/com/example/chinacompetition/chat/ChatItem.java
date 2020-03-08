package com.example.chinacompetition.chat;

public class ChatItem {

    String id,thumbnail,content,user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ChatItem(String id, String thumbnail, String content, String user) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.content = content;
        this.user = user;
    }

    public ChatItem(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public ChatItem(String id, String thumbnail, String content) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.content = content;
    }

    public ChatItem(String content) {
        this.content = content;
    }


}
