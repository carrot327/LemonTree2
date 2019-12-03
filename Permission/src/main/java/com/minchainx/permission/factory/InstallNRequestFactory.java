package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.InstallNRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * OverlayLRequestFactory
 */

public class InstallNRequestFactory implements InstallRequestFactory {

    @Override
    public InstallNRequest create(Context context) {
        return new InstallNRequest(context);
    }
}
