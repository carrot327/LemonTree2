package com.cocotree.android.uploadUtil;

import android.app.Activity;
import android.content.Context;

import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.utils.CProgressDialogUtils;
import com.cocotree.android.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImg {
    private boolean uploadFaceImageSucceed = false;
    private boolean uploadEncryptFaceImageSucceed = false;

    private boolean uploadImageRequestFinished = false;
    private boolean uploadEncryptRequestFinished = false;
    private boolean hasSendBackError = false;

    public interface UploadLivenessInfoListener {
        void success();

        void error();
    }

    public void upload(Context context, File fileImg, File fileEncrypt, int failedCount, UploadLivenessInfoListener mListener) {
        uploadLivenessInfo(context, fileImg, failedCount, mListener);
        uploadLivenessEncryptionData(context, fileEncrypt, mListener);
    }

    private void uploadLivenessInfo(Context context, File file, int failedCount, UploadLivenessInfoListener mListener) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", BaseApplication.mUserId)
                .addFormDataPart("app_version", Tools.getAppVersion())
                .addFormDataPart("app_name", Tools.getAppName())
                .addFormDataPart("app_clientid", Tools.getChannel())
                .addFormDataPart("pre_score", "1")
                .addFormDataPart("try_count", failedCount + 1 + "")
                .addFormDataPart("face_rec_file", "liveness.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), file));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(UrlHostConfig.uploadLivenessInfo())
                .post(requestBody)
                .build();

        CProgressDialogUtils.showProgressDialog((Activity) context);

        OK.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                uploadImageRequestFinished = true;
                if (isAllRequestFinished()) {
                    CProgressDialogUtils.cancelProgressDialog((Activity) context);
                }
                if (!hasSendBackError) {
                    hasSendBackError = true;
                    mListener.error();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                uploadImageRequestFinished = true;
                if (isAllRequestFinished()) {
                    CProgressDialogUtils.cancelProgressDialog((Activity) context);
                }
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.optString("res_code").equals("0000")) {
                        uploadFaceImageSucceed = true;
                        if (isAllUploadSuccess()) {
                            mListener.success();
                        }
                    } else {
                        if (obj.optString("res_msg").contains("Semua item sertifikasi Anda telah disertifikasi")) {
                            uploadFaceImageSucceed = true;
                            if (isAllUploadSuccess()) {
                                mListener.success();
                            }

                        } else {
                            if (!hasSendBackError) {
                                hasSendBackError = true;
                                mListener.error();
                            }
                            UIUtils.showToast(obj.optString("res_msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 上传加密数据
     *
     * @param context
     * @param file
     * @param mListener
     */
    private void uploadLivenessEncryptionData(Context context, File file, UploadLivenessInfoListener mListener) {

        CProgressDialogUtils.showProgressDialog((Activity) context);

        OK.getInstance().newCall(new Request.Builder()
                .url(UrlHostConfig.uploadLivenessEncryptionData())
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", BaseApplication.mUserId)
                        .addFormDataPart("app_version", Tools.getAppVersion())
                        .addFormDataPart("app_name", Tools.getAppName())
                        .addFormDataPart("app_clientid", Tools.getChannel())
                        .addFormDataPart("face_rec_file", "liveness_file_encrypt",
                                RequestBody.create(MediaType.parse("image/jpeg"), file)).build())
                .build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                UIUtils.showToast("onFailure");

                uploadEncryptRequestFinished = true;
                if (isAllRequestFinished()) {
                    CProgressDialogUtils.cancelProgressDialog((Activity) context);
                }
                if (!hasSendBackError) {
                    hasSendBackError = true;
                    mListener.error();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                uploadEncryptRequestFinished = true;
                if (isAllRequestFinished()) {
                    CProgressDialogUtils.cancelProgressDialog((Activity) context);
                }
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    if (obj.optString("res_code").equals("0000")) {
                        uploadEncryptFaceImageSucceed = true;
                        if (isAllUploadSuccess()) {
                            mListener.success();
                        }
                    } else {
                        if (obj.optString("res_msg").contains("Semua item sertifikasi Anda telah disertifikasi")) {
                            uploadEncryptFaceImageSucceed = true;
                            if (isAllUploadSuccess()) {
                                mListener.success();
                            }
                        } else {
                            if (!hasSendBackError) {
                                hasSendBackError = true;
                                mListener.error();
                            }
                            UIUtils.showToast(obj.optString("res_msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isAllUploadSuccess() {
        return uploadFaceImageSucceed && uploadEncryptFaceImageSucceed;
    }

    private boolean isAllRequestFinished() {
        return uploadImageRequestFinished && uploadEncryptRequestFinished;
    }
}
