package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.converter.GiftDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.converter.UserDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.custome_componence.sampleusingobjectorientationpattern.model.User;
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;
import com.custome_componence.sampleusingobjectorientationpattern.operation.UserOperation;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class GiftHome extends ActionBarActivity{

    public static ArrayList<Gift> gifts = null;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> posts = new ArrayList<String>();
    ArrayList<String> froms = new ArrayList<String>();
    ArrayList<String> categories = new ArrayList<String>();
    ArrayList<String> descriptions = new ArrayList<String>();
    ArrayList<String> giftid = new ArrayList<String>();
    ArrayList<Bitmap> Image = new ArrayList<Bitmap>();
    ArrayList<String> gift_path = new ArrayList<String>();
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_home);
        lv = (ListView) findViewById(R.id.listView);
        final GiftOperation giftOperation = new GiftOperation();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gid = ((TextView) view.findViewById(R.id.giftid)).getText().toString();
                Intent intent = new Intent(GiftHome.this, GiftDetail.class);
                intent.putExtra("id", gid);
                startActivity(intent);
            }
        });
        giftOperation.getAllGift(new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                        /* These two line of code will be use next time */
                GiftDataConverter giftDataConverter = new GiftDataConverter();
                gifts = giftDataConverter.convertJSONToAllGift(json);

                for (int i = 0; i < gifts.size(); i++) {
                    String name1 = gifts.get(i).getName();
                    String post = gifts.get(i).getPost();
                    String category = gifts.get(i).getCategory();
                    String from = gifts.get(i).getFrom();
                    String description = gifts.get(i).getDescription();
                    String id = gifts.get(i).getId();
                    String gift_name = gifts.get(i).getIm();
                    names.add(name1);
                    posts.add(post);
                    categories.add(category);
                    froms.add(from);
                    descriptions.add(description);
                    giftid.add(id);
                    gift_path.add(gift_name);
                    Image.add(BitmapFactory.decodeResource(GiftHome.this.getResources(), R.mipmap.christmas_18));

                    if (gift_name.equals(null) || gift_name.equals("no image")) {

                    } else {
                        new DownloadImageTask().execute("http://192.168.1.15:8585/Android_Assignment_1/GiftApi/app/webroot/img/" + gift_name, String.valueOf(names.size() - 1));
                    }


                }

                CustomAdapter adt = new CustomAdapter(GiftHome.this, names, posts, categories, froms, descriptions, gift_path,Image,giftid);
                lv.setAdapter(adt);
                lv.deferNotifyDataSetChanged();
            }

            @Override
            public void fail(int statusCode, String responseBody) {

            }
        });
    }
    private  class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        int position;

        protected Bitmap doInBackground(String... urls) {
            position = Integer.parseInt(urls[1]);
            return loadImageFromNetwork(urls[0]);

        }

        @Override
        protected void onCancelled() {

        }

        protected void onPostExecute(Bitmap result) {
            //Do something with bitmap eg:
            try {
                Image.add(position, result);
                ((CustomAdapter) lv.getAdapter()).notifyDataSetChanged();
            } catch (Exception e) {

            }
        }
    }


    private Bitmap loadImageFromNetwork(String url) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift_home, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                Intent intentToShareGift = new Intent(GiftHome.this, ShareGift.class);
                startActivity(intentToShareGift);
                return true;
            case R.id.logout:
                SharedPreferences logout = getApplicationContext()
                        .getSharedPreferences("AuthenticationLogout", Context.MODE_PRIVATE);
                logout.edit().clear().commit();
                Intent intentToUserLogin = new Intent(GiftHome.this, UserLogin.class);
                startActivity(intentToUserLogin);
            return true;
            case R.id.cancel:
                closeOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
