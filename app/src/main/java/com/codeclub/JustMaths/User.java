package com.codeclub.JustMaths;

public class User {
    public String name;
    public String user_id;
    public String phone;

    public User(String name, String user_id) {
        this.name = name;
        this.user_id = user_id;
    }
    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public User(String phone) {
//        this.phone = phone;
//    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
