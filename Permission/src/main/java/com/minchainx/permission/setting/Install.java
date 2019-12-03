package com.minchainx.permission.setting;

import android.content.Context;
import android.os.Build;

import com.minchainx.permission.factory.InstallNRequestFactory;
import com.minchainx.permission.factory.InstallORequestFactory;
import com.minchainx.permission.factory.InstallRequestFactory;
import com.minchainx.permission.request.InstallRequest;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * install permission
 */

public class Install {

    private static InstallRequestFactory FACTORY;
    private Context mContext;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            FACTORY = new InstallORequestFactory();
        } else {
            FACTORY = new InstallNRequestFactory();
        }
    }

    public Install(Context context) {
        this.mContext = context;
    }

    /**
     * Handle overlay permission.
     */
    public InstallRequest install() {
        return FACTORY.create(mContext);
    }
}
