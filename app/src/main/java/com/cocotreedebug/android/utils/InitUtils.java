package com.cocotreedebug.android.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

import com.cocotreedebug.android.manager.BaseApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import dalvik.system.BaseDexClassLoader;

/**
 * 初始化配置信息 用户数据加载及统计信息采集
 */
public class InitUtils {
    private static String TAG = "INIT";
    public static String UA = null;
    public static String IMEI = null;


    public static String deviceId = null;

    public static String getIMEI(Context context) {
        if (deviceId != null) {
            return deviceId;
        }

        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String s = tm.getDeviceId();
            //s="";//for test
            if (s == null) {
                return deviceId = createMUUID();
            } else {
                String ret = s.trim();

                if (ret.equals("")) {
                    return deviceId = createMUUID();
                }

                if (ret.equals("000000000000000")) {
                    return deviceId = createMUUID();
                }

                return deviceId = ret;
            }
        } catch (java.lang.SecurityException e) {
            return deviceId = createMUUID();
        }
    }

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static String getSdCardPath(Context context) {
        File path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory();
        } else {
            path = context.getCacheDir();
        }
        File newPath = new File(path, "crazy_Teacher");
        if (!newPath.exists()) {
            newPath.mkdir();
        }
        return newPath.toString();
    }


    /**
     * 判断字符串是否为全是数字
     *
     * @param phone
     * @return
     */
    public static boolean isNumeric(String phone) {
        return phone.matches("\\d*");
    }


    /**
     * 获取SIM状态
     *
     * @param tm
     * @return
     */
    public static String getSimState(TelephonyManager tm) {
        int simState = tm.getSimState();

        String state = null;
        if (simState == TelephonyManager.SIM_STATE_READY) {
            state = "良好";
        } else if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            state = "无SIM卡";
        } else if (simState == TelephonyManager.SIM_STATE_NETWORK_LOCKED) {
            state = "需要NetWork PIN 解锁";
        } else if (simState == TelephonyManager.SIM_STATE_PIN_REQUIRED) {
            state = "需要SIM卡的PIN解锁";
        } else if (simState == TelephonyManager.SIM_STATE_PUK_REQUIRED) {
            state = "需要SIM卡的PUK解锁";
        } else if (simState == TelephonyManager.SIM_STATE_UNKNOWN) {
            state = "SIM卡状态未知";
        }

        // state = "{simstate:"+state+"}";
        return state;
    }

    /**
     * 匹配网络类型
     *
     * @param tm
     * @return
     */
    public static String getNetworkType(TelephonyManager tm) {
        int networkType = tm.getNetworkType();
        String type = null;
        if (networkType == TelephonyManager.NETWORK_TYPE_GPRS) {
            type = "NETWORK_TYPE_GPRS";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
            type = "NETWORK_TYPE_EDGE";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS) {
            type = "NETWORK_TYPE_UMTS";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
            type = "NETWORK_TYPE_HSDPA";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSUPA) {
            type = "NETWORK_TYPE_HSUPA";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_HSPA) {
            type = "NETWORK_TYPE_HSPA";
        } else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
            type = "NETWORK_TYPE_CDMA";
        } else {
            type = "EVDO | 1xRTT";
        }

        return type;
    }

    public static String getApplicationName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取手机信号类型
     *
     * @param tm
     * @return
     */
    public static String getPhoneType(TelephonyManager tm) {
        int phoneType = tm.getPhoneType();
        String type = null;

        if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
            type = "PHONE_TYPE_GSM";
        } else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
            type = "PHONE_TYPE_CDMA";
        } else {
            type = "PHONE_TYPE_NONE";// 无信号
        }

        return type;
    }

    /**
     * 生成唯一标识
     *
     * @return
     */
    public static String createMUUID() {
//		UUID uuid = UUID.randomUUID();
//		LogUtils.i( "Random UUID String " + uuid.toString());
//		LogUtils.i( "UUID version  = " + uuid.version());
//		LogUtils.i( "UUID variant       = " + uuid.variant());
//		return uuid.toString();
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/crazyteacher/logs", "uuid.dat");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        //没有文件 先创建并写入uuid
        if (!file.exists()) {
            try {
                file.createNewFile();
                BufferedWriter out = null;
                try {
                    out = new BufferedWriter(new OutputStreamWriter(
                            new FileOutputStream(file, false)));
                    UUID uuid = UUID.randomUUID();
                    out.write(uuid.toString());
                } catch (Exception e) {
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                            out = null;
                        }
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e) {
            }
        }

        // 从文件读取
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            try {
                line = br.readLine();
                if (line == null) {
                    UUID uuid = UUID.randomUUID();
                    return uuid.toString();
                } else {
                    return line.trim();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    public static void onCall(Context ctx, String phonenum) {
        Uri uri = Uri.parse("tel:" + phonenum);
        Intent call = new Intent(Intent.ACTION_DIAL, uri);
        ctx.startActivity(call);
    }

    public static String getPhotoPath() {
        File dir = new File(Environment.getExternalStorageDirectory(),
                "/sdp_mpos/photo");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static void deletePhoto(String path, String name) {
        File file = new File(path, name);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    /**
     * 获取CPU核心数
     *
     * @return
     */
    public static int getCPUNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            // Default to return 1 core
            return 1;
        }
    }

    public static String getPhoneBrand() {
        return Build.BRAND + " " + Build.MODEL;
    }

    public static int getCurrentOsVersionCode() {
        /*
         *
         * Build.VERSION_CODES 1 (0x00000001) Android 1.0 BASE 2 (0x00000002)
         * Android 1.1 BASE_1_1 3 (0x00000003) Android 1.5 CUPCAKE 4
         * (0x00000004) Android 1.6 DONUT 5 (0x00000005) Android 2.0 ECLAIR 6
         * (0x00000006) Android 2.0.1 ECLAIR_0_1 7 (0x00000007) Android 2.1
         * ECLAIR_MR1 8 (0x00000008) Android 2.2 FROYO 9 (0x00000009) Android
         * 2.3 GINGERBREAD 10 (0x0000000a) Android 2.3.3 GINGERBREAD_MR1 11
         * (0x0000000b) Android 3.0 HONEYCOMB 12 (0x0000000c) Android 3.1
         * HONEYCOMB_MR1 13 (0x0000000d) Android 3.2 HONEYCOMB_MR2
         */
        /* 获取当前系统的android版本号 */
        return android.os.Build.VERSION.SDK_INT;
    }

    public static void setDialogTypeToast(Window window) {
        if (InitUtils.getCurrentOsVersionCode() >= Build.VERSION_CODES.KITKAT) {
            window.setType(WindowManager.LayoutParams.TYPE_TOAST);
        }/*else{
            window.setType(WindowManager.LayoutParams.TYPE_PHONE);
        }*/
    }

    public static boolean isActivityRunning(Context context, Class cls) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        for (int i = 0; i < list.size(); i++) {
            ComponentName cn = list.get(i).baseActivity;
            if (cls.toString().contains(cn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTopRunning(Context context, Class cls) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        for (int i = 0; i < list.size(); i++) {
            ComponentName cn = list.get(i).topActivity;
            if (cls.toString().contains(cn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得当前的版本
     *
     * @param context
     * @return
     */
    public static int getVersion(Context context) {
        int version = 0;
        try {
            String name = context.getPackageName();
            String verName = context.getPackageManager()
                    .getPackageInfo(name, 0).versionName;
            String v = "0";
            if (!StringUtils.isEmpty(verName)) {
                v = verName.replace(".", "");
            }
            version = Integer.parseInt(v);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获得版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            String name = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(name, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }


    public static String getVersionWithDot(Context context) {
        String name = context.getPackageName();
        String verName = null;
        try {
            verName = context.getPackageManager()
                    .getPackageInfo(name, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
            /*if (!StringUtil.isEmpty(verName)) {
                v = verName.replace(".", "");
            }*/

        return verName;
    }

    /**
     * 求俩个数的最大公约数
     *
     * @param a
     * @param b
     * @return
     */
    public static long greatestCommon(long a, long b) {
        if (a < b) {
            long temp;
            temp = a;
            a = b;
            b = temp;
        }
        if (0 == b) {
            return a;
        }
        return greatestCommon(b, a % b);
    }

    /**
     * 获得当前的版本
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            String name = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(name, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static String getChannel(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString("ANDROID_CHANNEL");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).toString();
        } else {
            return null;
        }
    }


    public static String getAppDexPathList() {
        BaseDexClassLoader aLoder = (BaseDexClassLoader) BaseApplication.getApplication().getClassLoader();

        try {
            Class loderCls = Class.forName("dalvik.system.BaseDexClassLoader");
            Field f = loderCls.getDeclaredField("pathList");
            f.setAccessible(true);
            Object list = f.get(aLoder);
            return list.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
