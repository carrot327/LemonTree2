package com.cocotreedebug.android.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * 精准数值运算工具类（处理精准数值的各种运算）
 * Created by evanyu on 16/7/29.
 */
public class NumUtils {

    private static final int DEF_DIV_SCALE = 10; // 默认除法运算后小数点后的精度


    /*************************************** 四舍五入 ***************************************/
    /**
     * 四舍五入到小数点后第n位
     *
     * @param num 参数操作的数值
     * @param n   精确到的小数点后的位数
     * @return 四舍五入后的结果
     */
    public static String round(double num, int n) {
        return round(Double.toString(num), n);
    }

    /**
     * 四舍五入到小数点后第n位
     *
     * @param num 参数操作的数值
     * @param n   精确到的小数点后的位数
     * @return 四舍五入后的结果
     */
    public static String round(String num, int n) {
        if (TextUtils.isEmpty(num)) {
            return "0";
        }
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(num);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return round(bigDecimal, n);
    }

    /**
     * 四舍五入到小数点后第n位
     *
     * @param num 参数操作的数值
     * @param n   精确到的小数点后的位数
     * @return 四舍五入后的结果
     */
    public static String round(BigDecimal num, int n) {
        return num.setScale(n, BigDecimal.ROUND_HALF_UP).toString();
    }

    /*************************************** 保留指定位数(不进位) ***************************************/

    /**
     * @param num 参数操作的数值
     * @param n   小数点后保留的位数
     * @return
     */
    public static String roundDown(double num, int n) {
        return round(Double.toString(num), n);
    }

    /**
     * @param num 参数操作的数值
     * @param n   小数点后保留的位数
     * @return
     */
    public static String roundDown(String num, int n) {
        if (TextUtils.isEmpty(num)) {
            return "0";
        }
        return roundDown(new BigDecimal(num), n);
    }

    /**
     * @param num 参数操作的数值
     * @param n   小数点后保留的位数
     * @return
     */
    public static String roundDown(BigDecimal num, int n) {
        return num.setScale(n, BigDecimal.ROUND_DOWN).toString();
    }

    /*************************************** 加法运算 ***************************************/
    /**
     * 精准的加法运算
     *
     * @param num1 加数
     * @param num2 被加数
     * @return 两数之和
     */
    public static BigDecimal add(double num1, double num2) {
        return add(Double.toString(num1), Double.toString(num2));
    }

    /**
     * 精准的加法运算
     *
     * @param num1 加数
     * @param num2 被加数
     * @return 两数之和
     */
    public static BigDecimal add(String num1, String num2) {
        BigDecimal b1;
        if (TextUtils.isEmpty(num1)) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(num1);
        }
        BigDecimal b2;
        if (TextUtils.isEmpty(num2)) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(num2);
        }
        return add(b1, b2);
    }

    /**
     * 精准的加法运算
     *
     * @param num1 被加数
     * @param num2 加数
     * @return 两数之和
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        return num1.add(num2);
    }

    /*************************************** 减法运算 ***************************************/
    /**
     * 精准的减法运算
     *
     * @param num1 被减数
     * @param num2 减数
     * @return 两数之差
     */
    public static BigDecimal substract(double num1, double num2) {
        return substract(Double.toString(num1), Double.toString(num2));
    }

    /**
     * 精准的减法运算
     *
     * @param num1 被减数
     * @param num2 减数
     * @return 两数之差
     */
    public static BigDecimal substract(String num1, String num2) {
        BigDecimal b1;
        if (TextUtils.isEmpty(num1)) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(num1);
        }
        BigDecimal b2;
        if (TextUtils.isEmpty(num2)) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(num2);
        }
        return substract(b1, b2);
    }

    /**
     * 精准的减法运算
     *
     * @param num1 被减数
     * @param num2 减数
     * @return 两数之差
     */
    public static BigDecimal substract(BigDecimal num1, BigDecimal num2) {
        return num1.subtract(num2);
    }

    /*************************************** 乘法运算 ***************************************/
    /**
     * 精准的乘法运算
     *
     * @param num1 被乘数
     * @param num2 乘数
     * @return 两数之积
     */
    public static BigDecimal multiply(double num1, double num2) {
        return multiply(Double.toString(num1), Double.toString(num2));
    }

    /**
     * 精准的乘法运算
     *
     * @param num1 被乘数
     * @param num2 乘数
     * @return 两数之积
     */
    public static BigDecimal multiply(String num1, String num2) {
        BigDecimal b1;
        if (TextUtils.isEmpty(num1)) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(num1);
        }
        BigDecimal b2;
        if (TextUtils.isEmpty(num2)) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(num2);
        }
        return multiply(b1, b2);
    }

    /**
     * 精准的乘法运算
     *
     * @param num1 被乘数
     * @param num2 乘数
     * @return 两数之积
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
        return num1.multiply(num2);
    }

    /*************************************** 除法运算 ***************************************/
    /**
     * 精准的除法运算
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 两数之商
     */
    public static BigDecimal divide(double num1, double num2) {
        return divide(num1, num2, DEF_DIV_SCALE);
    }

    /**
     * 精准的除法运算
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 表示需要精确到小数点后第几位
     * @return 两数之商
     */
    public static BigDecimal divide(double num1, double num2, int scale) {
        return divide(Double.toString(num1), Double.toString(num2), scale);
    }

    /**
     * 精准的除法运算
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 两数之商
     */
    public static BigDecimal divide(String num1, String num2) {
        return divide(num1, num2, DEF_DIV_SCALE);
    }

    /**
     * 精准的除法运算
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 表示需要精确到小数点后第几位
     * @return 两数之商
     */
    public static BigDecimal divide(String num1, String num2, int scale) {
        BigDecimal b1;
        if (TextUtils.isEmpty(num1)) {
            b1 = new BigDecimal(0);
        } else {
            b1 = new BigDecimal(num1);
        }
        BigDecimal b2;
        if (TextUtils.isEmpty(num2)) {
            b2 = new BigDecimal(0);
        } else {
            b2 = new BigDecimal(num2);
        }
        return divide(b1, b2, scale);
    }

    /**
     * 精准的除法运算
     *
     * @param num1 被除数
     * @param num2 除数
     * @return 两数之商
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2) {
        return divide(num1, num2, DEF_DIV_SCALE);
    }

    /**
     * 精准的除法运算
     *
     * @param num1  被除数
     * @param num2  除数
     * @param scale 表示需要精确到小数点后第几位
     * @return 两数之商
     */
    public static BigDecimal divide(BigDecimal num1, BigDecimal num2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        return num1.divide(num2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 去除小数点后多余的零 e.g. 100.0 -> 100 ; 100.01 -> 100.01
     */
    public static String stripTrailingZeros(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        try {
            num = new BigDecimal(num).stripTrailingZeros().toString();
        } catch (Exception e) {
            e.printStackTrace();
            num = "0";
        }
        return num;
    }

    /**
     * 去除小数点后多余的零 e.g. 100.0 -> 100 ; 100.01 -> 100.01
     */
    public static BigDecimal stripTrailingZeros(BigDecimal num) {
        return new BigDecimal(num.stripTrailingZeros().toPlainString());
    }

    /**
     * String转Double时空指针排除
     *
     * @param num
     * @return
     */
    public static Double parseDouble(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        Double d = 0d;
        try {
            d = Double.parseDouble(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * String转Int时空指针排除
     *
     * @param num
     * @return
     */
    public static Integer parseInt(String num) {
        if (TextUtils.isEmpty(num)) {
            num = "0";
        }
        int i = 0;
        try {
            i = Integer.parseInt(num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
