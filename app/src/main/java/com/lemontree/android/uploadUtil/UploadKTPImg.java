package com.lemontree.android.uploadUtil;

import com.lemontree.android.manager.BaseApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadKTPImg {
    public interface UploadImgListener {
        void success();

        void error();
    }

    public void upload(Map<String, File> imgMap, UploadImgListener mListener) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", BaseApplication.mUserId)
                .addFormDataPart("app_version", Tools.getAppVersion())
                .addFormDataPart("app_name", Tools.getAppName())
                .addFormDataPart("app_clientid", Tools.getChannel());

        if (imgMap.containsKey("KTP1")) {
            builder.addFormDataPart("kpt_pre_file", "kpt_pre_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("KTP1")));
        }

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(UrlHostConfig.getOCRAuthenticateUrl())
                .post(requestBody)
                .build();
        Call call = OK.getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                mListener.error();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.optString("res_code").equals("0000")) {

                        mListener.success();
                    } else {

                        mListener.error();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
