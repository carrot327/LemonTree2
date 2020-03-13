package com.lemontree.android.uploadUtil;

import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.manager.NetConstantValue;

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

public class UploadAuthImg {
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
        if (imgMap.containsKey("KTP_HOLD2")) {
            builder.addFormDataPart("self_kpt_pre_file", "self_kpt_pre_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("KTP_HOLD2")));
        }
        if (imgMap.containsKey("STAFF_CARD3")) {
            builder.addFormDataPart("work_card_file", "work_card_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("STAFF_CARD3")));
        }
        if (imgMap.containsKey("GZD4")) {
            builder.addFormDataPart("wage_card_file", "wage_card_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("GZD4")));
        }
        if (imgMap.containsKey("GZXC5")) {
            builder.addFormDataPart("license_card_file", "license_card_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("GZXC5")));
        }
        if (imgMap.containsKey("YYZC6")) {
            builder.addFormDataPart("photo_card_file", "photo_card_file.jpg", RequestBody.create(MediaType.parse("image/jpeg"), imgMap.get("YYZC6")));
        }

        OK.getInstance().newCall(new Request.Builder()
                .url(NetConstantValue.BASE_HOST + ConstantValue.AUTH_IMG_INFO)
                .post(builder.build())
                .build()).enqueue(new Callback() {
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
