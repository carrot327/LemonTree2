package com.lemontree.android.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.util.List;

/**
 * AppInfoUtils
 *
 * @author evanyu
 * @date 17/1/9
 */
public class AppInfoUtils {

    /**
     * or you may just hardcode them in your app
     */
    private static String sProcessName = null;

    /**
     * 版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionName != null) {
                return packageInfo.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 版本号
     */
    public static String getVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return String.valueOf(packageInfo.versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 移动网络运营商的名称(SPN)
     */
    public static String getNetworkOperatorName(Context context) {
        try {
            String networkOperator;
            networkOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
            if (!TextUtils.isEmpty(networkOperator)) {
                return networkOperator;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获取服务提供者的名称(SPN)
     */
    public static String getSimOperator(Context context) {
        try {
            String simOperator;
            simOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperatorName();
//            simOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
            if (simOperator != null) {
                return simOperator;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 网络运营商
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        try {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info == null) {
                return "unknown";
            }
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "wifi";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                // 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EDGE，电信的2G为CDMA，电信的3G为EVDO
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_IDEN:   // 25 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:   // 电信2G, 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:   // 移动/联通2G, 100 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:   // 移动/联通2.5G, 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_1xRTT:  // CDMA2000-1X, 50-100 kbps(号称3G,然而速度并没有达到3G标准)
                        return "2G";
                    case TelephonyManager.NETWORK_TYPE_EVDO_0: // 电信3G, 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3G, 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // 电信3G, 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  // 电信3G,EVDO的升级, 1-2 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:   // 联通3G, 400-7000 kbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:   // 联通3G,700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:  // 联通3G, 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:  // 联通3.5G, 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  // 联通3.5G, 10-20 Mbps
                        return "3G";
                    case TelephonyManager.NETWORK_TYPE_LTE:    // 4G, 10+ Mbps
                        return "4G";
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        String subTypeName = info.getSubtypeName();
                        if (!TextUtils.isEmpty(subTypeName)) {
                            // 移动、联通、电信 三种3G制式
                            if (subTypeName.equalsIgnoreCase("TD-SCDMA") || subTypeName.equalsIgnoreCase("WCDMA") || subTypeName.equalsIgnoreCase("CDMA2000")) {
                                return "3G";
                            } else {
                                return subTypeName;
                            }
                        } else {
                            return "unknown";
                        }
                }
            } else {
                String typeName = info.getTypeName();
                if (!TextUtils.isEmpty(typeName)) {
                    return typeName;
                } else {
                    return "unknown";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    /**
     * 获取屏幕的像素宽高
     */
    public static int[] getDisplayInfo(Context context) {
        try {
            DisplayMetrics metric = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
            int[] display = new int[2];
            display[0] = metric.widthPixels;     // 屏幕宽度（像素）
            display[1] = metric.heightPixels;   // 屏幕高度（像素）
            // display[2] = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
            // display[3] = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240
            return display;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[2];
    }

    /**
     * 获取屏幕宽度（单位：像素）
     */
    public static int getScreenWidth(Context context) {
        return getDisplayInfo(context)[0];
    }

    /**
     * 获取屏幕高度（单位：像素）
     */
    public static int getScreenHeight(Context context) {
        return getDisplayInfo(context)[1];
    }

    /**
     * 获取手机经纬度
     */
    public static double[] getLocationInfo(Context context) {
        try {
            double latitude = 0.0;
            double longitude = 0.0;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
            double[] location = new double[2];
            location[0] = latitude;
            location[1] = longitude;
            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[2];
    }

    public static boolean isNullOrNil(final String object) {
        if ((object == null) || (object.length() <= 0)) {
            return true;
        }
        return false;
    }

    public static boolean isMainProcess(Context context) {
        String mainProcessName = null;
        if (context != null) {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo != null) {
                mainProcessName = applicationInfo.processName;
            }
        }
        if (isNullOrNil(mainProcessName)) {
            mainProcessName = context.getPackageName();
        }
        if (isNullOrNil(mainProcessName)) {
            mainProcessName = "";
        }
        String processName = getProcessName(context);
        if (isNullOrNil(processName)) {
            processName = "";
        }
        return mainProcessName.equals(processName);
    }

    /**
     * add process name cache
     *
     * @param context
     * @return
     */
    public static String getProcessName(final Context context) {
        if (sProcessName != null) {
            return sProcessName;
        }
        //will not null
        sProcessName = getProcessNameInternal(context);
        return sProcessName;
    }

    private static String getProcessNameInternal(final Context context) {
        int myPid = android.os.Process.myPid();

        if (context == null || myPid <= 0) {
            return "";
        }

        ActivityManager.RunningAppProcessInfo myProcess = null;
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
                    .getRunningAppProcesses();

            if (appProcessList != null) {
                try {
                    for (ActivityManager.RunningAppProcessInfo process : appProcessList) {
                        if (process.pid == myPid) {
                            myProcess = process;
                            break;
                        }
                    }
                } catch (Exception e) {
                    Log.e("###", "getProcessNameInternal exception:" + e.getMessage());
                }

                if (myProcess != null) {
                    return myProcess.processName;
                }
            }
        }

        byte[] b = new byte[128];
        FileInputStream in = null;
        try {
            in = new FileInputStream("/proc/" + myPid + "/cmdline");
            int len = in.read(b);
            if (len > 0) {
                for (int i = 0; i < len; i++) { // lots of '0' in tail , remove them
                    if (b[i] > 128 || b[i] <= 0) {
                        len = i;
                        break;
                    }
                }
                return new String(b, 0, len);
            }

        } catch (Exception e) {
            Log.e("###", "getProcessNameInternal exception:" + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }

        return "";
    }

    /**
     * 检测是否安装过对应app
     *
     * @param mContext
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context mContext, String packageName) {
        final PackageManager packageManager = mContext.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
