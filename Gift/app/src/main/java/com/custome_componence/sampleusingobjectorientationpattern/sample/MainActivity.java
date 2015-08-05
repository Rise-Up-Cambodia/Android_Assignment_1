package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.converter.ContactDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.Contact;
import com.custome_componence.sampleusingobjectorientationpattern.operation.ContactOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private Button btnAdd;
    private String name = "";
    private String phone = "";
    TextView tv;
    EditText edt;
    ListView lv;
    public static ArrayList<Contact> contacts = null;
    public static Contact contact = null;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        edt = (EditText)findViewById(R.id.changename);
        tv = (TextView)findViewById(R.id.age);
        lv = (ListView)findViewById(R.id.listView);
        final ContactOperation contactOperation = new ContactOperation();


        contactOperation.getContactById(new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                        /* These two line of code will be use next time */
               ContactDataConverter contactDataConverter = new ContactDataConverter();
                contact = contactDataConverter.convertJSONToDetail(json);
                String age = contact.getAge();

                Toast.makeText(getApplicationContext(), age, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void fail(int statusCode, String responseBody) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        contactOperation.getContact( new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                /* These two line of code will be use next time */
                ContactDataConverter contactDataConverter = new ContactDataConverter();
                contacts = contactDataConverter.convertJSONToContact(json);
                String name1 = "", phone1 = "";
                for (int i = 0; i < contacts.size(); i++) {
                    name1 = contacts.get(i).getName();
                    names.add(name1);
//                    phone1 = contacts.get(i).getPhone();
//                    Toast.makeText(getApplicationContext(), name1 + ", " + phone1, Toast.LENGTH_LONG).show();
                }
                CustomAdapter ad = new CustomAdapter(MainActivity.this,names);
                lv.setAdapter(ad);
               // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(int statusCode, String responseBody) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                name = "NaNa";
//                phone = "077987654";

                contactOperation.updateContact(edt.getText().toString(),new IOperationListener() {
                    @Override
                    public void success(JSONObject json) {
                        /* These two line of code will be use next time */
//                        ContactDataConverter contactDataConverter = new ContactDataConverter();
//                        contacts = contactDataConverter.convertJSONToContact(json);
//                        String name1 = "", phone1 = "";
//                        for (int i = 0; i < contacts.size(); i++) {
//                            name1 = contacts.get(i).getName();
//                            phone1 = contacts.get(i).getPhone();
//                            Toast.makeText(getApplicationContext(), name1 + ", " + phone1, Toast.LENGTH_LONG).show();
//                        }
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail(int statusCode, String responseBody) {
                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
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
        // Handle action bar item clicks here. The action bar will
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
