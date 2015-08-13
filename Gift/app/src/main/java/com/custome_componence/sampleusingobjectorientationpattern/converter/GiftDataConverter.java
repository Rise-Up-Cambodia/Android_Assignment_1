package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vanda on 10/08/2015.
 */
public class GiftDataConverter implements IDataConverter {
    public ArrayList<Gift> convertJSONToGifts(JSONObject jsonObject) {
        ArrayList<Gift> gifts = new ArrayList<Gift>();
        try {
            JSONArray json = jsonObject.getJSONArray("gifts");
            int lg = json.length();
            for (int i = 0; i < lg; i++) {
                JSONObject jsonObj = json.getJSONObject(i);
                String description = jsonObj.getJSONObject("Gift").getString("description");
                String userName = jsonObj.getJSONObject("user").getString("name");
                String userProfile = jsonObj.getJSONObject("user").getString("user_profile");
                String post = jsonObj.getJSONObject("Gift").getString("date");
                String from = jsonObj.getJSONObject("Gift").getString("from");
                String category = jsonObj.getJSONObject("category").getString("cat_name");
                String giftName = jsonObj.getJSONObject("Gift").getString("gift_name");
                String receivedDate = jsonObj.getJSONObject("Gift").getString("receive_date");
                String id = jsonObj.getJSONObject("Gift").getString("id");
                gifts.add(new Gift(userName, post, from, description, category, giftName, id, receivedDate,
                        userProfile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gifts;
    }

    /**
     * Created by Sreyleak on 12/08/2015
     */
    public Gift convertJSONToOneGift(JSONObject jsonObject) {
        Gift gift = null;
        try {
            JSONArray json = jsonObject.getJSONArray("gifts");
            String description = json.getJSONObject(0).getJSONObject("Gift").getString("description");
            String name = json.getJSONObject(0).getJSONObject("user").getString("name");
            String userProfile = json.getJSONObject(0).getJSONObject("user").getString("user_profile");
            String post = json.getJSONObject(0).getJSONObject("Gift").getString("date");
            String from = json.getJSONObject(0).getJSONObject("Gift").getString("from");
            String category = json.getJSONObject(0).getJSONObject("category").getString("cat_name");
            String giftName = json.getJSONObject(0).getJSONObject("Gift").getString("gift_name");
            String receivedDate = json.getJSONObject(0).getJSONObject("Gift").getString("receive_date");
            String id = json.getJSONObject(0).getJSONObject("Gift").getString("id");
            gift = new Gift(name, post, from, description, category, giftName, id, receivedDate, userProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gift;
    }

}
