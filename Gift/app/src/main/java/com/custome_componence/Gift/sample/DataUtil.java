package com.custome_componence.Gift.sample;

import android.os.Handler;
import android.widget.ArrayAdapter;

/**
 * Created by Leng Chiva on 4/30/2015.
 */
public class DataUtil {
    public static void getData(final boolean isRefresh, final ArrayAdapter<String> aa, final PullListView plv) {


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isRefresh) {
                    aa.clear();
                }
                int currentCount = aa.getCount();
                for (int i = currentCount; i < currentCount + 20; i++) {
                    aa.add("Chiva" + (i + 1) + "Item");
                }

                aa.notifyDataSetChanged();
                plv.refreshComplete();
                plv.getMoreComplete();
            }
        }, 2000);
    }
}
