package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.converter.UserDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.User;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;
import com.custome_componence.sampleusingobjectorientationpattern.operation.UserOperation;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserLogin extends Activity {

    private Button btnAdd;
    EditText username;
    EditText password;
    public static ArrayList<User> gifts = null;
    public static User gift = null;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAdd = (Button) findViewById(R.id.btnadd);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        final UserOperation userOperation = new UserOperation();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userOperation.login(username.getText().toString(), password.getText().toString(), new IOperationListener() {
                        @Override
                    public void success(JSONObject json) {
                        /* These two line of code will be use next time */
                        UserDataConverter userDataConverter = new UserDataConverter();
                        gifts = userDataConverter.convertJSONToLogin(json);
                        // if(gift != "")

                        String name1 = "", password = "";


                        for (int i = 0; i < gifts.size(); i++) {
                            name1 = gifts.get(i).getName();

                            if (name1 == "username password are not match!") {
                                Toast.makeText(getApplicationContext(), name1, Toast.LENGTH_LONG).show();
                            } else {

                                SharedPreferences sh = getSharedPreferences("username", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edt = sh.edit();
                                edt.putString("username", name1);
                                edt.commit();
                                Intent e = new Intent();
                                e.setClass(UserLogin.this, GiftHome.class);

                                startActivity(e);
                            }
                        }
                    }
                    @Override
                    public void fail(int statusCode, String responseBody) {
                        Toast.makeText(getApplicationContext(), "User name and password are not match", Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar gift_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
