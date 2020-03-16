package com.lemontree.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lemontree.android.R;


public class PicExampleDialog extends Dialog {
    private CloseListener mListener;
    private int mImgRes;

    public PicExampleDialog(Context context, int res, CloseListener closeListener) {
        super(context);
        mListener = closeListener;
        mImgRes = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_example_dialog);
        initView();
    }

    private void initView() {
        Button button = findViewById(R.id.btn_dialog_close);
        ImageView iv_content = findViewById(R.id.iv_content);
        iv_content.setImageResource(mImgRes);
        button.setOnClickListener(v -> mListener.close(PicExampleDialog.this, v));
    }

    public interface CloseListener {
        void close(Dialog dialog, View view);
    }
}
