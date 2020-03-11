package com.lemontree.android.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lemontree.android.R;


public class DrawableShowDialog extends Dialog {
    Listener mListener;
    Context mContext;
    Drawable mImgDrawable;
    ImageView iv_content;

    public DrawableShowDialog(Context context, Drawable bitmap, Listener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        mImgDrawable = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);
        initView();
    }

    private void initView() {
        iv_content = findViewById(R.id.iv_content);
        iv_content.setImageDrawable(mImgDrawable);
        iv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.contentClick(DrawableShowDialog.this, v);
            }
        });
    }

    public interface Listener {
        void contentClick(Dialog dialog, View view);
    }
}
