package com.cocotree.android.utils;

import android.os.Build;

/**
 * Miui工具类
 *
 * @author evanyu
 * @date 17/11/24
 */
public class MiuiUtils {

    private static final String MANUFACTURER_MIUI = "Xiaomi";

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public interface VersionName {
        String V8 = "V8";
        String V9 = "V9";
    }

    /**
     * 检查当前设备系统是否是MIUI系统
     */
    public static boolean isMiui() {
        return MANUFACTURER_MIUI.equals(Build.MANUFACTURER);
    }

    /**
     * MIUI系统版本编号
     */
    public static String getMiuiVersionCode() {
        return BuildPropUtils.getProperty(KEY_MIUI_VERSION_CODE);
    }

    /**
     * MIUI系统版本名称，例如：V9（代表 MIUI V9系统）
     */
    public static String getMiuiVersionName() {
        return BuildPropUtils.getProperty(KEY_MIUI_VERSION_NAME);
    }

    /**
     * MIUI系统内部存储根目录路径
     */
    public static String getMiuiInternalStorage() {
        return BuildPropUtils.getProperty(KEY_MIUI_INTERNAL_STORAGE);
    }

}
