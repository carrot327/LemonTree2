package com.lemontree.android.utils;

import android.text.TextUtils;

import java.text.NumberFormat;

/**
 * 货币格式转换工具类
 * Created by fei on 16/6/1.
 */
public class CurrencyFormatUtils {

    private static NumberFormat mFormat;

    private CurrencyFormatUtils() {
    }

    private static NumberFormat getInstance() {
        if (mFormat == null) {
            return mFormat = NumberFormat.getInstance();
        } else {
            return mFormat;
        }
    }

    /**
     * 格式化保留2位数<br/>
     * 12,345,678.901 => 12,345,678.90<br/>
     * 12,345,678.909 => 12,345,678.91
     */
    public static String formatTwoDecimal(String data) {
        if (TextUtils.isEmpty(data)) {
            return "0.00";
        }
        Double d;
        try {
            d = Double.parseDouble(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
        return formatTwoDecimal(d);
    }

    /**
     * 格式化保留2位数<br/>
     * 12,345,678.901 => 12,345,678.90<br/>
     * 12,345,678.909 => 12,345,678.91
     */
    public static String formatTwoDecimal(Double data) {
        getInstance();
        mFormat.setMinimumFractionDigits(2);
        mFormat.setMaximumFractionDigits(2);
        return mFormat.format(data);
    }

    /**
     * 格式化取整<br/>
     * 12,345,678.901 => 12,345,679<br/>
     */
    public static String formatDecimal(String data) {
        if (TextUtils.isEmpty(data)) {
            return "0";
        }
        Double d;
        try {
            d = Double.parseDouble(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return formatDecimal(d);
    }

    /**
     * 格式化取整<br/>
     * 12,345,678.901 => 12,345,679<br/>
     */
    public static String formatDecimal(Double data) {
        getInstance();
        mFormat.setMinimumFractionDigits(0);
        mFormat.setMaximumFractionDigits(0);
        return mFormat.format(data);
    }

    public static String formatTwoDecimalNoCurrency(Double data) {
        getInstance();
        mFormat.setGroupingUsed(false);
        mFormat.setMinimumFractionDigits(2);
        mFormat.setMaximumFractionDigits(2);
        return mFormat.format(data);
    }

}
