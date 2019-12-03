package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.RuntimeMRequest;
import com.minchainx.permission.request.RuntimeRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * RuntimeMRequestFactory
 */

public class RuntimeMRequestFactory implements RuntimeRequestFactory {

    @Override
    public RuntimeRequest create(Context context) {
        return new RuntimeMRequest(context);
    }
}
