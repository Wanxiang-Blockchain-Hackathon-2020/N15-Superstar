package com.example.chinacompetition;

public class CommonValue {


    public static String serverURL = "http://34.64.144.139/";
    public static String id = "test";
    public static String name = "test";
    public static String loginedCLIENTFREELANCER = "freelancer";


    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CommonValue.id = id;
    }

    public static String getServerURL() {
        return serverURL;
    }

    public static void setServerURL(String serverURL) {
        CommonValue.serverURL = serverURL;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        CommonValue.name = name;
    }

    public static String getLoginedCLIENTFREELANCER() {
        return loginedCLIENTFREELANCER;
    }

    public static void setLoginedCLIENTFREELANCER(String loginedCLIENTFREELANCER) {
        CommonValue.loginedCLIENTFREELANCER = loginedCLIENTFREELANCER;
    }
}
