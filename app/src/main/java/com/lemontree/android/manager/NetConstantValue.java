package com.lemontree.android.manager;


import com.lemontree.android.uploadUtil.UrlHostConfig;

public class NetConstantValue {

    public static String BASE_HOST = UrlHostConfig.getBackgroundHost();

    /**
     * 导流 banner 获取
     */
    public static String NET_REQUEST_URL_FLOWIO_GET_BANNER = "/flowio/get/banner";

    /**
     * 导流是否展示弹框
     */
    public static String NET_REQUEST_URL_FLOWIO_GET_POP_PRODUCT = "/flowio/get/popProduct";

    /**
     * 导流商家产品获取
     */
    public static String NET_REQUEST_URL_FLOWIO_GET_CUSTOMERPRODUCT = "/flowio/get/customerproduct";

    /**
     * 导流商家产品获取
     */
    public static String NET_REQUEST_URL_FLOWIO_MARK_CUSTOMERPRODUCT = "/flowio/mark/customerproduct";

    /**
     * MJB控制
     */
    public static String NET_REQUEST_URL_MJB_CONTROL = "/appmjb/getAZversionstatus";

    /**
     * 获取版本状态用于强更
     */
    public static String NET_REQUEST_UPDATE_STATUS = "/app/getstatus4update";

    /**
     * 定向强更
     */
    public static String NET_REQUEST_UPDATE_STATUS_BY_USER_ID = "/app/getstatus4updateByUserid";

    /**
     * 获取发现页显示状态
     */
    public static String NET_REQUEST_GET_FIND_STATUS = "/app/getfindxstatus";

    /**
     * 获取首页弹框控制接口
     */
    public static String NET_REQUEST_HOME_OPEREATION_DIALOG = "/app/content/getContentByMobile";

    /**
     * 埋点
     */
    public static String NET_REQUEST_URL_EVENT_MARK = "/mark";

    /**
     * 商城首页开关
     */
    public static String NET_REQUEST_HOME_SHOPPING_STATE = "/app/store/getAppUserStore";


    /**
     * 置零天数
     */
    public static String NET_REQUEST_ZERO_DAY = "/app/zeroday/getStatusByUserId";

    /**
     * 还款期数接口
     */
    public static String NET_REQUEST_GET_PEROID_DETAIL = "/app/period/getPeriodDetail";

    /**
     * 还款期数接口
     */
    public static String GET_REPAY_WAY_LIST = "/app/repayment/choosePayMethod";

    /**
     * 还款码设置为失效
     */
    public static String SET_PAY_CODE_INVALID = "/app/repayment/payCodeDated";

    /**
     * 获取用户当前type状态
     */
    public static String GET_USER_TYPE_STATE= "/app/getTypeAndUserId";

    /**
     * 人脸识别加密数据
     */
    public static String POST_LIVENESS_ENCRYPTION_DATA= "/ocr/auth/hachCheck";

}
