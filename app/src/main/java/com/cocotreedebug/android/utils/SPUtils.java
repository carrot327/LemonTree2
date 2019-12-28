package com.cocotreedebug.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cocotreedebug.android.manager.BaseApplication;
import com.cocotreedebug.android.manager.ConstantValue;

/**
 * SharedPreferences工具类
 * Created by evanyu on 16/7/12.
 * Edited by linqi on 16/9/23
 * Edited by evanyu on 16/11/29 修改方法名
 */
public class SPUtils {

    private static final String DEFAULT_SP_NAME = "config";
    private static final String KEY_SUFFIX = "170913";


    private SPUtils() {
    }

    private static Context getContext() {
        return BaseApplication.getContext();
    }

    public static boolean putString(String key, String value, String spName) {
        return putString(key, value, spName, false);
    }

    public static boolean putInt(String key, int value, String spName) {
        return putInt(key, value, spName, false);
    }

    public static boolean putFloat(String key, float value, String spName) {
        return putFloat(key, value, spName, false);
    }

    public static boolean putLong(String key, long value, String spName) {
        return putLong(key, value, spName, false);
    }

    public static boolean putBoolean(String key, boolean value, String spName) {
        return putBoolean(key, value, spName, false);
    }

    public static String getString(String key, String defValue, String spName) {
        return getString(key, defValue, spName, false);
    }

    public static int getInt(String key, int defValue, String spName) {
        return getInt(key, defValue, spName, false);
    }

    public static float getFloat(String key, float defValue, String spName) {
        return getFloat(key, defValue, spName, false);
    }

    public static long getLong(String key, long defValue, String spName) {
        return getLong(key, defValue, spName, false);
    }

    public static boolean getBoolean(String key, boolean defValue, String spName) {
        return getBoolean(key, defValue, spName, false);
    }

    /*************************************************************************************************/

    public static boolean putString(String key, String value) {
        return putString(key, value, DEFAULT_SP_NAME);
    }

    public static boolean putInt(String key, int value) {
        return putInt(key, value, DEFAULT_SP_NAME);
    }

    public static boolean putFloat(String key, float value) {
        return putFloat(key, value, DEFAULT_SP_NAME);
    }

    public static boolean putLong(String key, long value) {
        return putLong(key, value, DEFAULT_SP_NAME);
    }

    public static boolean putBoolean(String key, boolean value) {
        return putBoolean(key, value, DEFAULT_SP_NAME);
    }

    public static String getString(String key, String defValue) {
        return getString(key, defValue, DEFAULT_SP_NAME);
    }

    public static int getInt(String key, int defValue) {
        return getInt(key, defValue, DEFAULT_SP_NAME);
    }

    public static float getFloat(String key, float defValue) {
        return getFloat(key, defValue, DEFAULT_SP_NAME);
    }

    public static long getLong(String key, long defValue) {
        return getLong(key, defValue, DEFAULT_SP_NAME);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getBoolean(key, defValue, DEFAULT_SP_NAME);
    }

    /*************************************************************************************************/

    public static boolean putString(String key, String value, String spName, boolean encrypt, int type) {
        if (value == null) {
            value = "";
        }
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
//        if (encrypt) {
//            /**
//             * 初始化DesBase64Tool中的sSecretKeyAccount
//             */
//            if (!TextUtils.isEmpty(key) && key.equalsIgnoreCase(ConstantValue.USER_ID)) {
//                SecretKeyUtils.initAccountSecretKey(value);
//            }
//            value = DesBase64Tool.desEncrypt(value, type);
//        }
        return sp.edit().putString(getKey(key), value).commit();
    }

    public static boolean putInt(String key, int value, String spName, boolean encrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().putInt(getKey(key), value).commit();
//        String newValue = value + "";
//        if (encrypt) {
//            newValue = DesBase64Tool.desEncrypt(newValue, type);
//        }
//        return sp.edit().putString(key, newValue).commit();
    }

    public static boolean putFloat(String key, float value, String spName, boolean encrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().putFloat(getKey(key), value).commit();
//        String newValue = value + "";
//        if (encrypt) {
//            newValue = DesBase64Tool.desEncrypt(newValue, type);
//        }
//        return sp.edit().putString(key, newValue).commit();
    }

    public static boolean putLong(String key, long value, String spName, boolean encrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().putLong(getKey(key), value).commit();
//        String newValue = value + "";
//        if (encrypt) {
//            newValue = DesBase64Tool.desEncrypt(newValue, type);
//        }
//        return sp.edit().putString(key, newValue).commit();
    }

    public static boolean putBoolean(String key, boolean value, String spName, boolean encrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().putBoolean(getKey(key), value).commit();
//        String newValue = value + "";
//        if (encrypt) {
//            newValue = DesBase64Tool.desEncrypt(newValue, type);
//        }
//        return sp.edit().putString(key, newValue).commit();
    }

    public static String getString(String key, String defValue, String spName, boolean decrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        String rtn = sp.getString(getKey(key), defValue);
//        if (decrypt) {
//            rtn = DesBase64Tool.desDecrypt(rtn, type);
//        }
        return rtn;
    }

    public static int getInt(String key, int defValue, String spName, boolean decrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getInt(getKey(key), defValue);
//        int rtn = defValue;
//        String value = sp.getString(key, "");
//        if (!TextUtils.isEmpty(value)) {
//            if (decrypt) {
//                value = DesBase64Tool.desDecrypt(value, type);
//            }
//            try {
//                rtn = Integer.parseInt(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtn;
    }

    public static float getFloat(String key, float defValue, String spName, boolean decrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getFloat(getKey(key), defValue);
//        float rtn = defValue;
//        String value = sp.getString(key, "");
//        if (!TextUtils.isEmpty(value)) {
//            if (decrypt) {
//                value = DesBase64Tool.desDecrypt(value, type);
//            }
//            try {
//                rtn = Float.parseFloat(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtn;
    }

    public static long getLong(String key, long defValue, String spName, boolean decrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getLong(getKey(key), defValue);
//        long rtn = defValue;
//        String value = sp.getString(key, "");
//        if (!TextUtils.isEmpty(value)) {
//            if (decrypt) {
//                value = DesBase64Tool.desDecrypt(value, type);
//            }
//            try {
//                rtn = Long.parseLong(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtn;
    }

    public static boolean getBoolean(String key, boolean defValue, String spName, boolean decrypt, int type) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(getKey(key), defValue);
//        boolean rtn = defValue;
//        String value = sp.getString(key, "");
//        if (!TextUtils.isEmpty(value)) {
//            if (decrypt) {
//                value = DesBase64Tool.desDecrypt(value, type);
//            }
//            try {
//                rtn = Boolean.parseBoolean(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rtn;
    }

    /*************************************************************************************************/

    public static boolean putString(String key, String value, String spName, boolean encrypt) {
        return putString(key, value, spName, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putInt(String key, int value, String spName, boolean encrypt) {
        return putInt(key, value, spName, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putFloat(String key, float value, String spName, boolean encrypt) {
        return putFloat(key, value, spName, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putLong(String key, long value, String spName, boolean encrypt) {
        return putLong(key, value, spName, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putBoolean(String key, boolean value, String spName, boolean encrypt) {
        return putBoolean(key, value, spName, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static String getString(String key, String defValue, String spName, boolean decrypt) {
        return getString(key, defValue, spName, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static int getInt(String key, int defValue, String spName, boolean decrypt) {
        return getInt(key, defValue, spName, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static float getFloat(String key, float defValue, String spName, boolean decrypt) {
        return getFloat(key, defValue, spName, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static long getLong(String key, long defValue, String spName, boolean decrypt) {
        return getLong(key, defValue, spName, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean getBoolean(String key, boolean defValue, String spName, boolean decrypt) {
        return getBoolean(key, defValue, spName, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    /*************************************************************************************************/

    public static boolean putString(String key, String value, boolean encrypt) {
        return putString(key, value, DEFAULT_SP_NAME, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putInt(String key, int value, boolean encrypt) {
        return putInt(key, value, DEFAULT_SP_NAME, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putFloat(String key, float value, boolean encrypt) {
        return putFloat(key, value, DEFAULT_SP_NAME, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putLong(String key, long value, boolean encrypt) {
        return putLong(key, value, DEFAULT_SP_NAME, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean putBoolean(String key, boolean value, boolean encrypt) {
        return putBoolean(key, value, DEFAULT_SP_NAME, encrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static String getString(String key, String defValue, boolean decrypt) {
        return getString(key, defValue, DEFAULT_SP_NAME, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static int getInt(String key, int defValue, boolean decrypt) {
        return getInt(key, defValue, DEFAULT_SP_NAME, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static float getFloat(String key, float defValue, boolean decrypt) {
        return getFloat(key, defValue, DEFAULT_SP_NAME, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static long getLong(String key, long defValue, boolean decrypt) {
        return getLong(key, defValue, DEFAULT_SP_NAME, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    public static boolean getBoolean(String key, boolean defValue, boolean decrypt) {
        return getBoolean(key, defValue, DEFAULT_SP_NAME, decrypt, DesBase64Tool.TYPE_DEFAULT);
    }

    /*************************************************************************************************/

    /**
     * 初始化DesBase64Tool中的sSecretKeyAccount
     */
    private static void initAccountSecretKey() {
        if (DesBase64Tool.getSecretKeyAccount() == null) {
            String mid = getString(ConstantValue.USER_ID, "", true);
            if (!TextUtils.isEmpty(mid)) {
                SecretKeyUtils.initAccountSecretKey(mid);
            }
        }
    }

    /**
     * 初始化SharedPreferencesName
     */
    private static void initSharedPreferencesName() {
        if (TextUtils.isEmpty(BaseApplication.mSharedPreferencesName)) {
            BaseApplication.mSharedPreferencesName = getString(ConstantValue.USER_ID, "", true);
        }
    }

    /**
     * 初始化
     */
    public static void initAccountDataParameter() {
        initAccountSecretKey();
        initSharedPreferencesName();
    }

    public static boolean putStringAccountData(String key, String value) {
        initAccountDataParameter();
        return putString(key, value, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static boolean putIntAccountData(String key, int value) {
        initAccountDataParameter();
        return putInt(key, value, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static boolean putFloatAccountData(String key, float value) {
        initAccountDataParameter();
        return putFloat(key, value, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static boolean putLongAccountData(String key, long value) {
        initAccountDataParameter();
        return putLong(key, value, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static boolean putBooleanAccountData(String key, boolean value) {
        initAccountDataParameter();
        return putBoolean(key, value, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static String getStringAccountData(String key, String defValue) {
        initAccountDataParameter();
        return getString(key, defValue, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static int getIntAccountData(String key, int defValue) {
        initAccountDataParameter();
        return getInt(key, defValue, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static float getFloatAccountData(String key, float defValue) {
        initAccountDataParameter();
        return getFloat(key, defValue, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static long getLongAccountData(String key, long defValue) {
        initAccountDataParameter();
        return getLong(key, defValue, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    public static boolean getBooleanAccountData(String key, boolean defValue) {
        initAccountDataParameter();
        return getBoolean(key, defValue, BaseApplication.mSharedPreferencesName, true, DesBase64Tool.TYPE_ACCOUNT);
    }

    /*************************************************************************************************/

    public static boolean remove(String key, String spName) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().remove(getKey(key)).commit();
    }

    public static boolean remove(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        return sp.edit().remove(getKey(key)).commit();
    }

    public static boolean clear(String spName) {
        SharedPreferences sp = getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }

    public static boolean clear() {
        SharedPreferences sp = getContext().getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }

    /** --------------------------------------------------------------------------------------------- **/

    private static String getKey(String oldKey) {
        if (oldKey == null) {
            oldKey = "";
        }
        return oldKey + KEY_SUFFIX;
    }
}
