package com.networklite.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author linqi
 * @description
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseResponse(Response response, int id) throws IOException {
        return response.body().string();
    }
}
