package com.minchainx.permission.base;

/**
 * Created by jimmy on 2018/8/1.
 */

public interface Action<T> {

    /**
     * An action.
     *
     * @param data data
     */
    void onAction(T data);
}
