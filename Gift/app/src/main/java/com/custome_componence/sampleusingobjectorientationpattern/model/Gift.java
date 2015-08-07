package com.custome_componence.sampleusingobjectorientationpattern.model;

/**
 * Created by Channy on 8/3/2015.
 */
public class Gift implements IDataModel {

    private String description = "";
    private String from = "";
    private String date = "";
    private  String name = "";
    private  String password = "";


    public Gift(String description, String from, String date) {
        this.description = description;
        this.from = from;
        this.date = date;
    }
    public Gift(String name){
        this.name = name;
    }

    public Gift(String name,String password){
        this.name = name;
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
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
