package com.custome_componence.sampleusingobjectorientationpattern.operation;

import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by riseupcambodia on 8/7/2015.
 */
public class UserOperation implements IOperation {

    AsyncHttpClient Userclient = new AsyncHttpClient();

    public void login(String username, String password,final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();


        Userclient.get(Constant.BASE_URL + "users/view/"+username+"/"+password+".json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject obj = new JSONObject(data);
                        iOperationListener.success(obj);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    iOperationListener.fail(statusCode, data);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
