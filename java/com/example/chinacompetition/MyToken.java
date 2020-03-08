package com.example.chinacompetition;

public class MyToken {

    public static String mToken;
    public static String bcURL = "http://34.97.249.244:4000/";
    public static String getmToken() {
        return mToken;
    }

    public static void setmToken(String mToken) {
        MyToken.mToken = mToken;
    }

    public static String getBcURL() {
        return bcURL;
    }
}
