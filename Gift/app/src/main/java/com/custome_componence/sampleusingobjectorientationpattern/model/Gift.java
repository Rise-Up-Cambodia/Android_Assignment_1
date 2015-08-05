package com.custome_componence.sampleusingobjectorientationpattern.model;

/**
 * Created by Channy on 8/3/2015.
 */
public class Gift implements IDataModel {

    private String description = "";
    private String from = "";
    private String date = "";

    public Gift(String description, String from, String date) {
        this.description = description;
        this.from = from;
        this.date = date;
    }
    public Gift(String date){
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
