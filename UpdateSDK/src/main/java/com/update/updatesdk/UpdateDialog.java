package com.update.updatesdk;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * Created by karl on 2017/11/2.
 */

public class UpdateDialog extends Dialog {
    public OnBackPressedListener mListener;

    public interface OnBackPressedListener {
        void onBackPressed(Dialog dialog);
    }

    public UpdateDialog(@NonNull Context context) {
        this(context, 0);
    }

    public UpdateDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_update_new);
    }

    public UpdateDialog(@NonNull Context context, @StyleRes int themeResId, int layout) {
        super(context, themeResId);
        setContentView(layout);
    }

    protected UpdateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mListener != null) {
            mListener.onBackPressed(this);
        }
    }
}
