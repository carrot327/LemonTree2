package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.RuntimeRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * RuntimeRequestFactory
 */

public interface RuntimeRequestFactory {

    /**
     * Create permission request.
     */
    RuntimeRequest create(Context context);
}
