package com.lemontree.android.manager;

public interface ConstantValue {

    String FIRST_OPEN_APP = "first_open_app";
    String LOGIN_STATE = "login_state";
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
    String EXTEND_PAY = "EXTEND_PAY";


    /**
     * 运营自定义弹框相关标记
     */
    String OPERATION_DIALOG_SHOWED_TIME = "operationDialogRequestTime";
    String OPERATION_DIALOG_HAS_SHOWED = "operationDialogHasShowed";
    String DIALOG_HAS_SHOWED_FOR_ONCE = "dialogHasShowedForOnce";
    String OPERATION_DIALOG_MSG_MD5 = "operationDialogMsgMd5";


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
    //协议
    String H5_AGREEMENT = "/#/agreement/agreement";

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
     * 借款详情
     */
    String NET_REQUEST_URL_ORDER_DETAILS = "/app/order/orderdetails";

    /**
     * 展期费
     */
    String NET_REQUEST_URL_EXTENT_FEE = "/app/repayment/queryAmountByUserId";
}
