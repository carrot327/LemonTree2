package com.cocotree.android.uploadUtil;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.cocotree.android.service.LocationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadNecessaryData {

    private boolean uploadAddressBookSucceed = false;
    private boolean uploadDeviceInfoSucceed = false;

    private boolean isGranted = false;

    public interface UploadDataListener {
        void success();

        void error();
    }

    public void upload(String userId, UploadDataListener mListener, boolean isGranted) {
        this.isGranted = isGranted;
        uploadAddressBook(userId, mListener);

        if ((int) LocationService.getInstance().getLocationX() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadDeviceInfo(userId, mListener);
                }
            }, 1500);
        } else {
            uploadDeviceInfo(userId, mListener);
        }
    }

    /**
     * APP通讯录上传
     */
    private void uploadAddressBook(String user_id, UploadDataListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (isGranted && Utils.readContacts().size() != 0) {
            map.put("status", "1");
            map.put("list", Utils.readContacts().toArray());
        } else {
            map.put("status", "2");
            map.put("list", new ArrayList<>().toArray());
        }

        String json = gson.toJson(map);
        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadAddressBook())
                .post(RequestBody.create(MediaType.parse("application/json"), json))
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
                        uploadAddressBookSucceed = true;
                        Log.d("karl", "uploadAddressBookSucceed:" + uploadAddressBookSucceed);
                        if (isAllUploadSuccess()) {
                            Log.d("karl", "isAllUploadSuccess:" + isAllUploadSuccess());
                            mListener.success();
                        }
                    } else {
                        mListener.error();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设备信息上传
     */
    private void uploadDeviceInfo(String user_id, UploadDataListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        map.put("status", "1");
        map.put("list", Utils.getDeviceInfo());

        String json = gson.toJson(map);
        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadDeviceInfo())
                .post(RequestBody.create(MediaType.parse("application/json"), json))
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

                        uploadDeviceInfoSucceed = true;
                        if (isAllUploadSuccess()) {
                            mListener.success();
                        }
                    } else {

                        mListener.error();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 判断2个接口是否都成功
     */
    private boolean isAllUploadSuccess() {
        return uploadDeviceInfoSucceed && uploadAddressBookSucceed;
    }
}
