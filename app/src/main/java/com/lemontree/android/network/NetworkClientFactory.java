package com.lemontree.android.network;


import com.lemontree.android.BuildConfig;
import com.networklite.HttpsHelper;
import com.networklite.NetworkLite;
import com.networklite.interceptor.HttpLoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkClientFactory {

    private static final long DEFAULT_TIMEOUT = 30_000L; // 默认超时时间(单位:毫秒)

    static NetworkLite createDefaultNetworkLite() {
        return new NetworkLite(createDefaultOkHttpClient());
    }

    private static OkHttpClient createDefaultOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                // 网络请求日志
                .addInterceptor(new HttpLoggerInterceptor(new HttpLoggerInterceptor.Callback() {
                    @Override
                    public boolean getLogOutputState() {
                        return BuildConfig.DEBUG;
                    }

                    @Override
                    public String generateLogTag() {
                        return "OKHttpLog";
                    }
                }))
                .sslSocketFactory(HttpsHelper.getSslSocketFactory(null))
                .hostnameVerifier(new HttpsHelper.UnSafeHostnameVerifier())
                .build();
    }

}
