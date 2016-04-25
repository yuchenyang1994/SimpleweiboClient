package com.oracleoaec.simpleweibo.simpleweibo.enity;

/**
 * Created by ycy on 16-4-15.
 */
public class Answers
{
    private int user_id;
    private String user_name;
    private String content;
    private String answertime;

    public Answers() {
    }

    public Answers(int user_id, String user_name, String content, String answertime) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.content = content;
        this.answertime = answertime;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", content='" + content + '\'' +
                ", answertime='" + answertime + '\'' +
                '}';
    }
}
