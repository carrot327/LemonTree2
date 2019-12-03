package com.minchainx.permission.request;

import android.content.Context;

import com.minchainx.permission.base.Forbid;
import com.minchainx.permission.base.Rationale;
import com.minchainx.permission.check.StrictChecker;


/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * use for request runtime permission
 * adapter Android 5.0(API>=21)
 */

public class RuntimeLRequest extends RuntimeRequest {

    public RuntimeLRequest(Context context) {
        super(context);
    }

    @Override
    public RuntimeLRequest rationale(Rationale<String[]> rationale) {
        return this;
    }

    @Override
    public RuntimeLRequest forbid(Forbid<String[]> forbid) {
        return this;
    }

    @Override
    public void start() {
        String[] deniedPermissions = getDeniedPermissions(new StrictChecker(), mPermissions);
        if (deniedPermissions.length == 0)
            callBackPermissions();
        else
            callBackPermissions(deniedPermissions);
    }

}
