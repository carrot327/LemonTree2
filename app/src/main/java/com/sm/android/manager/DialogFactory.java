package com.sm.android.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sm.android.BuildConfig;
import com.sm.android.R;
import com.sm.android.base.BaseDialog;
import com.sm.android.bean.response.CouponResBean;
import com.sm.android.bean.response.HomeDialogDataResBean;
import com.sm.android.bean.response.OperationDialogResBean;
import com.sm.android.bean.response.RecommendDialogResBean;
import com.sm.android.ui.widget.CommonDialog;
import com.sm.android.ui.widget.HomePromptDialog;
import com.sm.android.ui.widget.HomeRecommendDialogOne;
import com.sm.android.ui.widget.HomeRecommendDialogTwo;
import com.sm.android.ui.widget.PayWaySelectDialog;
import com.sm.android.uploadUtil.Tools;
import com.sm.android.utils.MarkUtil;
import com.sm.android.utils.IntentUtils;
import com.sm.android.utils.MyTimeUtils;
import com.sm.android.utils.SPUtils;
import com.sm.android.utils.StringUtils;
import com.sm.android.utils.UIUtils;

import java.util.List;

import static com.sm.android.manager.ConstantValue.EXTEND_PAY;
import static com.sm.android.manager.ConstantValue.NORMAL_PAY;
import static com.sm.android.manager.ConstantValue.PART_PAY;
import static com.sm.android.uploadUtil.UrlHostConfig.getH5BaseHost;

/**
 * 对话框工厂类
 */
public class DialogFactory {
    private static boolean isHomeDaoLiuDialogShow;


    public interface OnClickListener {
        void onClick(Dialog dialog, View view);
    }

    /**
     * 创建通用样式的提示对话框（带一个按钮，默认点击事件为关闭对话框）
     */
    public static CommonDialog createOneButtonCommonDialog(Context context,
                                                           CharSequence title,
                                                           View contentView,
                                                           CharSequence btnText) {
        CommonDialog dialog = createOneButtonCommonDialog(context, title, contentView, btnText, new BaseDialog.OnClickListener() {
            @Override
            public void onClick(BaseDialog dialog, View view) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    /**
     * 创建通用样式的提示对话框（带一个按钮，默认点击事件为关闭对话框）
     */
    public static CommonDialog createOneButtonCommonDialog(Context context,
                                                           CharSequence title,
                                                           CharSequence message,
                                                           CharSequence btnText) {
        CommonDialog dialog = createOneButtonCommonDialog(context, title, message, btnText, new BaseDialog.OnClickListener() {
            @Override
            public void onClick(BaseDialog dialog, View view) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    /**
     * 创建通用样式的提示对话框（带一个按钮），需填入msg
     */
    public static CommonDialog createOneButtonCommonDialog(Context context,
                                                           CharSequence title,
                                                           CharSequence message,
                                                           CharSequence btnText,
                                                           BaseDialog.OnClickListener listener) {
        CommonDialog dialog = createCommonDialog(context, title, message, null, null, btnText, listener);
        dialog.setBtnVisibility(false, true);
        return dialog;
    }

    /**
     * 创建通用样式的提示对话框（带一个按钮）
     */
    public static CommonDialog createOneButtonCommonDialog(Context context,
                                                           CharSequence title,
                                                           View contentView,
                                                           CharSequence btnText,
                                                           BaseDialog.OnClickListener listener) {
        CommonDialog dialog = createCommonDialog(context, title, contentView, null, null, btnText, listener);
        dialog.setBtnVisibility(false, true);
        return dialog;
    }

    /**
     * 创建通用样式的提示对话框
     */
    public static CommonDialog createCommonDialog(Context context,
                                                  CharSequence title,
                                                  CharSequence message,
                                                  CharSequence negativeBtnText,
                                                  BaseDialog.OnClickListener negativeBtnClickListener,
                                                  CharSequence positiveBtnText,
                                                  BaseDialog.OnClickListener positiveBtnClickListener) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negativeBtnText, negativeBtnClickListener)
                .setPositiveButton(positiveBtnText, positiveBtnClickListener).build();
        return dialog;
    }

    /**
     * 创建通用样式的提示对话框
     */
    private static CommonDialog createCommonDialog(Context context,
                                                   CharSequence title,
                                                   View contentView,
                                                   CharSequence leftBtnText,
                                                   BaseDialog.OnClickListener negativeBtnClickListener,
                                                   CharSequence rightBtnText,
                                                   BaseDialog.OnClickListener positiveBtnClickListener) {
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(title)
                .setContentView(contentView)
                .setNegativeButton(leftBtnText, negativeBtnClickListener)
                .setPositiveButton(rightBtnText, positiveBtnClickListener).build();
        return dialog;
    }

    /**
     * 通用文字弹框
     *
     * @param context
     * @param response
     * @return
     */
    public static Dialog createCommonTextDialog(Context context, OperationDialogResBean response) {
        CommonDialog commonDialog = new CommonDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage(response.content)
                .setPositiveButton("确定", new BaseDialog.OnClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog, View view) {
                        if (!TextUtils.isEmpty(response.redirect_url)) {
                            IntentUtils.openWebViewActivity(context, response.redirect_url);
                        }
                        MarkUtil.postEvent(context, "home_alert_confirm");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new BaseDialog.OnClickListener() {
                    @Override
                    public void onClick(BaseDialog dialog, View view) {
                        MarkUtil.postEvent(context, "home_alert_cancel");
                        dialog.cancel();
                    }
                })
                .build();
        if (!TextUtils.isEmpty(response.redirect_url)) {
            commonDialog.setBtnVisibility(true, true);
        } else {
            commonDialog.setBtnVisibility(false, true);
        }
        return commonDialog;
    }


    /**
     * 普通运营弹框
     *
     * @param context
     * @return
     */
    public static Dialog createNoticeDialog(final Context context, String noticeContent) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_notice, null);
        ImageView ivBackground = contentView.findViewById(R.id.iv_notice_background);
        TextView tvContent = contentView.findViewById(R.id.tv_notice_content);
        Button btn = contentView.findViewById(R.id.btn_coupon);
        if (!TextUtils.isEmpty(noticeContent))
            tvContent.setText(noticeContent);

        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparence);

        ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 优惠券弹框
     *
     * @param context
     * @return
     */
    public static Dialog createCouponDialog(final Context context, CouponResBean bean) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_coupon, null);
        ImageView ivBackground = contentView.findViewById(R.id.iv_coupon_background);
        Button btn = contentView.findViewById(R.id.btn_coupon);
        TextView tv_coupon_proportion = contentView.findViewById(R.id.tv_coupon_proportion);
        TextView tv_coupon_deadline = contentView.findViewById(R.id.tv_coupon_deadline);
        tv_coupon_proportion.setText("Rp." + bean.couponCutAmount);
        tv_coupon_deadline.setText("Tanggal jatuh tempo：\n\n" + MyTimeUtils.timeStamp2Date(bean.active_time) + " 23:59");
        ivBackground.setMinimumWidth(UIUtils.getScreenWidth() * 5 / 6);
        ivBackground.setMinimumHeight(UIUtils.getScreenWidth());

        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparence);


        ivBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * @param context
     * @return
     */
    public static Dialog createSelectCouponDialog(final Context context, TextView tvCoupon, TextView tvFinalPayAmount, String originPayAmount, String afterCutAmount, CouponResBean bean) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_coupon, null);
        LinearLayout ll = contentView.findViewById(R.id.ll_coupon);
        Button btn = contentView.findViewById(R.id.btn_confirm);
        TextView tv_coupon_amount = contentView.findViewById(R.id.tv_coupon_amount);
        TextView tv_coupon_deadline = contentView.findViewById(R.id.tv_coupon_deadline);
        CheckBox checkBox = contentView.findViewById(R.id.checkbox_coupon);
        if (0 == SPUtils.getInt(ConstantValue.IS_SELECT_COUPON, 1)) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }
        tv_coupon_amount.setText("Diskon Pokok: Rp." + bean.couponCutAmount);
        tv_coupon_deadline.setText("Tanggal jatuh tempo: " + MyTimeUtils.timeStamp2Date(bean.active_time) + " 23:59");

        ll.setMinimumWidth(UIUtils.getScreenWidth() * 5 / 6);

        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparence);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    tvCoupon.setText("-Rp." + bean.couponCutAmount);
                    tvFinalPayAmount.setText("Rp." + afterCutAmount);
                } else {
                    tvCoupon.setText("Kupon x1");
                    tvFinalPayAmount.setText(originPayAmount);
                }
                dialog.dismiss();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SPUtils.putInt(ConstantValue.IS_SELECT_COUPON, 1);
                } else {
                    SPUtils.putInt(ConstantValue.IS_SELECT_COUPON, 0);
                }
            }
        });

        return dialog;
    }


    /**
     * 首页 借款攻略/产品简介
     *
     * @param context
     * @return
     */
    public static Dialog createHomePromptDialog(final Context context, int res) {
        HomePromptDialog dialog = new HomePromptDialog(context, res, new HomePromptDialog.CloseListener() {
            @Override
            public void close(Dialog dialog, View view) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(R.color.transparence);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 导流
     *
     * @param context
     * @return
     */
    public static Dialog createRecommendDialogOne(final Context context, final List<RecommendDialogResBean.ProductListBean> data) {
        HomeRecommendDialogOne dialog = new HomeRecommendDialogOne(context, data, new HomeRecommendDialogOne.CloseListener() {
            @Override
            public void close(Dialog dialog, View view) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(R.color.transparence);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static Dialog createRecommendDialogTwo(final Context context, final List<RecommendDialogResBean.ProductListBean> data) {
        HomeRecommendDialogTwo dialog = new HomeRecommendDialogTwo(context, data, new HomeRecommendDialogTwo.CloseListener() {
            @Override
            public void close(Dialog dialog, View view) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(R.color.transparence);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 首页导流弹框
     *
     * @param context
     * @param homeDialogDataResBean
     * @return
     */
    public static Dialog createDaoLiuDialog(final Context context, HomeDialogDataResBean homeDialogDataResBean) {
        if (isHomeDaoLiuDialogShow) {
            return null;
        } else {
            isHomeDaoLiuDialogShow = true;

            View contentView = LayoutInflater.from(context).inflate(R.layout.home_event_dialog, null);
            TextView tvContent = contentView.findViewById(R.id.tv_content);
            TextView tvSubtitle = contentView.findViewById(R.id.tv_subtitle);
            TextView tvConfirm = contentView.findViewById(R.id.tv_dialog_confirm);
            ImageView ivClose = contentView.findViewById(R.id.iv_dialog_close);

            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(contentView);

            tvContent.setText(homeDialogDataResBean.alertMessage1);
            tvSubtitle.setText(homeDialogDataResBean.alertMessage2);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    isHomeDaoLiuDialogShow = false;
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeDialogDataResBean.openType == 0) {//0  APP内打开；  1：浏览器内打开
                        IntentUtils.openWebViewActivity(context, homeDialogDataResBean.jumpUrl);
                    } else {
                        if (!TextUtils.isEmpty(homeDialogDataResBean.jumpUrl)) {
                            Uri uri = Uri.parse(homeDialogDataResBean.jumpUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    }
                    dialog.dismiss();
                    isHomeDaoLiuDialogShow = false;
                }
            });

            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {

                dialogWindow.setBackgroundDrawableResource(R.color.transparence);

            }
            return dialog;
        }
    }

    /**
     * 还款选择框
     *
     * @param context
     * @return
     */
    public static Dialog payWaySelectDialog(final Context context, String from) {
        PayWaySelectDialog dialog = new PayWaySelectDialog(context, from);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(R.color.transparence);
        }
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 还款选择框
     *
     * @param context
     * @return
     */
    public static AlertDialog payWaySelectMaterialDialog(
            final Context context, String title, String[] items, String from, String payAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        //replace ConvenienceStore to Toko serba ada
        String[] myItems = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            if ("ConvenienceStore".equals(items[i])) {
                myItems[i] = "Toko serba ada";
            } else {
                myItems[i] = items[i];
            }
        }

        builder.setItems(myItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                String pay_way = items[position];
                String pay_type = EXTEND_PAY.equals(from) ? "2" : "1";//NORMAL_PAY和PART_PAY为1
                String isUseCoupon = "0";
                if (NORMAL_PAY.equals(from) && 1 == (SPUtils.getInt(ConstantValue.IS_SELECT_COUPON, 1))) {
                    isUseCoupon = "1";
                }
                String suffixURL = ConstantValue.H5_REPAY + "?type=" + pay_way + "&from=" + pay_type;

                String url = getH5BaseHost() + suffixURL + "&" +
                        "app_clientid=" + Tools.getChannel() +
                        "&app_name=android" +
                        "&app_version=" + Tools.getAppVersion() +
                        "&phone=" + BaseApplication.sPhoneNum +
                        "&user_id=" + BaseApplication.mUserId +
                        "&repayment_amount=" + payAmount +
                        "&isUseCoupon=" + isUseCoupon +
                        "&user_name=" + StringUtils.toUTF8(BaseApplication.sUserName);

                IntentUtils.openWebViewActivity(context, url);
                if (BuildConfig.DEBUG) {
                    if ("0".equals(isUseCoupon)) {
                        Toast.makeText(context, "未选用优惠券", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "已选用优惠券", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
