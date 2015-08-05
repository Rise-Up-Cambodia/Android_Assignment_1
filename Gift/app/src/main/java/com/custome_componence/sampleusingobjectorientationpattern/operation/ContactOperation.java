package com.custome_componence.sampleusingobjectorientationpattern.operation;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONObject;

import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;

import java.io.UnsupportedEncodingException;

/**
 * Created by channy
 */
public class ContactOperation implements IOperation {
    AsyncHttpClient client = new AsyncHttpClient();

    public void updateContact (String name, final IOperationListener iOperationListener){
     RequestParams requestParams = new RequestParams();
        requestParams.add("name", name);
//      requestParams.add("phone", phone);

        client.put(Constant.BASE_URL + "contacts/18.json", requestParams,new AsyncHttpResponseHandler() {
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

    public void getContact (final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();
       // requestParams.add("name", name);
//      requestParams.add("phone", phone);

        client.get(Constant.BASE_URL + "contacts.json",new AsyncHttpResponseHandler() {
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

    public void getContactById (final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();
     //   requestParams.add("age", age);
//      requestParams.add("phone", phone);

        client.get(Constant.BASE_URL + "contacts/19.json",new AsyncHttpResponseHandler() {
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
