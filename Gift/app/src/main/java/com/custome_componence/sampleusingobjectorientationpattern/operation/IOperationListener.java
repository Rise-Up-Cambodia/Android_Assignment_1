package com.custome_componence.sampleusingobjectorientationpattern.operation;

import org.json.JSONObject;

public interface IOperationListener {
    public void success(JSONObject json);
    public void fail (int statusCode, String responseBody);
}
