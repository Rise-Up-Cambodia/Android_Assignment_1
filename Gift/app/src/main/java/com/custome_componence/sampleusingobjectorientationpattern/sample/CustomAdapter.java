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
import java.util.Date;

/**
 * Author Sreyleak
 * Date : 28/07/2015
 */

public class CustomAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private ArrayList<String> description;
    private ArrayList<String> from;
    private ArrayList<String> category;
    private ArrayList<String> posts;
    private ArrayList<Bitmap> im;
    private ArrayList<String> names;
    private ArrayList<String> gift_path;
    private ArrayList<String> id;

    public CustomAdapter(Activity activity, ArrayList<String> names,ArrayList<String> posts,ArrayList<String> categories,ArrayList<String>
            froms,ArrayList<String> descriptions, ArrayList<String> gift_path,  ArrayList<Bitmap> im, ArrayList<String> id) {

        super(activity, R.layout.gift_item, names);
        this.activity = activity;
        this.names = names;
        this.posts = posts;
        this.from = froms;
        this.category = categories;
        this.description = descriptions;
        this.gift_path = gift_path;
        this.im = im;
        this.id = id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.gift_item, null, true);

        TextView Name = (TextView) rowView.findViewById(R.id.name);
        TextView Posts = (TextView) rowView.findViewById(R.id.post);
        TextView From = (TextView) rowView.findViewById(R.id.from);
        TextView giftid = (TextView) rowView.findViewById(R.id.giftid);
        TextView Category = (TextView) rowView.findViewById(R.id.category);
        TextView Description = (TextView) rowView.findViewById(R.id.description);
        ImageView image = (ImageView) rowView.findViewById(R.id.imageView3);

        Name.setText(names.get(position));
        Posts.setText("Posted "+ (posts.get(position)));
        From.setText("From "+ (from.get(position)));
        Category.setText("For "+ (category.get(position)));
        giftid.setText(id.get(position));
        image.setImageBitmap(im.get(position));
        Description.setText(description.get(position));
        loadBitmap(im.get(position), image);

        return rowView;
    }
    public void loadBitmap(Bitmap img, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(img);
    }

    class BitmapWorkerTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private Bitmap data = null;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            data = params[0];
            return decodeSampledBitmapFromResource(data, 250, 250);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageview = imageViewReference.get();
                if (imageview != null) {
                    imageview.setImageBitmap(bitmap);
                }
            }
        }

        public Bitmap decodeSampledBitmapFromResource(Bitmap bitmap,
                                                      int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(getContext().getResources(), bitmap, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return bitmap;
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

}

