package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.OverlayLRequest;
import com.minchainx.permission.request.OverlayRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * OverlayLRequestFactory
 */

public class OverlayLRequestFactory implements OverlayRequestFactory {

    @Override
    public OverlayRequest create(Context context) {
        return new OverlayLRequest(context);
    }
}
