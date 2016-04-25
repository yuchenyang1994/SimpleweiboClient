package com.oracleoaec.simpleweibo.simpleweibo.enity;

/**
 * Created by ycy on 16-4-22.
 */
public class Friend {
    private String friendphoto;
    private String friendname;
    private int friend_id;

    public Friend(String friendphoto, String friendname,int friend_id) {
        this.friendphoto = friendphoto;
        this.friendname = friendname;
        this.friend_id = friend_id;
    }

    public Friend() {
    }

    public String getFriendphoto() {
        return friendphoto;
    }

    public void setFriendphoto(String friendphoto) {
        this.friendphoto = friendphoto;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "friendphoto='" + friendphoto + '\'' +
                ", friendname='" + friendname + '\'' +
                '}';
    }
}
