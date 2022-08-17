package com.ziojio.zuiapp.database.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserInfo {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userid;
    public String username;
    public String password;
    public String nickname;
    public String email;
    public String phone;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
