package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.InstallORequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * OverlayLRequestFactory
 */

public class InstallORequestFactory implements InstallRequestFactory {

    @Override
    public InstallORequest create(Context context) {
        return new InstallORequest(context);
    }
}
