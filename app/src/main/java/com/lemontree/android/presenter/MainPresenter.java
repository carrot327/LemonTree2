package com.lemontree.android.presenter;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

import com.lemontree.android.base.BasePresenter;
import com.lemontree.android.iview.IMainView;

import org.greenrobot.eventbus.EventBus;

/**
 * @author evanyu
 * @date 17/11/22
 */

public class MainPresenter extends BasePresenter<IMainView> {


    public MainPresenter(Context context, IMainView view, Activity activity, Fragment fragment) {
        super(context, view, activity, fragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
