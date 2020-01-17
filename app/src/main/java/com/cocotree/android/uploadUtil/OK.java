package com.cocotree.android.uploadUtil;

import okhttp3.OkHttpClient;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/20 13:32
 * 文件描述:
 */
public class OK {

    private static volatile OkHttpClient INSTANCE = null;
    public static Object obj = new Object();

    public static OkHttpClient getInstance() {
        if (INSTANCE == null) {
            synchronized (obj) {
                if (INSTANCE == null) {

                    INSTANCE = new OkHttpClient.Builder()
                            .addInterceptor(new LoggerInterceptor())
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
