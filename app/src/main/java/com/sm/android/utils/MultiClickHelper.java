package com.sm.android.utils;

import android.os.SystemClock;

/**
 * 连点事件帮助类
 *
 * @author evanyu
 * @date 17/11/22
 */
public class MultiClickHelper {

    private long[] mHits;
    private int dt; // 触发连点事件的最小时间间隔，单位：毫秒

    /**
     * 默认双击事件，最小触发时间为1000毫秒
     */
    public MultiClickHelper() {
        this(2, 1000);
    }

    /**
     * @param clickCount 触发连点事件的点击次数
     * @param dt         触发连点事件的最小时间间隔，单位：毫秒
     */
    public MultiClickHelper(int clickCount, int dt) {
        if (clickCount < 2) {
            throw new RuntimeException("clickCount must be greater than 1");
        }
        this.dt = dt;
        mHits = new long[clickCount];
    }

    /**
     * 记录一次点击操作
     *
     * @return 是否是连续点击事件
     */
    public boolean click() {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        return (mHits[mHits.length - 1] - mHits[0] <= dt);
    }

}

