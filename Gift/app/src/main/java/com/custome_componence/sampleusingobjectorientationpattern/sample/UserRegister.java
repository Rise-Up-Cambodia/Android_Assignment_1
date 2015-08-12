package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.converter.UserDataConverter;
import com.custome_componence.sampleusingobjectorientationpattern.model.User;
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;
import com.custome_componence.sampleusingobjectorientationpattern.operation.UserOperation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Created by Vanda 06-12/08/2015
* */
public class UserRegister extends ActionBarActivity {
    UserOperation userOperation = new UserOperation();

    Button btnRegister, btnchoose;
    ImageView  giftimage;
    EditText username,email,password;

    Spinner from;
    private int serverResponseCode = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = null;
    private ProgressDialog dialog = null;
    public static ArrayList<User> users = null;
    String image_path = "no image";
    String[] fromwho = {"Gender", "Male", "Female"};
    //random number for concatenate image name before upload
    Random random = new Random();
    int ran = random.nextInt(1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        getSupportActionBar().setTitle("User Registration");



        btnRegister = (Button) findViewById(R.id.btnshare);
        btnchoose = (Button)findViewById(R.id.btnchooseimage);
        username = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
       giftimage = (ImageView)findViewById(R.id.image);
        from = (Spinner)findViewById(R.id.from);


        // set circle bitmap
        ArrayAdapter<String> fr1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, fromwho);
        from.setAdapter(fr1);

        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select A Picture"), SELECT_PICTURE);
            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (selectedImagePath == null) {
                    Toast.makeText(UserRegister.this, "Please, select photo", Toast.LENGTH_LONG).show();
                } else {
                    if (username.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("") ||
                            from.getSelectedItem().toString().equals("Gender")) {
                        Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_SHORT).show();
                    } else {

                 String emailInput =   email.getText().toString().trim();
                        if (!isValidEmail(emailInput)) {
                            email.setError("Invalid Email"); /*"Invalid Text" or something like getString(R.string.Invalid)*/
                            email.requestFocus();
                        } else {



                            userOperation.convertJSONAuthenticatedSignup(email.getText().toString(), new IOperationListener() {
                                @Override
                                public void success(JSONObject json) {
                        /* These two line of code will be use next time */
                                    UserDataConverter userDataConverter = new UserDataConverter();
                                    users = userDataConverter.convertJSONAuthenticatedSignup(json);


                                    String email1 = "";
                                    for (int i = 0; i < users.size(); i++) {


                                        email1 = users.get(i).getParam();

                                        String name = username.getText().toString();
                                        String emails = email.getText().toString();
                                        String passwords = password.getText().toString();

                                        if (email1 == "Not duplicate user") {

                                            userOperation.registerUser(name, emails, passwords, from.getSelectedItem().toString(), image_path, new IOperationListener() {
                                                @Override
                                                public void success(JSONObject json) {

                                                    Intent e = new Intent();
                                                    e.setClass(UserRegister.this, UserLogin.class);
                              
                                                    startActivity(e);
                                                }

                                                @Override
                                                public void fail(int statusCode, String responseBody) {
                                                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            if (selectedImagePath.equals("")) {

                                            } else {
                                                dialog = ProgressDialog.show(UserRegister.this, "", "Registering ...", true);
                                                new Thread(new Runnable() {
                                                    public void run() {
                                                        runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                System.out.println("is loading");
                                                            }
                                                        });
                                                        int response = uploadFile(selectedImagePath);
                                                        System.out.println("RES : " + response);
                                                    }
                                                }).start();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Email already existed!", Toast.LENGTH_LONG).show();

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
    }
});
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
        getMenuInflater().inflate(R.menu.menu_share_gift, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                giftimage.setImageURI(selectedImageUri);


                Cursor cursor = getContentResolver().query(
                        selectedImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedImagePath = cursor.getString(columnIndex);
                cursor.close();
                //============================================= rename image name before upload===
                int dotCnt = selectedImagePath.indexOf(".");
                int lastslash = selectedImagePath.lastIndexOf("/");
                String filepath = selectedImagePath.substring(0, lastslash + 1);
                String filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1, dotCnt);
                File newName = new File(filename + ran + selectedImagePath.substring(dotCnt));
                String newpath = filepath + newName;
                //img=(ImageView)findViewById(R.id.image1);
                File imgFile = new File(newpath);
                image_path = imgFile.getName();
//                Bitmap myBitmap = BitmapFactory.decodeFile(newpath);
            }
        }
    }

    public int uploadFile(String sourceFileUri) {

        String upLoadServerUri = "http://192.168.1.15:8585/Android_Assignment_1/GiftApi/app/webroot/user_register.php";
        String filePth = sourceFileUri;
        //============================================= rename image name before upload===
        int dotCnt = filePth.indexOf(".");
        String filename = filePth.substring(filePth.lastIndexOf("/") + 1, dotCnt);
        File lastfilename = new File(filename + ran + filePth.substring(dotCnt));

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", filePth);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + lastfilename + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //tv.setText("File Upload Completed.");
                        //     Toast.makeText(form.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ShareGift.this, Home.class);
//                        startActivity(intent);
                    }
                });
            }
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            dialog.dismiss();
            ex.printStackTrace();
            Toast.makeText(UserRegister.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Toast.makeText(UserRegister.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "Exception : " + e.getMessage(), e);
        }
        dialog.dismiss();
        return serverResponseCode;
    }
}
