package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.custome_componence.sampleusingobjectorientationpattern.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Author Sreyleak
 * Date : 28/07/2015
 */

public class CustomAdapter extends ArrayAdapter<String> {
    private Activity activity;
//    private ArrayList<String> description;
//    private ArrayList<String> from;
//    private ArrayList<String> category;
//    private ArrayList<String> date;
//    private ArrayList<String> id;
//    private ArrayList<String> fulldescription;
//    private ArrayList<String> image_name;
//    private ArrayList<Bitmap> giftimage;
      private ArrayList<String> names;

    public CustomAdapter(Activity activity, ArrayList<String> names) {

        super(activity, R.layout.item, names);
        this.activity = activity;
        this.names = names;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.item, null, true);

        TextView name1 = (TextView) rowView.findViewById(R.id.txtname);

        name1.setText(names.get(position));

        return rowView;
    }

}

