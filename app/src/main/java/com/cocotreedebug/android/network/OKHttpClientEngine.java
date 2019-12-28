package com.cocotreedebug.android.network;

import com.networklite.NetworkLite;

public class OKHttpClientEngine {

    private static NetworkLite sNetworkClient; // 网络访问客户端

    private OKHttpClientEngine() {
        // empty
    }

    private static class SingletonHolder {
        static final OKHttpClientEngine instance = new OKHttpClientEngine();
    }

    public static OKHttpClientEngine getInstance() {
        return SingletonHolder.instance;
    }

    public static void replaceNetworkClient(NetworkLite networkClient) {
        sNetworkClient = networkClient;
    }

    public static NetworkLite getNetworkClient() {
        if (sNetworkClient == null) {
            synchronized (OKHttpClientEngine.class) {
                if (sNetworkClient == null) {
                    sNetworkClient = NetworkClientFactory.createDefaultNetworkLite();
                }
            }
        }
        return sNetworkClient;
    }

}
