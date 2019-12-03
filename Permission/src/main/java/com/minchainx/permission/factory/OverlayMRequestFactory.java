package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.OverlayMRequest;
import com.minchainx.permission.request.OverlayRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * OverlayMRequestFactory
 */

public class OverlayMRequestFactory implements OverlayRequestFactory {

    @Override
    public OverlayRequest create(Context context) {
        return new OverlayMRequest(context);
    }
}
