package com.sm.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sm.android.R;


public class HomePromptDialog extends Dialog {
    CloseListener mListener;
    Context mContext;
    int mImgRes;
    ImageView iv_dialog_close, iv_content;

    public HomePromptDialog(Context context, int res, CloseListener closeListener) {
        super(context);
        mContext = context;
        mListener = closeListener;
        mImgRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_prompt_dialog);
        initView();
    }

    private void initView() {
        iv_dialog_close = findViewById(R.id.iv_dialog_close);
        iv_content = findViewById(R.id.iv_content);
        iv_content.setImageResource(mImgRes);
        iv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.close(HomePromptDialog.this, v);
            }
        });
    }

    public interface CloseListener {
        void close(Dialog dialog, View view);
    }
}
