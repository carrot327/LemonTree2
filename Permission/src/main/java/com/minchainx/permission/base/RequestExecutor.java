package com.minchainx.permission.base;

/**
 * Created by jimmy on 2018/8/1.
 */

public interface RequestExecutor {
    /**
     * runtime permission task
     */
    int TASK_RUNTIME = 0;

    /**
     * system permission setting task
     */
    int TASK_SETTING = 1;

    /**
     * system permission setting for result check permission again
     */
    int TASK_SETTING_STRICT_CHECK = 2;

    /**
     * overlay permission task
     */
    int TASK_OVERLAY = 3;

    /**
     * install permission task
     */
    int TASK_INSTALL = 4;


    /**
     * Go request permission.
     */
    void execute(int taskId);

    /**
     * Cancel the operation.
     */
    void cancel();
}
