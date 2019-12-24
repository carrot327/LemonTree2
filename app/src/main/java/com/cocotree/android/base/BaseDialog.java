package com.cocotree.android.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.cocotree.android.R;

public abstract class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(Context context) {
        super(context, R.style.dialog_default_style);
        this.mContext = context;
        if (context instanceof Activity) {
            this.setOwnerActivity((Activity) context);
        }

    }

    public void show() {
        Activity ownerActivity = this.getOwnerActivity();
        if (ownerActivity != null && !ownerActivity.isFinishing()) {
            super.show();
        }

    }

    public void hide() {
        if (this.isShowing()) {
            Activity ownerActivity = this.getOwnerActivity();
            if (ownerActivity != null && !ownerActivity.isFinishing()) {
                super.hide();
            }
        }

    }

    public void dismiss() {
        if (this.isShowing()) {
            Activity ownerActivity = this.getOwnerActivity();
            if (ownerActivity != null && !ownerActivity.isFinishing()) {
                super.dismiss();
            }
        }

    }

    public void showProgressBar() {
    }

    public void hideProgressBar() {
    }

    public interface OnClickListener {
        void onClick(BaseDialog dialog, View view);
    }
}