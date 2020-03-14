package com.lemontree.android.manager;

public interface ConstantValue {

    String FIRST_OPEN_APP = "first_open_app";
    String LOGIN_STATE = "login_state";
    String GOD_MODE = "god_mode";
    String JWT_KEY = "jwt";
    String KEY_SERVER_TIME_OFFSET = "serverTimeOffset";
    String KEY_LATEST_LOGIN_NAME = "lastLoginName";
    String KEY_USER_ID = "user_id";
    String KEY_CALLBACK_FUNCTION = "callbackFunction";
    String KEY_EXIT_APP = "exitApp";

    /**
     * =================sp============================
     */
    String USERNAME_KEY = "username"; //用户名
    String REALNAME_KEY = "realname";    //姓名
    String IDNUM_KEY = "idnum";    //身份证
    String USER_ID = "user_id";    // 用户id
    String LOGIN_PASSWORD = "login_password"; //登录密码
    String PHONE_NUMBER = "phone_number";


    String KEY_FROM = "from";
    int FROM_HOME = 1;
    int FROM_MINE = 2;

    String UPLOAD_SMS_SUCCESS = "upload_sms_success";
    String UPLOAD_SMS_TIME = "upload_sms_time";

    String UPLOAD_CONTACT_SUCCESS = "upload_contact_success";
    String UPLOAD_CONTACT_TIME = "upload_contact_time";

    String NORMAL_PAY = "NORMAL";
    String PART_PAY = "PART_PAY";
    String EXTEND_PAY = "EXTEND_PAY";


    /**
     * 运营自定义弹框相关标记
     */
    String OPERATION_DIALOG_SHOWED_TIME = "operationDialogRequestTime";
    String OPERATION_DIALOG_HAS_SHOWED = "operationDialogHasShowed";
    String DIALOG_HAS_SHOWED_FOR_ONCE = "dialogHasShowedForOnce";
    String OPERATION_DIALOG_MSG_MD5 = "operationDialogMsgMd5";
    String COUPON_DIALOG_HAS_SHOWED = "couponDialogHasShowed";
    String OPERATION_NORMAL_DIALOG_HAS_SHOWED = "operationNormalDialogHasShowed";

    String IS_SELECT_COUPON = "isSelectCoupon"; //"1" 选中， "0" 未选中
    String LAST_ORDER_ID = "lastOrderId"; //"1" 选中， "0" 未选中

    public static final String WHITE_LIST = "WHITE_LIST";
    public static final String OLD_USER = "OLD_USER";
    public static final String DUAN_XIN = "DUAN_XIN";
    public static final String WILD = "WILD";

    /**
     * ======    H5    ======
     **/
    String H5_URL_SUFFIX_SERVICE = "/#/service";

    //认证开始
    String H5_AUTHENTICATION = "/#/";
    //信息确认页
    String H5_APPLY_INFO_CONFIRM = "/#/pages/confirm/confirm";
    //消息
    String H5_MSG = "/#/pages/message/index";
    //借款列表
    String H5_BORROW_LIST = "/#/pages/borrowlist/borrowlist";
    //还款
    String H5_REPAY = "/#/pages/repay/repay";
    //隐私协议
    String H5_PRIVACY_AGREEMENT = "/#/pages/agreement/agreement?type=reg";

    //个人信息
    String H5_USER_INFO = "/#/pages/auth/identityAuth/index";
    //工作
    String H5_COMPANY = "/#/pages/auth/workAuth/index";
    //联系人
    String H5_CONTACT = "/#/pages/auth/contactAuth/index";
    //上传照片
    String H5_UPLOAD = "/#/pages/auth/photoAuth/index";

    //银行卡列表
    String H5_BANK_CARD_LIST = "/#/pages/banklist/banklist";
    //客服
    String H5_CUSTOMER_SERVICE = "/#/pages/service/service";





    /*--------------------------------*/
    /**
     * 获取首页
     */
    String NET_REQUEST_URL_HOMEPAGE_TAB = "/app/homePage/tab";
    /**
     * 首页通知
     */
    String NET_REQUEST_URL_HOME_NOTICE_LIST = "/app/notice/list";


    /**
     * 获取图形验证码
     */
    String NET_REQUEST_URL_TURING_CODE = "/app/img/getCode";
    /**
     * 验证图形验证码
     */
    String NET_REQUEST_URL_CHECK_TURING_CODE = "/app/img/verify";

    /**
     * 检查是否需要图形码验证
     */
    String NET_REQUEST_URL_IS_NEED_TURING_CHECK = "/app/send/graphicverification";

    /**
     * 短信验证码
     */
    String NET_REQUEST_URL_VERIFY_CODE = "/app/send/verifycode";

    /**
     * 注册
     */
    String NET_REQUEST_URL_LOGIN = "/app/login";

    /**
     * 用户反馈
     */
    String NET_REQUEST_URL_FEEDBACK = "/app/feedback/question";

    /**
     * 注册协议
     */
    String NET_REQUEST_URL_PROTOCOL_REG = "/app/protocol/reg";
    /**
     * 借款协议
     */
    String NET_REQUEST_URL_PROTOCOL_LOAN = "/app/protocol/loan";
    /**
     * 隐私政策
     */
    String NET_REQUEST_URL_PROTOCOL_DISCLAIMER = "/app/protocol/disclaimer";

    /**
     * 借款列表
     */
    String NET_REQUEST_URL_CONFIRMAMT_LIST = "/app/order/confirmAmt/list";

    /**
     * 添加银行卡
     */
    String NET_REQUEST_URL_BANKCARD_ADD = "/app/auth/bankCard/add";

    /**
     * 查询银行卡信息
     */
    String NET_REQUEST_URL_BANKCARD_QUERY = "/app/auth/bankCard/query";

    /**
     * 查询银行卡信息
     */
    String NET_REQUEST_URL_UPDATEORDERSTATUES = "/app/repayment/hcRepayUpdateOrderStatus";

    /**
     * 获取首页banner
     */
    String NET_REQUEST_URL_GET_BANNER_LIST = "/app/banner/getList";

    /**
     * 获取导流弹框数据
     */
    String NET_REQUEST_URL_GET_DIALOG_DATA = "/app/popoverRemindsDiversion/getByDialogPosition";

    /**
     * 获取用户信息
     */
    String NET_REQUEST_URL_GET_USER_INFO = "/idcard/get/idCardInfo";

    /**
     * 获取认证状态
     */
    String NET_REQUEST_URL_GET_AUTH_STATE = "/user/getAuthStatusList";

    //--------------新增接口
    /**
     * 新消息提示
     */
    String NET_REQUEST_URL_NEW_MSG_REMIND = "/app/send/newMessage";

    /**
     * 借款信息
     */
    String NET_REQUEST_URL_BORROW_INFO = "/app/order/toConfirmAmt";
    /**
     * 确认借款
     */
    String NET_REQUEST_URL_CONFIRM_BORROW = "/app/order/confirmAmt";

    /**
     * 展期费
     */
    String NET_REQUEST_URL_EXTENT_FEE = "/app/repayment/queryAmountByUserId";

    /**
     * 获取订单信息
     */
    String NET_REQUEST_URL_GET_ORDER_DETAILS = "/app/order/getOrderdetails";

    /**
     * 获取优惠券信息
     */
    String NET_REQUEST_URL_GET_COUPON_INFO = "/app/repayment/getAppCashCoupon";

    /**
     * 认证_基本信息
     */
    String AUTH_BASIC_INFO = "/idcard/auth/idCardInfo";

    /**
     * 认证_公司信息
     */
    String AUTH_COMPANY_INFO = "/app/auth/workInfo/save";

    /**
     * 认证_联系人信息
     */
    String AUTH_CONTACTS_INFO = "/app/auth/relationship/save";

    /**
     * 认证_图片上传
     */
//    String AUTH_IMG_INFO = "/ocr/auth/ocrKptInfo";
    String AUTH_IMG_INFO = "/ocr/auth/newOcrKptInfo";

    /**
     * ocr check
     */
    String OCR_CHECK = "/ocr/advance/ocrCheck";

    /**
     * 获取用户标签
     */
    String GET_USER_TYPE = "/userLabel/queryCustomerType";

    /**
     * 获取银行卡
     */
    String GET_BANK_NAME_LIST = "/app/auth/bankCard/support";

    /**
     * 获取额度
     */
    String CALCULATE_AMOUNT = "/app/order/calculationAmt";
}
