package com.lemontree.android.uploadUtil;

import com.lemontree.android.BuildConfig;
import com.lemontree.android.manager.BaseApplication;
import com.lemontree.android.manager.ConstantValue;
import com.lemontree.android.setting.EvnConfig;
import com.lemontree.android.utils.StringUtils;

public class UrlHostConfig {
    //控制在debug模式下是否进入release域名   HTTP_TEST：用测试域名   HTTP_RELEASE:用生产域名
    private static EvnConfig HOST_ENV = EvnConfig.HTTP_TEST;

    //测试
    private static final String DEBUG_MAIN_HOST = "http://161.117.179.26:80";
    private static final String DEBUG_H5_HOST = "http://161.117.179.26:83";
    private static final String DEBUG_FLOW_IO_HOST = "";
    private static final String DEBUG_UPDATE_HOST = DEBUG_MAIN_HOST;

    //生产
    private static final String RELEASE_MAIN_HOST = "http://47.74.255.192";
    private static final String RELEASE_H5_HOST = "http://161.117.226.202";
    private static final String RELEASE_FLOW_IO_HOST = "";
    private static final String RELEASE_UPDATE_HOST = RELEASE_MAIN_HOST;

    /**
     * 后台接口域名
     */
    public static String getBackgroundHost() {
        if (EvnConfig.HTTP_TEST == HOST_ENV && BuildConfig.DEBUG) {
            return DEBUG_MAIN_HOST;
        } else {
            return RELEASE_MAIN_HOST;
        }
    }

    /**
     * H5 主域名
     */
    public static String getH5BaseHost() {
        if (EvnConfig.HTTP_TEST == HOST_ENV && BuildConfig.DEBUG) {
            return DEBUG_H5_HOST;
        } else {
            return RELEASE_H5_HOST;
        }
    }

    /**
     * 导流平台的域名
     */
    public static String getFlowIOHost() {
        if (EvnConfig.HTTP_TEST == HOST_ENV && BuildConfig.DEBUG) {
            return DEBUG_FLOW_IO_HOST;
        } else {
            return RELEASE_FLOW_IO_HOST;
        }
    }

    /**
     * 强更域名
     */
    public static String getUpdateHost() {
        if (EvnConfig.HTTP_TEST == HOST_ENV && BuildConfig.DEBUG) {
            return DEBUG_UPDATE_HOST;
        } else {
            return RELEASE_UPDATE_HOST;
        }
    }

    /**
     * OCR信息认证
     */
    public static String getOCRAuthenticateUrl() {

        return getBackgroundHost() + "/ocr/auth/ocrInfo";
    }

    /**
     * 活体信息上传
     */
    public static String uploadLivenessInfo() {

        return getBackgroundHost() + "/ocr/auth/faceInfo";
    }

    /**
     * 人脸识别加密数据
     */
    public static String uploadLivenessEncryptionData() {

        return getBackgroundHost() + "/ocr/auth/hachCheck";
    }

    /**
     * 通讯录上传
     */
    public static String uploadAddressBook() {

        return getBackgroundHost() + "/app/contact/create";
    }

    /**
     * APP通话记录上传
     */
    public static String uploadCallRecords() {

        return getBackgroundHost() + "/app/contactRecord/create";
    }

    /**
     * APP短信上传
     */
    public static String uploadSms() {

        return getBackgroundHost() + "/app/message/create";
    }

    /**
     * appList上传
     */
    public static String uploadAppList() {

        return getBackgroundHost() + "/app/appList/create";
    }

    /**
     * 设备信息上传
     */
    public static String uploadDeviceInfo() {

        return getBackgroundHost() + "/app/device/create";
    }

    /**
     * 自动拼接host和必要参数
     */
    public static String appendUrlWithParamsAndHost(String suffixURL) {

        return (getH5BaseHost() + suffixURL + "?") +
                "app_clientid=" + Tools.getChannel() +
                "&app_name=android" +
                "&app_version=" + Tools.getAppVersion() +
                "&phone=" + BaseApplication.sPhoneNum +
                "&user_id=" + BaseApplication.mUserId +
                "&user_name=" + StringUtils.toUTF8(BaseApplication.sUserName) +
                "&url_version=" + System.currentTimeMillis();
    }

    /**
     * 自动拼接必要参数
     */
    public static String appendUrlWithParamsOnly(String URL) {

        return (URL + "?") +
                "app_clientid=" + Tools.getChannel() +
                "&app_name=android" +
                "&app_version=" + Tools.getAppVersion() +
                "&phone=" + BaseApplication.sPhoneNum +
                "&user_id=" + BaseApplication.mUserId +
                "&user_name=" + StringUtils.toUTF8(BaseApplication.sUserName) +
                "&url_version=" + System.currentTimeMillis();
    }

    //IntentUtils.openWebViewActivity(mContext, UrlHostConfig.GET_H5_AUTHENTICATION());

    //认证开始
    public static String GET_H5_AUTHENTICATION() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_AUTHENTICATION);
    }

    //认证开始
    public static String GET_H5_INFO_CONFIRM() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_APPLY_INFO_CONFIRM);
    }

    //借款列表

    public static String GET_H5_BORROW_LIST() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_BORROW_LIST);
    }
    //还款

    public static String GET_H5_REPAY() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_REPAY);
    }
    //协议

    public static String GET_H5_PRIVACY_AGREEMENT() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_PRIVACY_AGREEMENT);
    }

    //个人信息
    public static String H5_USER_INFO() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_USER_INFO);
    }

    //工作
    public static String H5_COMPANY() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_COMPANY);
    }

    //联系人
    public static String H5_CONTACT() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_CONTACT);
    }

    //上传照片
    public static String H5_UPLOAD() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_UPLOAD);
    }

    //银行卡列表
    public static String H5_BANK_CARD_LIST(String selectedAmount, String selectedPeriod, String selectedTotal) {
        return appendUrlWithParamsAndHost(ConstantValue.H5_BANK_CARD_LIST) +
                "&selectedAmount=" + selectedAmount +
                "&selectedPeriod=" + selectedPeriod +
                "&selectedTotal=" + selectedTotal;
    }

    //客服
    public static String H5_CUSTOMER_SERVICE() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_CUSTOMER_SERVICE);
    }

    //消息
    public static String GET_H5_MSG() {
        return appendUrlWithParamsAndHost(ConstantValue.H5_MSG);
    }
}