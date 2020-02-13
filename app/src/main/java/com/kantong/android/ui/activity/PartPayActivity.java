package com.kantong.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kantong.android.BuildConfig;
import com.kantong.android.R;
import com.kantong.android.base.BaseActivity;
import com.kantong.android.manager.ConstantValue;
import com.kantong.android.manager.DialogFactory;
import com.kantong.android.utils.CurrencyFormatUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PartPayActivity extends BaseActivity {
    @BindView(R.id.iv_home_back)
    ImageView ivHomeBack;
    @BindView(R.id.et_part_pay_amount)
    EditText etPartPayAmount;
    @BindView(R.id.tv_total_pay)
    TextView tvTotalPay;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_confirm)
    Button btnPartPay;

    private String[] mPayWayNameArr;
    private int mTotalPayAmount;
    private String mInputNumString;


    public static Intent createIntent(Context context) {
        return new Intent(context, PartPayActivity.class);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_part_pay;
    }

    @Override
    protected void initializeView() {
        inputWithCommaListener(etPartPayAmount);
        etPartPayAmount.setLongClickable(false);
        etPartPayAmount.setTextIsSelectable(false);
    }

    @Override
    protected void loadData() {
        String totalPayAmountString = getIntent().getStringExtra("totalPayAmount");
        tvTotalPay.setText(formatIndMoney(totalPayAmountString));

        if (!TextUtils.isEmpty(totalPayAmountString)) {
            mTotalPayAmount = Integer.valueOf(totalPayAmountString);
        }
//        mTotalPayAmount = 21000;
        mPayWayNameArr = getIntent().getStringArrayExtra("payWayArr");

    }

    public void inputWithCommaListener(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.d("karl", "onTextChanged");
                if (count != before) {
                    String sss = "";
                    String string = s.toString().replace(",", "");
                    int b = string.length() / 3;
                    if (string.length() >= 3) {
                        int yushu = string.length() % 3;
                        if (yushu == 0) {
                            b = string.length() / 3 - 1;
                            yushu = 3;
                        }
                        for (int i = 0; i < b; i++) {
                            sss = sss + string.substring(0, yushu) + "," + string.substring(yushu, 3);
                            string = string.substring(3, string.length());
                        }
                        sss = sss + string;
                        editText.setText(sss);
                    }
                }
                editText.setSelection(editText.getText().length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.d("karl", "beforeTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("karl", "afterTextChanged");

                String currentContent = editText.getText().toString();
                mInputNumString = currentContent.replace(",", "");

                if (currentContent.startsWith("0")) {//禁止出现开头为0的情况
                    editText.setText("");
                } else {
                    //judge if input num is between minimum and maximum
                    if (!TextUtils.isEmpty(mInputNumString)) {
                        int inputNum = Integer.valueOf(mInputNumString);

                        if (inputNum < 100000) {
                            btnPartPay.setEnabled(false);
                        } else if (inputNum > mTotalPayAmount) {
                            btnPartPay.setEnabled(false);
                        } else {
                            btnPartPay.setEnabled(true);
                            tvHint.setText(R.string.text_part_pay_hint_min);
                            tvHint.setTextColor(getResources().getColor(R.color.Grey400));
                        }
                        if (BuildConfig.DEBUG) {
                            btnPartPay.setEnabled(true);//为测试放开按钮限制
                        }
                    }

                }

            }

        });
    }

    @OnClick({R.id.iv_home_back, R.id.btn_confirm, R.id.tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_back:
                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(etPartPayAmount.getWindowToken(), 0);
                }
                finish();
                break;
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(mInputNumString)) {
                    int inputNum = Integer.valueOf(mInputNumString);
                    if (!BuildConfig.DEBUG) {
                        if (inputNum < 100000) {
                            tvHint.setText(R.string.text_part_pay_hint_min);
                            tvHint.setTextColor(getResources().getColor(R.color.red));
                        } else if (inputNum > mTotalPayAmount) {
                            tvHint.setText(R.string.text_part_pay_hint_max);
                            tvHint.setTextColor(getResources().getColor(R.color.red));
                        }
                    }
                }
                showPayWayDialog(ConstantValue.PART_PAY);
                break;
            case R.id.tv_edit:
                //调起键盘
                showInputMethod(mContext);
                etPartPayAmount.setFocusable(true);
                etPartPayAmount.setFocusableInTouchMode(true);
                etPartPayAmount.requestFocus();
                break;
        }
    }

    private void showPayWayDialog(String from) {
        //(type 1--便利店2--atm3--手机银行   from 1--正常2--展期)
        if (mPayWayNameArr != null && mPayWayNameArr.length > 0) {
            DialogFactory.payWaySelectMaterialDialog(mContext, getResources().getString(R.string.dialog_text_pay_method), mPayWayNameArr, from, mInputNumString).show();
        } else {
            showToast(getString(R.string.text_no_pay_way));
        }
    }

    private void showInputMethod(Context context) {
        //自动弹出键盘
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //强制隐藏Android输入法窗口
        // inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
    }

    private String formatIndMoney(String fee) {
        if (!TextUtils.isEmpty(fee)) {
            return String.format("Rp.%s", formatNumber(Integer.parseInt(fee)));
        } else {
            return String.format("Rp.%s", "0");
        }
    }

    private String formatNumber(int selectInterest) {
        return CurrencyFormatUtils.formatDecimal(String.valueOf(selectInterest));
    }
}
