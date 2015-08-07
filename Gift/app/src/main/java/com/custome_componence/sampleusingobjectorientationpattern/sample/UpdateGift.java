package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Random;

public class UpdateGift extends ActionBarActivity {
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
        setContentView(R.layout.activity_update_gift);

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

        GiftOperation.updateGift(new IOperationListener() {
            @Override
            public void success(JSONObject json) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void fail(int statusCode, String responseBody) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
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
}
