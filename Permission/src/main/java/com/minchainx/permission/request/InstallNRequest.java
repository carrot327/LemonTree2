package com.minchainx.permission.request;

import android.content.Context;

import com.minchainx.permission.base.Action;
import com.minchainx.permission.util.Permission;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * InstallNRequest
 */

public class InstallNRequest extends InstallRequest {

    public InstallNRequest(Context context) {
        super(context);
    }

    @Override
    public void start() {
        new RuntimeMRequest(mContext)
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<String[]>() {
                    @Override
                    public void onAction(String[] data) {
                        callbackDenied();
                        install();
                    }
                })
                .onDenied(new Action<String[]>() {
                    @Override
                    public void onAction(String[] data) {

                    }
                })
                .start();
    }


}
