package com.custome_componence.sampleusingobjectorientationpattern.model;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class User {

    private  String name = "";
    private  String password = "";


    public User(String name){
        this.name = name;
    }

    public User(String name,String password){
        this.name = name;
        this.password = password;
    }




    public void setName(String date) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

}
