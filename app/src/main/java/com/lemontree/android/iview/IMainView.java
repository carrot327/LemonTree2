package com.lemontree.android.iview;

import com.lemontree.android.base.IBaseView;

/**
 * @author evanyu
 * @date 17/11/22
 */

public interface IMainView extends IBaseView {

    /**
     * 切换标签页
     *
     * @param index
     */
    void switchTab(int index);

}
