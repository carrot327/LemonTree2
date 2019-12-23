package com.sm.android.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sm.android.R;
import com.sm.android.base.BaseActivity;
import com.sm.android.manager.BaseApplication;
import com.sm.android.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class BorrowRecordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.btn_goto_borrow)
    Button btnGotoBorrow;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_borrow_record;
    }

    @Override
    protected void initializeView() {

    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.iv_back, R.id.btn_goto_borrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_goto_borrow:
                if (TextUtils.isEmpty(BaseApplication.sUserName)) {
                    IntentUtils.gotoWebPageWithSuffixUrl(mContext, "/#/authList");
                } else {
                    IntentUtils.gotoMainActivity(mContext, MainActivity.TAB_HOME);
                }
                break;
        }
    }
}
