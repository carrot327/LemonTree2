package com.minchainx.permission.base;

import android.content.Context;

/**
 * Created by jimmy on 2018/8/1.
 */

public interface Forbid<T> {

    /**
     * Show always forbidden deny dialog to user.
     */
    void showForbidden(Context context, T data, RequestExecutor executor);
}
