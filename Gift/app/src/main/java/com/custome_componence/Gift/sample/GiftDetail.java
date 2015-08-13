package com.custome_componence.Gift.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.custome_componence.Gift.R;
import com.custome_componence.Gift.config.Constant;
import com.custome_componence.Gift.converter.GiftDataConverter;
import com.custome_componence.Gift.model.Gift;
import com.custome_componence.Gift.operation.GiftOperation;
import com.custome_componence.Gift.operation.IOperationListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

/*
* Created by Sreyleak 10/08/2015
* */
public class GiftDetail extends ActionBarActivity {
    GiftOperation GiftOperation = new GiftOperation();
    TextView description, date, receivedDate, username, from, category;
    ImageView giftImage, userImage;
    public static Gift gifts = null;
    private  String  userName = "";
    String description1 = "";
    String date1 = "";
    String category1 = "";
    String from1 = "";
    String receivedDate1 = "";
    String giftName = "";
    String username1 = "";
    String id = "";
    String userProfile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
        description = (TextView) findViewById(R.id.description);
        from = (TextView) findViewById(R.id.from);
        date = (TextView) findViewById(R.id.date);
        receivedDate = (TextView) findViewById(R.id.receive_date);
        username = (TextView) findViewById(R.id.username);
        category = (TextView) findViewById(R.id.category);
        giftImage = (ImageView)findViewById(R.id.giftimage);
        userImage = (ImageView)findViewById(R.id.userimage);

        getSupportActionBar().setTitle("Gift Detail");


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        userName = intent.getStringExtra("username");
        GiftOperation.getGiftById(id,new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                GiftDataConverter giftDataConverter = new GiftDataConverter();
                gifts = giftDataConverter.convertJSONToGiftDetail(json);
                username1 = gifts.getName();
                date1 = gifts.getPost();
                category1 = gifts.getCategory();
                from1 = gifts.getFrom();
                receivedDate1 = gifts.getReceiveDate();
                description1 = gifts.getDescription();
                giftName = gifts.getIm();
                userProfile = gifts.getUserProfile();

                description.setText(description1);
                from.setText(from1);
                username.setText(username1);
                date.setText(date1);
                category.setText(category1);
                receivedDate.setText(receivedDate1);
                /*
                * load gift image
                * */
                ImageViewAware imageViewAware = new ImageViewAware(giftImage);
                ImageLoader.getInstance().displayImage(Constant.BASE_URL + "app/webroot/img/" +
                        gifts.getIm(), imageViewAware, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason
                            failReason) {

                        super.onLoadingFailed(imageUri, view, failReason);

                    }
                });
                /*
                * load user image
                * */
                ImageViewAware imageViewUser = new ImageViewAware(userImage);
                ImageLoader.getInstance().displayImage(Constant.BASE_URL + "app/webroot/user_photo/" +
                        gifts.getUserProfile(), imageViewUser, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason
                            failReason) {
                        super.onLoadingFailed(imageUri, view, failReason);

                    }
                });

            }

            @Override
            public void fail(int statusCode, String responseBody) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sh = getSharedPreferences("username",Context.MODE_PRIVATE);
        String username2 = sh.getString("username", "");
        if (!userName.equals(username2)){

        }
        else {
            getMenuInflater().inflate(R.menu.menu_gift_detail, menu);
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                Intent intentToUpdateActivity = new Intent(GiftDetail.this, UpdateGift.class);
                intentToUpdateActivity.putExtra("description",description1);
                intentToUpdateActivity.putExtra("date",date1);
                intentToUpdateActivity.putExtra("receiveDate",receivedDate1);
                intentToUpdateActivity.putExtra("username",username1);
                intentToUpdateActivity.putExtra("from",from1);
                intentToUpdateActivity.putExtra("category",category1);
                intentToUpdateActivity.putExtra("giftName",giftName);
                intentToUpdateActivity.putExtra("id",id);
                startActivity(intentToUpdateActivity);
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GiftDetail.this);
                alertDialog.setTitle("Delete Confirmation");
                alertDialog.setMessage("Are you sure want to delete this item?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent1 = getIntent();
                        String id1 = intent1.getStringExtra("id");
                        GiftOperation.deleteGift(id1, new IOperationListener() {
                            @Override
                            public void success(JSONObject json) {
                                Intent intentToHome = new Intent(GiftDetail.this, GiftHome.class);
                                startActivity(intentToHome);
                            }

                            @Override
                            public void fail(int statusCode, String responseBody) {
                                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent it1 = new Intent(GiftDetail.this, GiftDetail.class);
                        startActivity(it1);
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;

            case R.id.cancel:
                closeOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
