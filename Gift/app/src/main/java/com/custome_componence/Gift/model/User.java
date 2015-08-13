package com.custome_componence.Gift.model;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class User {

    private  String param = "";
    private  String password = "";
    private  int userId = 0;


    public User(String name){
        this.param = name;
    }

    public User(String param,String password,int userId){
        this.param = param;
        this.password = password;
        this.userId = userId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setParam(String param) {
        this.param = param;
    }
    public String getParam() {
        return param;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

}
