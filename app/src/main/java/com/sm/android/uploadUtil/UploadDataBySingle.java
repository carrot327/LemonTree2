package com.sm.android.uploadUtil;

import com.google.gson.Gson;

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
 * 文件描述:单独上传信息，而非UploadData这样的联动
 */
public class UploadDataBySingle {

    public interface UploadAddressBookListener {
        void success();

        void error();
    }

    public interface UploadSmsListener {
        void success();

        void error();
    }

    public interface UploadCallRecordListener {
        void success();

        void error();
    }

    public interface UploadAppListListener {
        void success();

        void error();
    }



    /**
     * APP通讯录上传
     */
    public void uploadAddressBook(String user_id, UploadAddressBookListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (Utils.readContacts().size() == 0) {
            map.put("status", "2");
            map.put("list", new ArrayList<>().toArray());
        } else {
            map.put("status", "1");
            map.put("list", Utils.readContacts().toArray());
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

    /**
     * 短信上传
     */
    public void uploadSms(String user_id, UploadSmsListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (Utils.readSms().size() == 0) {

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

    /**
     * 通话记录上传
     */
    public void uploadCallRecord(String user_id, UploadCallRecordListener mListener) {
        Gson gson = new Gson();
        BaseHashMap map = new BaseHashMap();
        map.put("user_id", user_id);
        if (Utils.readCallRecords().size() == 0) {
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

    /**
     * appList上传
     */
    public void uploadAppList(String user_id, UploadAppListListener mListener) {
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
