package com.minchainx.permission.request;


import com.minchainx.permission.base.Action;
import com.minchainx.permission.base.Rationale;

/**
 * Created by jimmy on 2018/8/1.
 * <p>
 * permission request
 */

public interface PRequest<T> {

    /**
     * Set request rationale.
     */
    PRequest rationale(Rationale<T> rationale);

    /**
     * Action to be taken when permissions are granted.
     */
    PRequest onGranted(Action<T> granted);

    /**
     * Action to be taken when permissions are denied.
     */
    PRequest onDenied(Action<T> denied);

    /**
     * Request permission.
     */
    void start();
}
