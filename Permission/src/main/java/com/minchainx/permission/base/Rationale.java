package com.minchainx.permission.base;

import android.content.Context;

/**
 * Created by jimmy on 2018/8/1.
 */

public interface Rationale<T> {

    /**
     * Show rationale to user.
     */
    void showRationale(Context context, T data, RequestExecutor executor);
}