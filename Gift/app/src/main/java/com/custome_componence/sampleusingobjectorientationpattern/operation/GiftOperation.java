package com.custome_componence.sampleusingobjectorientationpattern.operation;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONObject;

import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;

import java.io.UnsupportedEncodingException;

public class GiftOperation implements IOperation {
    AsyncHttpClient Giftclient = new AsyncHttpClient();

    public void getAllGift(final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();
        Giftclient.get(Constant.BASE_URL + "gifts/index/3.json",new AsyncHttpResponseHandler() {
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

    /*
    * Created by Sreyleak 07/08/2015
    * */
    public void getGiftById(String id, final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();
        Giftclient.get(Constant.BASE_URL + "gifts/"+id+".json",requestParams,new AsyncHttpResponseHandler() {
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

    /*
    * Created by Sreyleak 07/08/2015
    * */
    public void shareGift(int userId, String description, String from, String category, String date, String receive_date,
                          String image_path, final IOperationListener iOperationListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("description", description);
        requestParams.add("from", from);
        requestParams.add("cat_id", category);
        requestParams.add("user_id", String.valueOf(userId));
        requestParams.add("status", "1");
        requestParams.add("date", date);
        requestParams.add("receive_date", receive_date);
        requestParams.add("gift_name", image_path);

        Giftclient.post(Constant.BASE_URL + "gifts.json", requestParams, new AsyncHttpResponseHandler() {
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
    /*
    * Created by Sreyleak 07/08/2015
    * */
    public void deleteGift(String giftId, final IOperationListener iOperationListener) {
        Giftclient.delete(Constant.BASE_URL1 + "gifts/"+giftId+".json", new AsyncHttpResponseHandler() {
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

    /*
    * Created by Sreyleak 11/08/2015
    * */
    public void updateGift(String id, String description, String from, String category, String date, String receive_date,
                           String image_path,final IOperationListener iOperationListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("description", description);
        requestParams.add("from", from);
        requestParams.add("cat_id", category);
        requestParams.add("date", date);
        requestParams.add("receive_date", receive_date);
        requestParams.add("gift_name",image_path );
        Giftclient.put(Constant.BASE_URL1 + "gifts/"+ id +".json", requestParams, new AsyncHttpResponseHandler() {
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
    /*
    * Created by Sreyleak 11/08/2015
    * */
    public void getGiftByPage(int pageNumber, final IOperationListener iOperationListener){
        RequestParams requestParams = new RequestParams();
        requestParams.add("pageNumber", String.valueOf(pageNumber));
        Giftclient.get(Constant.BASE_URL1 + "gifts/index/"+pageNumber+".json", requestParams, new AsyncHttpResponseHandler() {
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
