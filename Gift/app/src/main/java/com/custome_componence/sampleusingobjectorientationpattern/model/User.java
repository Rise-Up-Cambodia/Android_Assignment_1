package com.custome_componence.sampleusingobjectorientationpattern.model;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class User {

    private  String param = "";
    private  String password = "";


    public User(String name){
        this.param = name;
    }

    public User(String param,String password){
        this.param = param;
        this.password = password;
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
