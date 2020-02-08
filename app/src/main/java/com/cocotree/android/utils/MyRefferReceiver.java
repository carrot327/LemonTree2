package com.cocotree.android.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.cocotree.android.manager.BaseApplication;
import com.cocotree.android.manager.ConstantValue;
import com.cocotree.android.uploadUtil.Tools;
import com.google.android.gms.analytics.CampaignTrackingReceiver;


public class MyRefferReceiver extends CampaignTrackingReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String referrer = intent.getStringExtra("referrer");
        String utmSource = "";
        int first = 0;
        int last = 0;
        if (!TextUtils.isEmpty(referrer)) {
            if (referrer.contains("gp")) {
                first = referrer.indexOf("gp");
            }

            if (referrer.contains("%3D")) {
                if (referrer.contains("%26utm_medium")) {
                    last = referrer.lastIndexOf("%26utm_medium");
                }
            } else {
                if (referrer.contains("&utm_medium")) {
                    last = referrer.lastIndexOf("&utm_medium");
                }
            }
            if (0 < first && last < referrer.length() && (last - first) >= 0) {
                utmSource = referrer.substring(first, last);
            }
        }
        SPUtils.putString(ConstantValue.UTM_SOURCE, utmSource);
//        Log.v("ReferrerDemo", "referrer:"+referrer);
//        Log.v("ReferrerDemo", Tools.getUtmSource());

    }
}
