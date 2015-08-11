package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Vanda on 10/08/2015.
 */
public class GiftDataConverter implements IDataConverter {
    public ArrayList<Gift> convertJSONToAllGift(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<Gift> gifts = new ArrayList<Gift>();
        try {
           JSONArray  json = jsonObject.getJSONArray("gifts");
           // JSONArray arrjson = json.getJSONArray("Contact");
            int lg = json.length();
                for(int i = 0; i < lg; i++){
                JSONObject job = json.getJSONObject(i);
                String description = job.getJSONObject("Gift").getString("description");
                String name = job.getJSONObject("user").getString("name");
                String userProfile = job.getJSONObject("user").getString("user_profile");
                String post = job.getJSONObject("Gift").getString("date");
                String from = job.getJSONObject("Gift").getString("from");
                String category = job.getJSONObject("category").getString("cat_name");
                String gift_name = job.getJSONObject("Gift").getString("gift_name");
                String received_date = job.getJSONObject("Gift").getString("receive_date");
                String id = job.getJSONObject("Gift").getString("id");
                gifts.add(new Gift(name,post,from,description,category,gift_name,id,received_date,userProfile));
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gifts;
    }
    public Gift convertJSONToGiftDetail(JSONObject jsonObject){
        //Contact contact = null;
        Gift gift = null;
        try {
            JSONArray  json = jsonObject.getJSONArray("gifts");
            // JSONArray arrjson = json.getJSONArray("Contact");
                String description = json.getJSONObject(0).getJSONObject("Gift").getString("description");
                String name = json.getJSONObject(0).getJSONObject("user").getString("name");
            String userProfile = json.getJSONObject(0).getJSONObject("user").getString("user_profile");
                String post = json.getJSONObject(0).getJSONObject("Gift").getString("date");
                String from = json.getJSONObject(0).getJSONObject("Gift").getString("from");
                String category = json.getJSONObject(0).getJSONObject("category").getString("cat_name");
                String gift_name = json.getJSONObject(0).getJSONObject("Gift").getString("gift_name");
                String received_date = json.getJSONObject(0).getJSONObject("Gift").getString("receive_date");
                String id = json.getJSONObject(0).getJSONObject("Gift").getString("id");
                gift = new Gift(name,post,from,description,category,gift_name,id,received_date,userProfile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return gift;
    }

}
