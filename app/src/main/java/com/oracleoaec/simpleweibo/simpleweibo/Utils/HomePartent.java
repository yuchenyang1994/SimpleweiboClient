package com.oracleoaec.simpleweibo.simpleweibo.Utils;

/**
 * Created by ycy on 16-4-13.
 */
public class HomePartent
{
    public static String url = "http://192.168.1.112:5000/";

    public HomePartent() {
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        HomePartent.url = url;
    }
}
