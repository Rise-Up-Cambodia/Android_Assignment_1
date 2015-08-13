package com.custome_componence.Gift.sample;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.custome_componence.Gift.R;
import com.custome_componence.Gift.config.Constant;
import com.custome_componence.Gift.operation.GiftOperation;
import com.custome_componence.Gift.operation.IOperationListener;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/*
* Created by Sreyleak 10/08/2015
* */
public class UpdateGift extends ActionBarActivity {
    GiftOperation GiftOperation = new GiftOperation();
    Button btnUpdate, btnChoose;
    ImageView calendarImage, giftImage;
    EditText description, receivedDate;
    Spinner category, from;
    private int serverResponseCode = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath = null;
    private ProgressDialog dialog = null;
    String imagePath = "no image";
    String[] arrCategories = {"Select a category", "Other", "Christmas", "Birthday", "Anniversary", "Graduate", "Marriage", "New Year"};
    String[] arrFrom = {"From whom", "Other", "Friend", "Lover", "Co-Worker", "Family"};
    String imagePathForUpdate = "";

    //random number for concatenate image name before upload
    Random random = new Random();
    int ran = random.nextInt(1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gift);

        getSupportActionBar().setTitle("Update Gift");

        btnUpdate = (Button) findViewById(R.id.btnupdate);
        btnChoose = (Button) findViewById(R.id.btnchooseimage);
        description = (EditText) findViewById(R.id.description);
        receivedDate = (EditText) findViewById(R.id.receive_date);
        calendarImage = (ImageView) findViewById(R.id.imgcalendar);
        giftImage = (ImageView) findViewById(R.id.giftimage);
        category = (Spinner) findViewById(R.id.category);
        from = (Spinner) findViewById(R.id.from);
        ArrayAdapter<String> cat1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrCategories);
        ArrayAdapter<String> fr1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrFrom);
        category.setAdapter(cat1);
        from.setAdapter(fr1);


        // initialize default text in edit text box
        final Intent intentFromDetailActivity = getIntent();
        description.setText(intentFromDetailActivity.getStringExtra("description"));
        receivedDate.setText(intentFromDetailActivity.getStringExtra("receiveDate"));
        String categoryNumber = intentFromDetailActivity.getStringExtra("category");
        String fromIndex = intentFromDetailActivity.getStringExtra("from");
        imagePathForUpdate = intentFromDetailActivity.getStringExtra("giftName");
        new DownloadImageTask().execute(Constant.BASE_URL + "app/webroot/img/" + imagePathForUpdate);

        for (int i = 0; i < cat1.getCount(); i++) {
            if (categoryNumber.equals(cat1.getItem(i).toString())) {
                category.setSelection(i);
                break;
            }
        }

        for (int j = 0; j < fr1.getCount(); j++) {
            if (fromIndex.equals(fr1.getItem(j).toString())) {
                from.setSelection(j);
                break;
            }
        }


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
                DatePickerDialog mDatePicker = new DatePickerDialog(UpdateGift.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        setDateToBox(selectedyear, selectedmonth, selectedday);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idForUpdate = intentFromDetailActivity.getStringExtra("id");
                String descriptionForUpdate = description.getText().toString();
                String fromForUpdate = from.getSelectedItem().toString();
                String categoryForUpdate = category.getSelectedItem().toString();
                String receivedDateForUpdate = receivedDate.getText().toString();
                //to get current date of post
                Date d = new Date();
                String crrentDate = new SimpleDateFormat("yyyy-MM-dd").format(d);
                //to insert category number into database base on spinner item selected
                int CateNumber = 0;
                switch (categoryForUpdate) {
                    case "Other":
                        CateNumber = 2;
                        break;
                    case "Christmas":
                        CateNumber = 4;
                        break;
                    case "Birthday":
                        CateNumber = 1;
                        break;
                    case "Anniversary":
                        CateNumber = 3;
                        break;
                    case "Graduate":
                        CateNumber = 6;
                        break;
                    case "Marriage":
                        CateNumber = 5;
                        break;
                    default:
                        CateNumber = 7;
                }
                if (selectedImagePath == null) {
                    if (descriptionForUpdate.equals("") || fromForUpdate.equals("") ||
                            categoryForUpdate.equals("") || receivedDateForUpdate.equals("") ||
                            categoryForUpdate.equals("Select a category") ||
                            fromForUpdate.equals("From whom")) {
                        Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_SHORT).show();
                    } else {
                        imagePath = imagePathForUpdate;
                        GiftOperation.updateGift(idForUpdate, descriptionForUpdate, fromForUpdate, String.valueOf(CateNumber),
                                crrentDate, receivedDateForUpdate, imagePath, new IOperationListener() {
                                    @Override
                                    public void success(JSONObject json) {
                                        Intent intentToHome = new Intent(UpdateGift.this, GiftHome.class);
                                        startActivity(intentToHome);
                                    }

                                    @Override
                                    public void fail(int statusCode, String responseBody) {
                                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                } else {
                    if (descriptionForUpdate.equals("") || fromForUpdate.equals("") ||
                            categoryForUpdate.equals("") || receivedDateForUpdate.equals("") ||
                            categoryForUpdate.equals("Select a category") ||
                            fromForUpdate.equals("From whom")) {
                        Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_SHORT).show();
                    } else {

                        GiftOperation.updateGift(idForUpdate, descriptionForUpdate, fromForUpdate, String.valueOf(CateNumber),
                                crrentDate, receivedDateForUpdate, imagePath, new IOperationListener() {
                                    @Override
                                    public void success(JSONObject json) {
                                        Intent intentToHome = new Intent(UpdateGift.this, GiftHome.class);
                                        startActivity(intentToHome);
                                    }

                                    @Override
                                    public void fail(int statusCode, String responseBody) {
                                        Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            dialog = ProgressDialog.show(UpdateGift.this, "", "Uploading file...", true);
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
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_gift, menu);
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

    // select selected date in to edit text
    private void setDateToBox(int year, int month, int day) {
        final Calendar cal = Calendar.getInstance();
        receivedDate.setText(
                new StringBuilder()
                        .append(year).append("-")
                        .append(month + 1).append("-")
                        .append(day));
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

        String upLoadServerUri = Constant.BASE_URL + "app/webroot/upload_gift.php";
        String filePth = sourceFileUri;
        //============================================= rename image name before upload===
        int dotCnt = filePth.indexOf(".");
        String filename = filePth.substring(filePth.lastIndexOf("/") + 1, dotCnt);
        File resultFilename = new File(filename + ran + filePth.substring(dotCnt));

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
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + resultFilename + "\"" + lineEnd);
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
            Toast.makeText(UpdateGift.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();
            Toast.makeText(UpdateGift.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
        }
        dialog.dismiss();
        return serverResponseCode;
    }
}
