package com.minchainx.permission.setting;

import android.content.Context;
import android.os.Build;

import com.minchainx.permission.factory.OverlayLRequestFactory;
import com.minchainx.permission.factory.OverlayMRequestFactory;
import com.minchainx.permission.factory.OverlayRequestFactory;
import com.minchainx.permission.request.OverlayRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * overlay permission
 */

public class Overlay {

    private static OverlayRequestFactory FACTORY;
    private Context mContext;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new OverlayMRequestFactory();
        } else {
            FACTORY = new OverlayLRequestFactory();
        }
    }

    public Overlay(Context context) {
        this.mContext = context;
    }

    /**
     * Handle overlay permission.
     */
    public OverlayRequest overlay() {
        return FACTORY.create(mContext);
    }
}
