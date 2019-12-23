package com.sm.android.utils;

import android.content.Context;
import android.util.Log;

import com.sm.android.BuildConfig;
import com.sm.android.base.BaseResponseBean;
import com.sm.android.bean.request.EventMarkReqBean;
import com.sm.android.manager.BaseApplication;
import com.sm.android.manager.NetConstantValue;
import com.sm.android.network.OKHttpClientEngine;
import com.sm.android.uploadUtil.Tools;
import com.sm.android.uploadUtil.UrlHostConfig;
import com.google.gson.Gson;
import com.networklite.NetworkLiteHelper;
import com.networklite.callback.GenericCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;

/**
 * 埋点工具类
 */
public class MarkUtil {
    /**
     * 埋点
     */
    public static void postEvent(Context context, String eventId) {
        postEvent(context, eventId, "");
    }

    public static void postEvent(Context context, String eventId, String bannerId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String time = format.format(new Date());

        EventMarkReqBean eventMarkReqBean = new EventMarkReqBean();
        eventMarkReqBean.eventid = eventId;
        eventMarkReqBean.eventtime = time;
        eventMarkReqBean.deviceid = Tools.getIMSI(context);
        NetworkLiteHelper
                .postJson()
                .url(NetConstantValue.BASE_HOST + NetConstantValue.NET_REQUEST_URL_EVENT_MARK)
                .content(new Gson().toJson(eventMarkReqBean))
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                        if (response != null && BaseResponseBean.SUCCESS.equals(response.res_code)) {
                            if (BuildConfig.DEBUG) {
//                                Toast.makeText(context, "埋点事件：" + eventId, Toast.LENGTH_SHORT).show();
                                Log.d("maidian", eventId);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }

    /*
     * mark导流商家产品获取
     */
    public static void markCustomerProduct(int productId) {
        String url = UrlHostConfig.getFlowIOHost() + NetConstantValue.NET_REQUEST_URL_FLOWIO_MARK_CUSTOMERPRODUCT;
        StringBuilder wholeUrl = new StringBuilder();
        wholeUrl.append(url + "?")
                .append("app_clientid=" + Tools.getChannel())
                .append("&app_version=" + Tools.getAppVersion())
                .append("&app_name=android")
                .append("&id=" + productId)
                .append("&mobile=" + BaseApplication.sPhoneNum);
        NetworkLiteHelper
                .get()
                .url(wholeUrl.toString())
                .build()
                .execute(OKHttpClientEngine.getNetworkClient(), new GenericCallback<BaseResponseBean>() {
                    @Override
                    public void onSuccess(Call call, BaseResponseBean response, int id) {
                    }

                    @Override
                    public void onFailure(Call call, Exception exception, int id) {
                    }
                });
    }
}
