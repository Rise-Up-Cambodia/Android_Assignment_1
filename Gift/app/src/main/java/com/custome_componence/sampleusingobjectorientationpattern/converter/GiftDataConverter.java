package com.custome_componence.sampleusingobjectorientationpattern.converter;

import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Channy on 8/3/2015.
 */
public class GiftDataConverter implements IDataConverter {
    public ArrayList<Gift> convertJSONToContact(JSONObject jsonObject){
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
                String from = job.getJSONObject("Gift").getString("from");
                String date = job.getJSONObject("Gift").getString("date");
                gifts.add(new Gift(description,from,date));
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return gifts;
    }
    public Gift convertJSONToDetail(JSONObject jsonObject){
        Gift gift = null;
        try {
            String date = "";
            //JSONObject json = null;
            JSONObject  json = jsonObject.getJSONObject("Gift");
                JSONObject job = json.getJSONObject("Gift");
                date = job.getString("date");
                gift = new Gift(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return gift;
    }
}
