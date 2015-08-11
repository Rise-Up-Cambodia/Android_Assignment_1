package com.custome_componence.sampleusingobjectorientationpattern.operation;

import com.loopj.android.http.*;

import org.apache.http.Header;
import org.json.JSONObject;

import com.custome_componence.sampleusingobjectorientationpattern.config.Constant;

import java.io.UnsupportedEncodingException;

/**
 * Created by sreyeleak 05/08/2015
 */
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
        requestParams.add("id", id);
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
    public void shareGift(String description, String from, String category, String date, String receive_date,
                          String image_path, final IOperationListener iOperationListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.add("description", description);
        requestParams.add("from", from);
        requestParams.add("cat_id", category);
        requestParams.add("user_id", "1");
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
    * Created by Sreyleak 07/08/2015
    * */
    public void updateGift(final IOperationListener iOperationListener) {
//        RequestParams requestParams = new RequestParams();
//        requestParams.add("description", description);
//        requestParams.add("from", from);
//        requestParams.add("cat_id", category);
//        requestParams.add("user_id", "1");
//        requestParams.add("status", "1");
//        requestParams.add("date", date);
//        requestParams.add("receive_date", receive_date);
//        requestParams.add("gift_name",image_path );
        RequestParams requestParams = new RequestParams();
        requestParams.add("description", "aa");
        requestParams.add("from", "friend");
        requestParams.add("cat_id", "1");
        requestParams.add("user_id", "1");
        requestParams.add("status", "1");
        requestParams.add("date", "2015-08-04");
        requestParams.add("receive_date", "2015-08-04");
        requestParams.add("gift_name", "aa.jpg");
        Giftclient.put(Constant.BASE_URL1 + "gifts/9.json", requestParams, new AsyncHttpResponseHandler() {
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
