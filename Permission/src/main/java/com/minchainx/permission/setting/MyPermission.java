package com.minchainx.permission.setting;

import android.content.Context;

import com.minchainx.permission.request.InstallRequest;
import com.minchainx.permission.request.OverlayRequest;


/**
 * Created by jimmy on 2018/8/1.
 */
public class MyPermission {

    private MyPermission() {

    }

    /**
     * normal runtime
     */
    public static Runtime runtime(Context context) {
        return new Runtime(context);
    }

    /**
     * overlay
     */
    public static OverlayRequest overlay(Context context) {
        return new Overlay(context).overlay();
    }

    /**
     * install
     */
    public static InstallRequest install(Context context) {
        return new Install(context).install();
    }

}
