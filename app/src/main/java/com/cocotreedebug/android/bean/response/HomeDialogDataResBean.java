package com.cocotreedebug.android.bean.response;

import com.cocotreedebug.android.base.BaseResponseBean;

/**
 * 导流弹框的弹框数据
 */
public class HomeDialogDataResBean extends BaseResponseBean {

    /**
     * id : 1
     * alertMessage1 : 您有一份
     * 30000元额度待激活
     * alertMessage2 : 马上再去撸一笔！
     * jumpUrl : http://47.111.33.30/#/home
     * alertStatus : 1
     * dialogPosition : borrow
     * openType : 1
     * state : 1
     * createTime : 1550911106000
     * updateTime : 1550904379000
     */

    public int id;
    public String alertMessage1;
    public String alertMessage2;
    public String jumpUrl;
    public int alertStatus; //0 不弹出；1 弹出
    public String dialogPosition;  //弹框位置（borrow：借款流程触发    pay：还款返回后触发）
    public int openType; //0  APP内打开；  1：浏览器内打开
    public int state;
    public long createTime;
    public long updateTime;
}
