package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;

import org.json.JSONObject;

public class GiftDetail extends ActionBarActivity {
    GiftOperation GiftOperation = new GiftOperation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_detail);
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
//                Intent intent1 = new Intent(GiftDetail.this, UpdateGift.class);
//                intent1.putExtra("giftid", gid);
//                intent1.putExtra("description", description);
//                intent1.putExtra("from", from);
//                intent1.putExtra("category", category);
//                intent1.putExtra("image_path", image_path);
//                intent1.putExtra("received_date", date);
//                startActivity(intent1);
                return true;

            case R.id.delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GiftDetail.this);
                alertDialog.setTitle("Delete Confirmation");
                alertDialog.setMessage("Are you sure want to delete this item?");

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent ii1 = getIntent();
                        String gfid1 = ii1.getStringExtra("giftid");
                        String url1 = "http://192.168.1.5:8585/RestCakephp/gifts/delete/" + gfid1 + ".json";
                        GiftOperation.deleteGift(new IOperationListener() {
                            @Override
                            public void success(JSONObject json) {
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
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
