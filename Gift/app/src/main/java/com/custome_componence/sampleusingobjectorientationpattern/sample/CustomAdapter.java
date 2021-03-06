package com.custome_componence.sampleusingobjectorientationpattern.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.custome_componence.sampleusingobjectorientationpattern.R;
import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;
import com.custome_componence.sampleusingobjectorientationpattern.model.Gift;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

/**
 * Author Vanda 4/08/2015
 */

public class CustomAdapter extends ArrayAdapter<Gift> {

    private Activity activity;
    ArrayList<Gift> gifts;

    public CustomAdapter(Activity activity, ArrayList<Gift> gifts) {

        super(activity, R.layout.gift_item, gifts);

        this.activity = activity;
        this.gifts = gifts;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.gift_item, null, true);

        Gift giftOb = gifts.get(position);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView createdDate = (TextView) rowView.findViewById(R.id.post);
        TextView From = (TextView) rowView.findViewById(R.id.from);
        TextView giftId = (TextView) rowView.findViewById(R.id.giftid);
        TextView Category = (TextView) rowView.findViewById(R.id.category);
        TextView Description = (TextView) rowView.findViewById(R.id.description);
        ImageView image = (ImageView) rowView.findViewById(R.id.imageView3);
        ImageView userImage = (ImageView) rowView.findViewById(R.id.userimage);


        name.setText(giftOb.getName());
        createdDate.setText("Posted " + giftOb.getPost());
        From.setText("From " + (giftOb.getFrom()));
        Category.setText("For " + (giftOb.getCategory()));
        giftId.setText(giftOb.getId());
        Description.setText(giftOb.getDescription());

        /**
         * Author Sreyleak 12/08/2015
         * load gift image
         */
        ImageViewAware imageViewAware = new ImageViewAware(image);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL1 + "app/webroot/img/" +
                giftOb.getImageName(), imageViewAware, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason
                    failReason) {

                super.onLoadingFailed(imageUri, view, failReason);

            }
        });
        /**
         * Author Sreyleak 12/08/2015
         * load user image
         */
        ImageViewAware imageViewUser = new ImageViewAware(userImage);
        ImageLoader.getInstance().displayImage(Constant.BASE_URL1 + "app/webroot/user_photo/" +
                giftOb.getUserProfile(), imageViewUser, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason
                    failReason) {
                super.onLoadingFailed(imageUri, view, failReason);

            }
        });
        return rowView;
    }

}

