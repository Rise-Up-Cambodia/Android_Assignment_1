package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.custome_componence.sampleusingobjectorientationpattern.R;

/**
 * Created by VANDA on 06/08/2015.
 */
public class TestActivity extends Activity {

    String noname = "No user";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        SharedPreferences sh1 = getSharedPreferences("user", Context.MODE_PRIVATE);

        TextView user= (TextView) findViewById(R.id.textView);

        String name = sh1.getString("name",noname);
        user.setText("user =  "+name);

    }
}