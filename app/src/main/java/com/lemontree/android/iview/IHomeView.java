package com.lemontree.android.iview;


import com.lemontree.android.base.IBaseView;
import com.lemontree.android.bean.response.BorrowApplyInfoResBean;
import com.lemontree.android.bean.response.CouponResBean;
import com.lemontree.android.bean.response.GetExtendFeeResBean;
import com.lemontree.android.bean.response.GetPayWayListResBean;
import com.lemontree.android.bean.response.HomeDataResBean;

import java.util.List;

public interface IHomeView extends IBaseView {
    //设置首页数据
    void setHomeData(HomeDataResBean homeData);

    //刷新首页
    void refreshHomeData();

    //停止刷新
    void stopRefresh();

    //设置借款信息
    void setBorrowInfo(BorrowApplyInfoResBean borrowData);

    //设置订单信息
    void setOrderInfo(BorrowApplyInfoResBean borrowData);

    //设置提交借款确认弹框信息
    void setSubmitDialogData(BorrowApplyInfoResBean borrowData);

    //展示借款信息
    void showLoanInfoLayout();

    //展期费用
    void showExtendPageData(GetExtendFeeResBean fee);

    //set Pay way data
    void setPayWayData(GetPayWayListResBean data);


    //show refuse info Dialog
    void setRefuseState();


    //优惠券信息
    void handleCouponInfo(CouponResBean data);

    void noCoupon(CouponResBean data);

    // 设置文字banner
    void setTextBanner(List<String> noticeList, List<String> urlList);
}
