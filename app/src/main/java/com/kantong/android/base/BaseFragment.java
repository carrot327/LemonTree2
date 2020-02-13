package com.kantong.android.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kantong.android.R;
import com.kantong.android.ui.widget.AppTitleBar;
import com.kantong.android.utils.LogUtils;
import com.kantong.android.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * @author linqi
 * @description
 */
public abstract class BaseFragment<P extends BasePresenter> extends BaseRootFragment<P> implements IBaseView {

    private FrameLayout mBaseContentContainer;
    private View mContentView; // 内容View对象(不包含最外层的BaseContentContainer)
    private AppTitleBar mTitleBar;
//    private NetErrorView mNetErrorView; // 网络错误视图

    @Override
    protected void onViewCreatedFirstLogic(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onDestroyViewFirstLogic() {
    }

    @Override
    protected void onDestroyFirstLogic() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutResId = getLayoutResId();
        if (layoutResId > 0 && isUseDefaultTitleBar()) {
            View resultView; // 最终返回的View引用
            mContentView = inflater.inflate(layoutResId, null);
            resultView = inflater.inflate(R.layout.base_content_container, null);
            mBaseContentContainer = resultView.findViewById(R.id.base_content_container);
            mBaseContentContainer.addView(mContentView);
            return resultView;
        } else {
            return mContentView = super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTitleBar(view);
    }

    private void initTitleBar(View view) {
        if (mTitleBar == null) {
            mTitleBar = (AppTitleBar) view.findViewById(R.id.app_titlebar);
        }
        if (mTitleBar != null) {
            mTitleBar.setBtnBackVisible(false);
            initTitleBar(mTitleBar);
        }
    }

    protected void initTitleBar(AppTitleBar titleBar) {
        // empty
    }

    @Override
    public void i(String text) {
        LogUtils.i("###" + toString(), text);
    }

    @Override
    public void e(String text) {
        LogUtils.e("###" + toString(), text);
    }

    @Override
    public void showProgressBar() {
        if (mContext != null && mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showProgressBar();
        }
    }

    @Override
    public void hideProgressBar() {
        if (mContext != null && mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).hideProgressBar();
        }
    }


    //暂时用不着，后面再详看功能，先注释掉 2019-01-11 11:26:13
    /*@Override
    public void openActivity(Class<? extends Activity> clazz) {
        if (clazz != null) {
            Intent inetnt = new Intent(mContext, clazz);
            openActivity(inetnt);
        }
    }

    @Override
    public void openActivity(Intent intent) {
        openActivityForResult(intent, -1);
    }

    @Override
    public void openActivityForResult(Intent intent, int requetCode) {
        if (intent != null) {
            if (-1 == requetCode) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, requetCode);
            }
        }
    }*/

    @Override
    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            UIUtils.showToast(text);
        }
    }

    protected boolean isUseDefaultTitleBar() {
        return false;
    }

    public View getContentView() {
        return mContentView;
    }

    public FrameLayout getBaseContentContainer() {
        return mBaseContentContainer;
    }

    protected AppTitleBar getTitleBar() {
        return mTitleBar;
    }
}
