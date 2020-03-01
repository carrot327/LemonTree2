package com.liveness.dflivenesslibrary.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DFViewShowUtils {

    public static final String DF_BOOLEAN_TRUE_STRING = "1";
    public static final String DF_BOOLEAN_FALSE_STRING = "0";

    public static String booleanTrans(boolean value) {
        return value ? DF_BOOLEAN_TRUE_STRING : DF_BOOLEAN_FALSE_STRING;
    }

    public static void refreshVisibility(View view, boolean show) {
        if (view != null) {
            view.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public static void refreshText(TextView textView, String text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    public static String getText(EditText editText) {
        return editText == null ? "" : editText.getText().toString();
    }

    public static int dp2px(Context context, float dpValue) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        return (int) (dpValue * (densityDpi / 160));
    }
}
