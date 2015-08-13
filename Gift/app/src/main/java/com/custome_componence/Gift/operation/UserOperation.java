package com.custome_componence.Gift.operation;

import com.custome_componence.Gift.config.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
/**
 * Created by Vanda on 7/8/2015.
 */
public class UserOperation implements IOperation {

    AsyncHttpClient Userclient = new AsyncHttpClient();

    public void login(String email, String password, final IOperationListener iOperationListener) {

        Userclient.get(Constant.BASE_URL1 + "users/view/" + email + "/" + password + ".json", new AsyncHttpResponseHandler() {
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

    public void getUserEmails(String email, final IOperationListener iOperationListener) {
        RequestParams requestParams = new RequestParams();


        Userclient.get(Constant.BASE_URL1 + "users/signupauthentication/" + email + ".json", new AsyncHttpResponseHandler() {
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


    public void registerUser(String name, String email, String password, String from,
                             String imagePath, final IOperationListener iOperationListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("name", name);
        requestParams.add("email", email);
        requestParams.add("password", password);
        requestParams.add("gender", from);
        requestParams.add("user_profile", imagePath);


        Userclient.post(Constant.BASE_URL + "users/signup.json",requestParams,new AsyncHttpResponseHandler() {


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
