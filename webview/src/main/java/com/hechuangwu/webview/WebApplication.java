package com.hechuangwu.webview;

import android.app.Application;

import com.hechuangwu.baselibrary.base.BaseApplication;


/**
 * 作者：created by ${zjt} on 2019/3/5
 * 描述:
 */
public class WebApplication extends BaseApplication {

    static WebApplication webApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        webApplication =new WebApplication();
    }




    public static Application getWebApplication(){
        return webApplication;
    }
}
