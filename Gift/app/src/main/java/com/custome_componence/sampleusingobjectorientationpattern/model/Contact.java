package com.custome_componence.sampleusingobjectorientationpattern.model;

/**
 * Created by Channy on 8/3/2015.
 */
public class Contact implements IDataModel {

    private String name = "";
    private String phone = "";
    private String age = "";

    public Contact(String name, String phone, String age) {
        this.name = name;
        this.phone = phone;
        this.age = age;
    }
    public Contact(String age){
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
