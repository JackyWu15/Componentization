package com.hechuangwu.baselibrary.helper.ioc;

import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by cwh on 2019/9/19 0019.
 * 功能:
 */
public class ViewFinder {
    private AppCompatActivity activity;
    private View view;

     ViewFinder(View view) {
        this.view = view;
    }

     ViewFinder(AppCompatActivity activity) {
        this.activity = activity;
    }

     View  findViewById(int viewId){
        return this.activity!=null?activity.findViewById(viewId):view.findViewById(viewId);
    }
}
