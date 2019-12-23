package com.sm.android.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.sm.android.manager.BaseApplication;

public class UIUtils {

    public static Context getContext() {
        return BaseApplication.getApplication();
    }

    /**
     * 获取手机屏幕宽度
     */
    public static int getScreenWidth() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕高度
     */
    public static Integer getScreenHeight() {
        return BaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * dip转换px
     */
    public static int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spVal) {
        return sp2px(getContext(), spVal);
    }

    /**
     * sp转px
     */
    @Deprecated
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     */
    public static float px2sp(float pxVal) {
        return px2sp(getContext(), pxVal);
    }

    /**
     * px转sp
     */
    @Deprecated
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }

    /**
     * 显示Toast(可在子线程调用,并且已处理了重复显示的问题)
     */
    public static void showToast(String text) {
        ToastUtils.showToast(text);
    }

    public static void showToast(int textResId) {
        showToast(getContext().getString(textResId));
    }

    public static void showToastShort(String text) {
        ToastUtils.showToastShort(text);
    }

    public static void showToastShort(int textResId) {
        showToastShort(getContext().getString(textResId));
    }

    public static void showToastLong(String text) {
        ToastUtils.showToastLong(text);
    }

    public static void showToastLong(int textResId) {
        showToastLong(getContext().getString(textResId));
    }

}


