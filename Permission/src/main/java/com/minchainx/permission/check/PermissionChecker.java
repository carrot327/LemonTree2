package com.minchainx.permission.check;

import android.content.Context;

/**
 * Created by jimmy on 2018/8/1.
 */

public interface PermissionChecker {
    /**
     * Check if the calling context has a set of permissions.
     *
     * @param context    {@link Context}.
     * @param permission check permission.
     * @return true, other wise is false.
     */
    boolean hasPermission(Context context, String permission);
}
