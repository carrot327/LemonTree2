package com.cocotree.android.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mio4kon
 * @ClassName: ActivityCollector
 * @Description: 作为活动管理器, 方便暂存活动的Activity
 * @mail mio4kon.dev@gmail.com
 * @date 2014-9-22 下午2:43:54
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
