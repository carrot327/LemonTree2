package com.kantong.android.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;

import com.kantong.android.utils.text.TextColorSpan;
import com.kantong.android.utils.text.TextSizeSpan;
import com.kantong.android.utils.text.TextSpanUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Random;

/**
 * 字符串工具类
 *
 * @author evanyu
 * @date 16/11/16
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence text) {
        String textStr = null;
        if (text != null) {
            textStr = text.toString().trim();
        }
        return TextUtils.isEmpty(textStr) || "null".equalsIgnoreCase(textStr);
    }

    public static int toInt(String str, int dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static byte toByte(String str, byte dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Byte.parseByte(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static short toShort(String str, short dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Short.parseShort(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static long toLong(String str, long dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static float toFloat(String str, float dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static double toDouble(String str, double dft) {
        if (isEmpty(str)) {
            return dft;
        }

        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException ex) {
            return dft;
        }
    }

    public static String toString(CharSequence text) {
        if (text != null) {
            return text.toString();
        } else {
            return "";
        }
    }

    /**
     * 获取字符串的最后四个字符
     */
    public static String getLastFourChars(CharSequence str) {
        if (!TextUtils.isEmpty(str) && str.length() > 4) {
            return str.toString().substring(str.length() - 4);
        } else {
            return "";
        }
    }

    /**
     * 格式化银行限额提示中的金额内容
     */
    public static String formatMoneyInBankLimit(String moneyStr) {
        if (StringUtils.isEmpty(moneyStr) || "无限额".equals(moneyStr)) {
            return "无限额";
        }
        try {
            int money = Integer.parseInt(moneyStr);
            if (money > 0) {
                int wan = money / 10000;
                if (wan > 0 && money % 10000 == 0) {
                    return wan + "万";
                } else {
                    return money + "元";
                }
            }
        } catch (NumberFormatException e) {
            // ignore
        }
        return "--";
    }

    /**
     * 修改指定字符的大小
     *
     * @param srcText  原始字符串
     * @param subText  需要修改文字大小的部分字符
     * @param textSize 文字大小
     * @return 修改完成后的字符串
     */
    public static Spannable modifyTextSize(CharSequence srcText, String subText, int textSize) {
        return TextSpanUtils.setTextSpan(srcText, subText, new TextSizeSpan(textSize));
    }

    /**
     * 修改指定字符的大小
     *
     * @param text     原始字符串
     * @param textSize 文字大小
     * @param strArr   需要修改文字大小的部分字符（1～多个）
     * @return 修改完成后的字符串
     */
    public static Spannable modifyTextSize(CharSequence text, int textSize, String... strArr) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        Spannable span = new SpannableString(text);
        for (String replaceStr : strArr) {
            span = modifyTextSize(span, replaceStr, textSize);
        }
        return span;
    }

    /**
     * 修改指定字符的大小
     *
     * @param srcText   原始字符串
     * @param subText   需要修改颜色的部分字符
     * @param textColor 文本颜色
     * @return 修改完成后的字符串
     */
    public static Spannable modifyTextColor(CharSequence srcText, String subText, int textColor) {
        return TextSpanUtils.setTextSpan(srcText, subText, new TextColorSpan(textColor));
    }

    /**
     * 修改指定字符的颜色
     *
     * @param text      原始字符串
     * @param textColor 文本颜色
     * @param strArr    需要修改颜色的部分字符（1～多个）
     * @return 修改完成后的字符串
     */
    public static Spannable modifyTextColor(CharSequence text, int textColor, String... strArr) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        Spannable span = new SpannableString(text);
        for (String replaceStr : strArr) {
            span = modifyTextColor(span, replaceStr, textColor);
        }
        return span;
    }

    /**
     * {@link StringUtils#formatRateTextStyle(CharSequence, int, int)}
     */
    public static Spannable formatRateTextStyle(CharSequence rateText, int opTextSize) {
        return formatRateTextStyle(rateText, opTextSize, opTextSize);
    }

    /**
     * 格式化利率样式，自动判断格式，支持以下格式：
     * 规则：
     * 1) xx%或xx%以上 : 利率数字不变，其他字符变为opTextSize
     * 2) xx%+xx% : 第一个利率数字不变，第二个利率数字和"＋"号变为numTextSize，操作符变为opTextSize
     * 3) xx%~xx% 或 xx%-xx% : 第一个利率数字和第二个利率数字大小不变，其他字符变为opTextSize
     *
     * @param rateText    利率文本内容
     * @param numTextSize 较小利率的数字部分字体大小（单位:dp）
     * @param opTextSize  操作符字体大小（单位:dp）
     */
    public static Spannable formatRateTextStyle(CharSequence rateText, int numTextSize, int opTextSize) {
        if (TextUtils.isEmpty(rateText) || rateText.length() < 1) {
            return null;
        }
        // 将字体单位转换为px
        numTextSize = UIUtils.dip2px(numTextSize);
        opTextSize = UIUtils.dip2px(opTextSize);

        String rateStr = rateText.toString();
        Spannable result = new SpannableString(rateStr);
        // 判断百分号的数量
        int percentCount = getCharCount(rateStr, '%');
        if (percentCount == 1) {
            String subStr = rateStr.substring(rateStr.indexOf("%"));
            result = modifyTextSize(rateStr, subStr, opTextSize);
        } else if (percentCount == 2) {
            if (rateStr.contains("%+") || rateStr.contains("% +")) {
                // xx%+xx%
                // "+"号和第二个数字变小
                String subStr = rateStr.substring(rateStr.lastIndexOf("+"), rateStr.lastIndexOf("%"));
                result = modifyTextSize(rateStr, subStr, numTextSize);
                // 其余操作符变小
                result = modifyTextSize(result, opTextSize, "%");
            } else if (rateStr.contains("%-") || rateStr.contains("% -")
                    || rateStr.contains("%~") || rateStr.contains("% ~")) {
                // xx%~xx% 或 xx%-xx%
                result = modifyTextSize(rateStr, opTextSize, " ", "-", "%", "~");
            }
        }
        return result;
    }

    public static int getCharCount(String text, char c) {
        if (TextUtils.isEmpty(text)) {
            return -1;
        }
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (c == text.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 生成随机字符串
     *
     * @param length 生成字符串的长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 将用户姓名的姓改为*
     *
     * @param userName
     * @return
     */
    public static String changeUserName(String userName) {
        if (!TextUtils.isEmpty(userName) && userName.length() > 1) {
            String name = userName.substring(1, userName.length());
            String surname = "*";
            return surname + name;
        }
        return "";
    }

    /**
     * 剩余可投金额 ≥ 5万	不显示
     * 5万 ＞ 剩余可投金额 ≥ 1000
     * 格式：剩余xx万元
     * 数字精确到小数点后2位，如：剩余0.1万
     * 剩余可投金额 ＜ 1000
     * 格式：剩余xx元
     * 数字为整数，如：剩余152元
     *
     * @param price
     * @return
     */
    public static String formatPrice(BigDecimal price) {
        if (price.compareTo(new BigDecimal(50000)) >= 0) { // >= 5万
            return null;
        }
        if (price.compareTo(new BigDecimal(50000)) < 0 && price.compareTo(new BigDecimal(1000)) >= 0) {
            // 小于5万 大于等于1000 数字精确到小数点后1位，如：剩余0.1万
            return price.divide(new BigDecimal(10000), 1, BigDecimal.ROUND_DOWN).toString() + "万";
        }
        if (price.compareTo(new BigDecimal(1000)) < 0) {
            // 小于1000 数字为整数，如：剩余152元
            return Math.floor(price.doubleValue()) + "元";
        }
        return null;
    }

    public static String format(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    /**
     * String 转 UTF-8
     *
     * @param str
     * @return
     */
    public static String toUTF8(String str) {
        if (!TextUtils.isEmpty(str)) {
            String result = null;
            try {
                result = URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return "";
        }
    }
}
