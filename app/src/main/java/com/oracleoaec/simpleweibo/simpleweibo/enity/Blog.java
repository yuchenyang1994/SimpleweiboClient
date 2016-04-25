package com.oracleoaec.simpleweibo.simpleweibo.enity;

/**
 * Created by ycy on 16-4-13.
 */
public class Blog
{
    private int blog_id;
    private String userphoto;
    private String username;
    private String userblog;
    private String issutime;
    private int user_id;

    public Blog(String issutime, String username, String userblog,int user_id) {
        this.username = username;
        this.userblog = userblog;
        this.issutime = issutime;
        this.user_id = user_id;
    }

    public Blog() {
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserblog() {
        return userblog;
    }

    public void setUserblog(String userblog) {
        this.userblog = userblog;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "userphoto='" + userphoto + '\'' +
                ", username='" + username + '\'' +
                ", userblog='" + userblog + '\'' +
                ", issutime='" + issutime + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public String getIssutime() {
        return issutime;
    }

    public void setIssutime(String issutime) {
        this.issutime = issutime;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
