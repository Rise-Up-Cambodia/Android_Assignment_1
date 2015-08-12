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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLogin extends Activity {

    private Button btnAdd;
    EditText email;
    EditText password;
    public static ArrayList<User> gifts = null;
    public static User gift = null;

    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnAdd = (Button) findViewById(R.id.register);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        final UserOperation userOperation = new UserOperation();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields required!", Toast.LENGTH_SHORT).show();

                } else {
                    String emailInput = email.getText().toString().trim();
                    if (!isValidEmail(emailInput)) {
                        email.setError("Invalid Email"); /*"Invalid Text" or something like getString(R.string.Invalid)*/
                        email.requestFocus();
                    } else {
                        userOperation.login(email.getText().toString(), password.getText().toString(), new IOperationListener() {
                            @Override
                            public void success(JSONObject json) {
                        /* These two line of code will be use next time */
                                UserDataConverter userDataConverter = new UserDataConverter();
                                gifts = userDataConverter.convertJSONToLogin(json);

                                String name1 = "";
                                int userId = 0;
                                for (int i = 0; i < gifts.size(); i++) {
                                    name1 = gifts.get(i).getParam();
                                    userId = gifts.get(i).getUserId();

                                    if (name1 == "username password are not match!") {
                                        Toast.makeText(getApplicationContext(), name1, Toast.LENGTH_LONG).show();
                                    } else {
                                        SharedPreferences sh = getSharedPreferences("username", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = sh.edit();
                                        edt.putString("username", name1);
                                        edt.putInt("userId", userId);
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
                }
            }
        });

        initializeImageLoader(this);
    }
    /**
     * Sreyleak 12/08/2015
     */

    public void initializeImageLoader(Context context){

        // Universal Image Loader, Display Option Config
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnLoading(R.color.material_blue_grey_800)
                .build();

        // ImageLoader config setting
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

    }
    private boolean isValidEmail(String emailInput) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailInput);
        return matcher.matches();
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
