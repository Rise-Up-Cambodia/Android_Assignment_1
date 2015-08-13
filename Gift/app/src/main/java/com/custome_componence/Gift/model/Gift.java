package com.custome_componence.Gift.model;

/**
 * Created by Sreyleak on 05/08/2015.
 */
public class Gift implements IDataModel {

    private String description = "";
    private String from = "";
    private String post = "";
    private  String name = "";
    private  String password = "";
    private  String category = "";
    private  String imageName = "";
    private String id = "";
    private String receiveDate = "";
    private String userProfile = "";


    public Gift(String name,String post,String from,String description,String category, String im,
                String id, String receiveDate, String userProfile) {
        this.description = description;
        this.from = from;
        this.post = post;
        this.name = name;
        this.category = category;
        this.imageName = im;
        this.id = id;
        this.receiveDate = receiveDate;
        this.userProfile = userProfile;

    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receive_date) {
        this.receiveDate = receive_date;
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

    public void setPost(String post) {
        this.post = post;
    }
    public String getPost() {
        return post;
    }

    public void setName(String name) {
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


    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public String getImageName() {
        return imageName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
