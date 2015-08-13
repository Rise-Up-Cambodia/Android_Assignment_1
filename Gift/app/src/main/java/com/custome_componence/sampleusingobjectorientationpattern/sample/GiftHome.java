package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.converter.GiftDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Vanda on 8/7/2015
 */
public class GiftHome extends ActionBarActivity {

    public static ArrayList<Gift> gifts = null;

    PullListView lv;

    private static int PAGE_NUM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_home);
        lv = (PullListView) findViewById(R.id.pulllistView);
        final GiftOperation giftOperation = new GiftOperation();
        lv.deferNotifyDataSetChanged();
        getSupportActionBar().setTitle("Gift Home");

        giftOperation.getAllGifts(new IOperationListener() {
            @Override
            public void success(JSONObject json) {

                GiftDataConverter giftDataConverter = new GiftDataConverter();
                gifts = giftDataConverter.convertJSONToGifts(json);
                CustomAdapter adt = new CustomAdapter(GiftHome.this, gifts);
                lv.setAdapter(adt);
                lv.refreshComplete();
                lv.getMoreComplete();
                lv.deferNotifyDataSetChanged();
            }

            @Override
            public void fail(int statusCode, String responseBody) {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gid = gifts.get(position - 1).getId();
                String username = gifts.get(position - 1).getName();
                Intent intent = new Intent(GiftHome.this, GiftDetail.class);
                intent.putExtra("id", gid);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        lv.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                gifts.clear();
                giftOperation.getAllGifts(new IOperationListener() {
                    @Override
                    public void success(JSONObject json) {
                        PAGE_NUM = 3;
                        GiftDataConverter giftDataConverter = new GiftDataConverter();
                        gifts = giftDataConverter.convertJSONToGifts(json);
                        CustomAdapter adt = new CustomAdapter(GiftHome.this, gifts);
                        lv.setAdapter(adt);
                        lv.refreshComplete();
                        lv.deferNotifyDataSetChanged();
                    }
                    @Override
                    public void fail(int statusCode, String responseBody) {

                    }
                });
            }
        });
        lv.setOnGetMoreListener(new PullListView.OnGetMoreListener() {
            @Override
            public void onGetMore() {
//                if (PAGE_NUM <= gifts.size()) {

//                } else {
//                    PAGE_NUM = gifts.size();
//                }
                giftOperation.getGiftByPage(PAGE_NUM, new IOperationListener() {
                    @Override
                    public void success(JSONObject json) {
                        //gifts.clear();
                        PAGE_NUM = PAGE_NUM + 3;
                        Toast.makeText(getApplicationContext(),PAGE_NUM+"",Toast.LENGTH_LONG).show();
                        GiftDataConverter giftDataConverter = new GiftDataConverter();
                        gifts = giftDataConverter.convertJSONToGifts(json);
                        CustomAdapter adt = new CustomAdapter(GiftHome.this, gifts);
                        lv.setAdapter(adt);
                        lv.getMoreComplete();
                        lv.deferNotifyDataSetChanged();
                    }

                    @Override
                    public void fail(int statusCode, String responseBody) {

                    }

                });

            }
        });

    }

    //prevent backpress button
    @Override
    public void onBackPressed() {
    }

    /*
    * Created by Sreyleak 10/08/2015
    * */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift_home, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postGift:
                Intent intentToShareGiftActivity = new Intent(GiftHome.this, ShareGift.class);
                startActivity(intentToShareGiftActivity);
                return true;
            case R.id.logout:
                SharedPreferences shpf = getApplicationContext()
                        .getSharedPreferences("username", Context.MODE_PRIVATE);
                shpf.edit().clear().commit();
                Intent intentToUserLoginActivity = new Intent(GiftHome.this, UserLogin.class);
                startActivity(intentToUserLoginActivity);
                finish();
                return true;
            case R.id.cancel:
                closeOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
