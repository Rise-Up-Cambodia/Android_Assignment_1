package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.custome_componence.sampleusingobjectorientationpattern.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class UserDataConverter implements IDataConverter{

    public ArrayList<User> convertJSONToLogin(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<User> gifts = new ArrayList<User>();

        try {


            JSONArray json = jsonObject.getJSONArray("users");
            String error = "username password are not match!";

            if(json.length() == 1){
                int lg = json.length();
                for(int i = 0; i < lg; i++) {
                    JSONObject job = json.getJSONObject(i);
                    String name = job.getJSONObject("User").getString("name");
                    String password = job.getJSONObject("User").getString("password");

                    gifts.add(new User(name, password));
                }
            }
            if(json.length() == 0){

                gifts.add(new User(error));

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return gifts;
    }
}
