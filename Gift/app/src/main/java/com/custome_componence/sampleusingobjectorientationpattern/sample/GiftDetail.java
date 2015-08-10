package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;
import com.custome_componence.sampleusingobjectorientationpattern.converter.GiftDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

/*
* Created by Sreyleak 10/08/2015
* */
public class GiftDetail extends ActionBarActivity {
    GiftOperation GiftOperation = new GiftOperation();
    TextView description, date, receivedDate, username, from, category;
    public static Gift gifts = null;

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

        getSupportActionBar().setTitle("Gift Detail");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        GiftOperation.getGiftById(id, new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                GiftDataConverter giftDataConverter = new GiftDataConverter();
                gifts = giftDataConverter.convertJSONToGiftDetail(json);
                String description1 = "";
                String name1 = "";
                String date1 = "";
                String category1 = "";
                String from1 = "";
                String receivedDate1 = "";
                String giftName = "";
                name1 = gifts.getName();
                date1 = gifts.getPost();
                category1 = gifts.getCategory();
                from1 = gifts.getFrom();
                receivedDate1 = gifts.getReceiveDate();
                description1 = gifts.getDescription();
                giftName = gifts.getIm();

                description.setText(description1);
                from.setText(from1);
                username.setText(name1);
                date.setText(date1);
                category.setText(category1);
                receivedDate.setText(receivedDate1);
                new DownloadImageTask().execute(Constant.BASE_URL1 + "app/webroot/img/" + giftName);
            }

            @Override
            public void fail(int statusCode, String responseBody) {

            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        int position;

        protected Bitmap doInBackground(String... urls) {

            return loadImageFromNetwork(urls[0]);
        }

        @Override
        protected void onCancelled() {
        }

        protected void onPostExecute(Bitmap result) {
            try {
                ImageView iv = (ImageView) findViewById(R.id.giftimage);
                iv.setImageBitmap(result);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift_detail, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
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
