package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Channy on 8/3/2015.
 */
public class GiftDataConverter implements IDataConverter {
    public ArrayList<Gift> convertJSONToAllGift(JSONObject jsonObject){
        //Contact contact = null;
        ArrayList<Gift> gifts = new ArrayList<Gift>();
        try {

//            String name = "";
//            String phone = "";
//            String date = "";
            //JSONObject json = null;
           JSONArray  json = jsonObject.getJSONArray("gifts");
           // JSONArray arrjson = json.getJSONArray("Contact");
            int lg = json.length();
                for(int i = 0; i < lg; i++){
                JSONObject job = json.getJSONObject(i);
                String description = job.getJSONObject("Gift").getString("description");
                String name = job.getJSONObject("user").getString("name");
                String post = job.getJSONObject("Gift").getString("date");
                String from = job.getJSONObject("Gift").getString("from");
                String category = job.getJSONObject("category").getString("cat_name");
                String gift_name = job.getJSONObject("Gift").getString("gift_name");
                String id = job.getJSONObject("Gift").getString("id");
                gifts.add(new Gift(name,post,from,description,category,gift_name,id));
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gifts;
    }

    public ArrayList<Gift> convertJSONGiftDetail(JSONObject jsonObject){
        ArrayList<Gift> gifts = new ArrayList<Gift>();
        try {
            JSONArray  json = jsonObject.getJSONArray("gifts");
            int lg = json.length();
            for(int i = 0; i < lg; i++){
                JSONObject job = json.getJSONObject(i);
                String description = job.getJSONObject("Gift").getString("description");
                String name = job.getJSONObject("user").getString("name");
                String post = job.getJSONObject("Gift").getString("date");
                String from = job.getJSONObject("Gift").getString("from");
                String category = job.getJSONObject("category").getString("cat_name");
                String gift_name = job.getJSONObject("Gift").getString("gift_name");
                String id = job.getJSONObject("Gift").getString("id");
                gifts.add(new Gift(name,post,from,description,category,gift_name,id));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gifts;
    }

}
