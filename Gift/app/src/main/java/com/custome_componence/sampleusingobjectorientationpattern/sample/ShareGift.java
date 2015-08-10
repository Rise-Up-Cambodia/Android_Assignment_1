package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.custome_componence.sampleusingobjectorientationpattern.operation.GiftOperation;
import com.custome_componence.sampleusingobjectorientationpattern.operation.IOperationListener;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/*
* Created by Sreyleak 06/08/2015
* */
public class ShareGift extends ActionBarActivity {
    GiftOperation GiftOperation = new GiftOperation();
    Button btnShare, btnChoose;
    ImageView calendarImage, giftImage;
    EditText description, receivedDate;
    Spinner category, from;
    private int serverResponseCode = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = null;
    private ProgressDialog dialog = null;
    String imagePath = "no image";
    String[] categories = {"Select a category", "Other", "Christmas", "Birthday", "Anniversary", "Graduate", "Marriage", "New Year"};
    String[] fromWho = {"From whom", "Other", "Friend", "Lover", "Co-Worker", "Family"};
    //random number for concatenate image name before upload
    Random random = new Random();
    int ran = random.nextInt(1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_gift);


        btnShare = (Button) findViewById(R.id.btnshare);
        btnChoose = (Button) findViewById(R.id.btnchooseimage);
        description = (EditText) findViewById(R.id.description);
        receivedDate = (EditText) findViewById(R.id.receive_date);
        calendarImage = (ImageView) findViewById(R.id.imgcalendar);
        giftImage = (ImageView) findViewById(R.id.giftimage);
        category = (Spinner) findViewById(R.id.category);
        from = (Spinner) findViewById(R.id.from);
        ArrayAdapter<String> cat1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        ArrayAdapter<String> fr1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, fromWho);
        category.setAdapter(cat1);
        from.setAdapter(fr1);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select A Picture"), SELECT_PICTURE);
            }
        });

        //pop up datepicker when click on edit text view
        calendarImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                Calendar mcurrentDate = Calendar.getInstance();

                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog mDatePicker = new DatePickerDialog(ShareGift.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        setDateToBox(selectedyear, selectedmonth, selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int CategoryNumber = 0;
                if (selectedImagePath == null) {
                    Toast.makeText(ShareGift.this, "Please, select an image", Toast.LENGTH_LONG).show();
                } else {
                    if (description.getText().toString().equals("") || from.getSelectedItem().toString().equals("") ||
                            category.getSelectedItem().toString().equals("") || receivedDate.getText().toString().equals("") ||
                            category.getSelectedItem().toString().equals("Select a category") ||
                            from.getSelectedItem().toString().equals("From whom")) {
                        Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_SHORT).show();
                    } else {
                        //to insert category number into database base on spinner gift_item selected
                        switch (category.getSelectedItem().toString()) {
                            case "Other":
                                CategoryNumber = 2;
                                break;
                            case "Christmas":
                                CategoryNumber = 4;
                                break;
                            case "Birthday":
                                CategoryNumber = 1;
                                break;
                            case "Anniversary":
                                CategoryNumber = 3;
                                break;
                            case "Graduate":
                                CategoryNumber = 6;
                                break;
                            case "Marriage":
                                CategoryNumber = 5;
                                break;
                            default:
                                CategoryNumber = 7;
                        }

                        //to get current date of post
                        Date d = new Date();
                        String crrentdate = new SimpleDateFormat("yyyy-MM-dd").format(d);

                        String des = description.getText().toString();
                        String rdate = receivedDate.getText().toString();

                        GiftOperation.shareGift(des, from.getSelectedItem().toString(), String.valueOf(CategoryNumber), crrentdate, rdate, imagePath, new IOperationListener() {
                            @Override
                            public void success(JSONObject json) {
                                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void fail(int statusCode, String responseBody) {
                                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (selectedImagePath.equals("")) {

                        } else {
                            dialog = ProgressDialog.show(ShareGift.this, "", "Uploading file...", true);
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

                    }
                }
                Intent intentToHome = new Intent(ShareGift.this, GiftHome.class);
                startActivity(intentToHome);
            }
        });
    }

    // select selected date in to edit text
    private void setDateToBox(int year, int month, int day) {
        final Calendar cal = Calendar.getInstance();
        receivedDate.setText(
                new StringBuilder()
                        .append(year).append("-")
                        .append(month + 1).append("-")
                        .append(day));
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
                giftImage.setImageURI(selectedImageUri);

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
                imagePath = imgFile.getName();
//                Bitmap myBitmap = BitmapFactory.decodeFile(newpath);
            }
        }
    }

    public int uploadFile(String sourceFileUri) {

        String upLoadServerUri = "http://192.168.1.113:8585/Android_Assignment_1/GiftApi/app/webroot/upload.php";
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
            Toast.makeText(ShareGift.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Toast.makeText(ShareGift.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
        }
        dialog.dismiss();
        return serverResponseCode;
    }
}
