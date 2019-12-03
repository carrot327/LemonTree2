package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.InstallRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * InstallRequestFactory
 */

public interface InstallRequestFactory {
    /**
     * Create permission request.
     */
    InstallRequest create(Context context);
}
