package com.minchainx.permission.factory;

import android.content.Context;

import com.minchainx.permission.request.OverlayRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * OverlayRequestFactory
 */

public interface OverlayRequestFactory {
    /**
     * Create permission request.
     */
    OverlayRequest create(Context context);
}
