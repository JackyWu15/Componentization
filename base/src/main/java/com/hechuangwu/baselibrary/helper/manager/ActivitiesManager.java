package com.hechuangwu.baselibrary.helper.manager;

import android.support.v7.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;


/**
 * Describe：管理所有的Activity
 * Created by 吴天强 on 2018/10/15.
 */

public class ActivitiesManager {

    //保存所有创建的Activity
    private Set<AppCompatActivity> allActivities = new HashSet<>();

    /**
     * 添加Activity到管理器
     *
     * @param activity activity
     */
    public void addActivity(AppCompatActivity activity) {
        if (activity != null) {
            allActivities.add(activity);
        }
    }


    /**
     * 从管理器移除Activity
     *
     * @param activity activity
     */
    public void removeActivity(AppCompatActivity activity) {
        if (activity != null) {
            allActivities.remove(activity);
        }
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        for (AppCompatActivity activity : allActivities) {
            activity.finish();
        }

    }

}
