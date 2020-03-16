package com.lemontree.android.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import androidx.appcompat.app.AlertDialog;

import com.lemontree.android.BuildConfig;
import com.lemontree.android.R;
import com.lemontree.android.base.BaseDialog;
import com.lemontree.android.bean.response.CouponResBean;
import com.lemontree.android.bean.response.HomeDialogDataResBean;
import com.lemontree.android.bean.response.OperationDialogResBean;
import com.lemontree.android.bean.response.RecommendDialogResBean;
import com.lemontree.android.ui.widget.CommonDialog;
import com.lemontree.android.ui.widget.DrawableShowDialog;
import com.lemontree.android.ui.widget.HomePromptDialog;
import com.lemontree.android.ui.widget.HomeRecommendDialogOne;
import com.lemontree.android.ui.widget.HomeRecommendDialogTwo;
import com.lemontree.android.ui.widget.PayWaySelectDialog;
import com.lemontree.android.ui.widget.PicExampleDialog;
import com.lemontree.android.uploadUtil.Tools;
import com.lemontree.android.utils.IntentUtils;
import com.lemontree.android.utils.MarkUtil;
import com.lemontree.android.utils.MyTimeUtils;
import com.lemontree.android.utils.SPUtils;
import com.lemontree.android.utils.StringUtils;
import com.lemontree.android.utils.UIUtils;

import java.util.List;

import static com.lemontree.android.manager.ConstantValue.EXTEND_PAY;
import static com.lemontree.android.manager.ConstantValue.NORMAL_PAY;
import static com.lemontree.android.uploadUtil.UrlHostConfig.getH5BaseHost;

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
     * 认证图片示例图展示
     *
     * @param context
     * @return
     */
    public static Dialog createExamplePicDialog(final Context context, int res) {
        PicExampleDialog dialog = new PicExampleDialog(context, res, new PicExampleDialog.CloseListener() {
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
     * 首页 借款攻略/产品简介
     *
     * @param context
     * @return
     */
    public static Dialog showImgByDrawable(final Context context, Drawable drawable) {
        DrawableShowDialog dialog = new DrawableShowDialog(context, drawable, new DrawableShowDialog.Listener() {
            @Override
            public void contentClick(Dialog dialog, View view) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setBackgroundDrawableResource(R.color.transparence);
        }
        dialog.setCanceledOnTouchOutside(true);
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

    public static Dialog newDialog(Context context, View view, boolean b) {
        Dialog dialog = new AlertDialog.Builder(context, R.style.style_bg_transparent_dialog)
                .setView(view)
                .setCancelable(b)
                .create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 隐私协议弹框
     *
     * @param context
     * @return
     */
    public static Dialog createPrivacyAgreementDialog(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_privacy_agreement, null);
        CheckBox checkBox = view.findViewById(R.id.checkbox);
        TextView tv_privacy_content = view.findViewById(R.id.tv_privacy_content);
        Button btn_privacy = view.findViewById(R.id.btn_privacy);

        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(view);

        tv_privacy_content.setText(Html.fromHtml(privacy_content));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (checkBox.isChecked()) {
                btn_privacy.setEnabled(true);
            } else {
                btn_privacy.setEnabled(false);
            }
        });
        btn_privacy.setOnClickListener(v -> dialog.dismiss());

        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = UIUtils.getScreenWidth() * 7 / 8;
            lp.height = UIUtils.getScreenHeight() * 3 / 4;
            dialogWindow.setAttributes(lp);
        }
        return dialog;
    }

    public static String privacy_content = "<b><big>Colada Perjanjian Privasi</big></b><br><br> Kebijakan Privasi ini telah disusun untuk menjelaskan aplikasi perangkat lunak kami, situs web kami dan pengumpulan, penggunaan dan pengungkapan informasi tentang pengguna layanan lain yang kami sediakan (secara kolektif).\n" +
            "\n" +
            "<br><br><b>Kami meminta Anda untuk memberikan izin untuk data berikut:</b>\n" +
            "<br><br>1. Informasi pendaftaran. Ketika Anda mendaftar ke Akun colada, kami akan meminta Anda untuk memberikan kami informasi pribadi seperti nama, email, dan nomor telepon Anda.\n" +
            "<br><br>2. Data seluler. Ketika Anda menginstal aplikasi kami, kami meminta akses ke aktivitas Anda di ponsel Anda, seperti (tetapi tidak terbatas pada): instalasi aplikasi, SMS, nama, email, dan nomor telepon Anda.\n" +
            "<br><br>3. Gunakan informasi. Kami juga mengumpulkan informasi tentang bagaimana Anda menggunakan layanan kami, termasuk bagaimana Anda melihat dan berinteraksi dengannya, bagaimana Anda menggunakan berbagai bagian dari layanan kami, informasi apa yang Anda cari, apa yang Anda lihat, dan apa yang Anda lakukan.\n" +
            "<br><br>4. Informasi arsip. Dalam bentuk pinjaman aplikasi, analisis kami membutuhkan data pribadi yang benar, data yang diperlukan dalam bentuk berikut: KTP, penggajian dan data lainnya.\n" +
            "<br><br>5. Perekaman data. Jika Anda menggunakan layanan kami, kami akan menggunakan (\"Data Log\") untuk secara otomatis merekam informasi tentang penggunaan Anda.\n" +
            "<br><br>6. Investigasi dan promosi. Kami akan menyelidiki dan mempromosikan dari waktu ke waktu, dan kami dapat memberi Anda peluang untuk berpartisipasi dalam survei, kontes, atau undian melalui layanan Anda. Jika Anda berpartisipasi, kami dapat meminta Anda beberapa informasi tambahan.\n" +
            "\n" +
            "<br><br><b>Bagaimana kami menggunakan informasi yang kami kumpulkan:</b><br>\n" +
            "<br><br>1. Kami menggunakan informasi yang kami kumpulkan (termasuk informasi pendaftaran, ID pengguna dan ID perangkat Anda, informasi tentang layanan dan media sosial, dan informasi survei dan promosi) untuk menyediakan dan meningkatkan layanan kami, mengembangkan penawaran baru dan meningkatkan dan Personalisasi pengalaman Anda di kantong kami, termasuk konten dan iklan yang Anda lihat.\n" +
            "<br><br>2. Kami menggunakan informasi yang dikumpulkan (termasuk informasi pendaftaran, ID pengguna dan ID perangkat, data seluler, data keuangan, dan media sosial Anda) untuk memverifikasi dan meningkatkan kemungkinan mendapatkan persetujuan untuk produk-produk Pocket yang kami tawarkan. Kami juga dapat menggunakan informasi yang kami kumpulkan untuk memverifikasi identitas Anda untuk mencegah penipuan, mencetak riwayat kredit, mengkonfirmasi informasi pekerjaan Anda, dan mengembangkan model statistik yang terkait dengan mesin pemeringkat kredit.\n" +
            "<br><br>3. Kami dapat menggunakan informasi pendaftaran Anda untuk mengirim berita penting tentang layanan ini, serta materi pemasaran yang menurut kami menarik bagi Anda. Jika Anda memutuskan bahwa Anda tidak lagi ingin menerima buletin pemasaran kami, silakan ikuti instruksi berhenti berlangganan di buletin itu.\n" +
            "\n" +
            "<br><br><b>Pengungkapan informasi yang kami kumpulkan:</b><br>\n" +
            "<br><br>1. Informasi yang Anda berikan. Kantong kami hanya untuk penggunaan pribadi dan konten yang Anda ungkapkan tidak tersedia untuk orang lain.\n" +
            "<br><br>2. Berdasarkan persetujuan Anda, kami dapat mengungkapkan informasi yang dikumpulkan dengan pihak ketiga setelah menerima persetujuan Anda.\n" +
            "<br><br>3. Informasi non-pribadi atau non-pribadi. Kami dapat mengungkapkan informasi agregat dan non-pribadi yang kami kumpulkan. Pengungkapan seperti itu tidak termasuk informasi pribadi dan non-publik tentang pengguna individu.\n" +
            "<br><br>4. Penyedia layanan. Kami mempekerjakan pihak ketiga untuk membantu kami dalam menyediakan dan meningkatkan layanan (seperti, tetapi tidak terbatas pada, pemeliharaan, manajemen basis data, dan perusahaan analisis jaringan). Jika pihak ketiga ini menerima informasi pribadi dan non-publik tentang Anda, mereka berkewajiban untuk tidak mengungkapkan atau menggunakannya untuk tujuan lain untuk melakukan tugas kami.\n" +
            "<br><br>5. Kepatuhan dengan hukum dan penegakan hukum. Kami telah bekerja dengan pejabat pemerintah, lembaga penegak hukum dan kelompok swasta untuk menegakkan dan mematuhi hukum. Kami akan mengungkapkan informasi apa pun tentang Anda kepada pejabat pemerintah, lembaga penegak hukum atau kelompok swasta karena kami yakin kami percaya perlu atau pantas untuk menanggapi klaim dan prosedur hukum (termasuk tetapi tidak terbatas pada panggilan pengadilan) untuk melindungi hak-hak properti dan hak-hak saku. Atau pihak ketiga untuk melindungi keselamatan masyarakat atau orang lain, atau untuk mencegah atau membatasi apa yang kami yakini ilegal, tidak etis atau dapat ditindaklanjuti secara hukum, atau mungkin merupakan tindakan ilegal.\n" +
            "\n" +
            "<br><br><b>Informasi keselamatan</b><br>\n" +
            "colada menggunakan langkah-langkah keamanan fisik, teknis, dan administratif yang wajar untuk melindungi informasi yang kami kumpulkan. Tidak ada cara untuk mengirim melalui Internet, dan tidak ada metode penyimpanan elektronik 100% aman. Karena itu, meskipun kami menggunakan metode yang dapat diterima secara komersial untuk melindungi informasi yang kami kumpulkan, kami tidak dapat menjamin bahwa itu benar-benar aman. Jika Pocket menyadari bahwa pelanggaran keamanan dapat menyebabkan pengungkapan informasi pendaftaran Anda yang tidak sah, kami dapat mencoba untuk memberi tahu Anda secara elektronik melalui Layanan sehingga Anda dapat mengambil tindakan perlindungan yang sesuai. colada juga dapat mengirim email ke alamat email yang Anda berikan kepada kami sesuai dengan paragraf ini untuk pemberitahuan.\n" +
            "\n" +
            "<br><br><b>Phishing</b><br>\n" +
            "Pencurian identitas dan apa yang sekarang dikenal sebagai \"phishing\" telah membuat Sakuji North sangat terkejut. Melindungi informasi untuk melindungi Anda dari pencurian identitas adalah prioritas. Kami tidak akan dan tidak akan menanyakan informasi kartu kredit Anda, kata sandi login atau nomor ID Anda kapan saja melalui email atau komunikasi telepon yang tidak aman atau tidak diminta.";
}
