package com.tql.huaweiapp.entry;

import java.util.Date;

public class User {
    int userID;

    String mail;

    String password;

    String nickName;

    int age;
    int gender;
    Date birthday;

    String hobby;

    public User(String mail, String password, String nickName, int age, int gender, Date birthday, String hobby) {
        this.mail = mail;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.birthday = birthday;
        this.hobby = hobby;
    }

    public User() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
