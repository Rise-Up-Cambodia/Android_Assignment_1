package com.custome_componence.Gift.converter;

import com.custome_componence.Gift.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vanda on 8/7/2015.
 */
public class UserDataConverter implements IDataConverter{

    public ArrayList<User> convertJSONToLogin(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<User> users = new ArrayList<User>();

        try {


            JSONArray json = jsonObject.getJSONArray("users");
            String error = "username password are not match!";

            if(json.length() == 1){
                int lg = json.length();
                for(int i = 0; i < lg; i++) {
                    JSONObject job = json.getJSONObject(i);
                    String name = job.getJSONObject("User").getString("name");
                    String password = job.getJSONObject("User").getString("password");
                    int userId = job.getJSONObject("User").getInt("id");
                    users.add(new User(name, password, userId));
                }
            }
            if(json.length() == 0){

                users.add(new User(error));

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<User> convertJSONAuthenticatedSignup(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<User> users = new ArrayList<User>();

        try {


            JSONArray json = jsonObject.getJSONArray("users");
            String emailMsg= "Not duplicate user";

            if(json.length() == 1){
                int lg = json.length();
                for(int i = 0; i < lg; i++) {
                    JSONObject job = json.getJSONObject(i);
                    String email = job.getJSONObject("User").getString("email");


                   users.add(new User(email));
                }
            }
            if(json.length() == 0){

                users.add(new User(emailMsg));

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return users;
    }
}
