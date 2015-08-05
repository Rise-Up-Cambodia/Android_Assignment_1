package com.custome_componence.sampleusingobjectorientationpattern.operation;

import org.json.JSONObject;

/**
 * Created by Channy on 8/3/2015.
 */
public interface IOperationListener {
    public void success(JSONObject json);
    public void fail (int statusCode, String responseBody);
}
