package com.sm.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sm.android.R;
import com.sm.android.manager.ConstantValue;
import com.sm.android.manager.NetConstantValue;

public class TuringCodeDialog extends Dialog {

    private Context mContext;
    private String mPhoneNum;
    private TuringDialogListener mListener;
    private EditText etTuringCode;
    private ImageView ivGraphValidateCode;

    public TuringCodeDialog(Context context, String phoneNum, TuringDialogListener listener) {
        super(context);
        mContext = context;
        mPhoneNum = phoneNum;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_turing_code_input);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        etTuringCode = findViewById(R.id.et_turing_code);
        ivGraphValidateCode = findViewById(R.id.iv_turing_code);
        getTuringCode(mPhoneNum);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelClick(TuringCodeDialog.this);

            }
        });
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onConfirmClick(TuringCodeDialog.this, etTuringCode.getText().toString().trim());
            }
        });
        findViewById(R.id.iv_turing_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新图形码
                if (mPhoneNum != null) {
                    getTuringCode(mPhoneNum);
                } else {
                    getTuringCode("111");
                }
            }
        });
    }

    /*获取图形验证码*/
    private void getTuringCode(String phoneNum) {
        String url = NetConstantValue.BASE_HOST + ConstantValue.NET_REQUEST_URL_TURING_CODE + "/" + phoneNum + "?t=" + System.currentTimeMillis();
        Glide.with(mContext.getApplicationContext()).load(url).into(ivGraphValidateCode);
    }

    public interface TuringDialogListener {
        void onConfirmClick(Dialog dialog, String verifyCode);

        void onCancelClick(Dialog dialog);
    }
}
