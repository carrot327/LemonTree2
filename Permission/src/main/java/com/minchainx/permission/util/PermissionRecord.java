package com.minchainx.permission.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jimmy on 2018/8/8.
 */

public class PermissionRecord {

    public static final int STATE_INIT = 0;
    public static final int STATE_RATIONAL = 1;
    public static final int STATE_FORBID = 2;

    public static final String FILE_NAME = "permission";

    public static void recordState(Context context, String permission, int state) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(permission, state).apply();
    }

    public static void deleteState(Context context, String permission) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(permission).apply();
    }

    public static int getState(Context context, String permission) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(permission, STATE_INIT);
    }


}
