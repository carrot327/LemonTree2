package com.cocotree.android.utils.text;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * @author evanyu
 * @date 17/12/13
 */
public class TextSpanUtils {

    private TextSpanUtils() {
        // do nothing
    }

    public static Spannable setTextSpan(CharSequence srcText, String replaceText, ITextSpan textSpan) {
        if (TextUtils.isEmpty(srcText) || TextUtils.isEmpty(replaceText)) {
            return null;
        }
        Spannable span = new SpannableString(srcText);
        // 标记textSpan是否已经在下面的循环中使用过(传进来的textSpan只能使用一次)
        boolean useFlag = false;
        for (int start = 0; start < srcText.length(); start += replaceText.length()) {
            start = srcText.toString().indexOf(replaceText, start);
            if (start < 0) {
                break;
            } else {
                if (useFlag) {
                    textSpan = copyTextSpan(textSpan);
                } else {
                    useFlag = true;
                }
                span.setSpan(textSpan, start, start + replaceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }

    private static ITextSpan copyTextSpan(ITextSpan textSpan) {
        ITextSpan result = textSpan;
        try {
            if (textSpan instanceof TextSizeSpan) {
                result = ((TextSizeSpan) textSpan).clone();
            } else if (textSpan instanceof TextColorSpan) {
                result = ((TextColorSpan) textSpan).clone();
            } else if (textSpan instanceof TextStyleSpan) {
                result = ((TextStyleSpan) textSpan).clone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
