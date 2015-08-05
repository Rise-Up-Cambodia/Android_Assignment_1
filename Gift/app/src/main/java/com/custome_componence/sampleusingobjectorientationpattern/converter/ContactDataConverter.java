package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Contact;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Channy on 8/3/2015.
 */
public class ContactDataConverter implements IDataConverter {
    public ArrayList<Contact> convertJSONToContact(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {

            String name = "";
            String phone = "";
            String age = "";
            //JSONObject json = null;
           JSONArray  json = jsonObject.getJSONArray("contacts");
           // JSONArray arrjson = json.getJSONArray("Contact");
            int lg = json.length();
                for(int i = 0; i < lg; i++){
                JSONObject job = json.getJSONObject(i);
                phone = job.getJSONObject("Contact").getString("phone");
                name = job.getJSONObject("Contact").getString("name");
                age = job.getJSONObject("Contact").getString("age");
                contacts.add(new Contact(name,phone,age));
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return contacts;
    }
    public Contact convertJSONToDetail(JSONObject jsonObject){
        Contact contact = null;
        try {
            String age = "";
            //JSONObject json = null;
            JSONObject  json = jsonObject.getJSONObject("contact");
                JSONObject job = json.getJSONObject("Contact");
                age = job.getString("age");
                contact = new Contact(age);
        }catch (Exception e){
            e.printStackTrace();
        }
        return contact;
    }
}
