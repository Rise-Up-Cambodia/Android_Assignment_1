package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.custome_componence.sampleusingobjectorientationpattern.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vanda on 4/8/2015.
 */
public class UserDataConverter implements IDataConverter {

    public ArrayList<User> convertJSONToUsers(JSONObject jsonObject) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            JSONArray json = jsonObject.getJSONArray("users");
            String errorMessage = "username password are not match!";

            if (json.length() == 1) {
                int lg = json.length();
                for (int i = 0; i < lg; i++) {
                    JSONObject jsonObj = json.getJSONObject(i);
                    String name = jsonObj.getJSONObject("User").getString("name");
                    String password = jsonObj.getJSONObject("User").getString("password");
                    int userId = jsonObj.getJSONObject("User").getInt("id");
                    users.add(new User(name, password, userId));
                }
            }
            if (json.length() == 0) {

                users.add(new User(errorMessage));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public ArrayList<User> convertJSONToUser(JSONObject jsonObject) {
        ArrayList<User> users = new ArrayList<User>();

        try {


            JSONArray json = jsonObject.getJSONArray("users");
            String emailMessage = "Not duplicate user";

            if (json.length() == 1) {
                int lg = json.length();
                for (int i = 0; i < lg; i++) {
                    JSONObject jsonObj = json.getJSONObject(i);
                    String email = jsonObj.getJSONObject("User").getString("email");
                    users.add(new User(email));
                }
            }
            if (json.length() == 0) {

                users.add(new User(emailMessage));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
