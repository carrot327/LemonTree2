package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.RuntimeLRequest;
import com.minchainx.permission.request.RuntimeRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * RuntimeLRequestFactory
 */

public class RuntimeLRequestFactory implements RuntimeRequestFactory {

    @Override
    public RuntimeRequest create(Context context) {
        return new RuntimeLRequest(context);
    }
}
