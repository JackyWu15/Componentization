package com.hechuangwu.baselibrary.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hechuangwu.baselibrary.helper.manager.ExceptionCrashManager;
import com.hechuangwu.baselibrary.helper.manager.ActivitiesManager;


/**
 * 作者：Created by Ding on 2019/5/25
 * 文件描述：
 */
public class BaseApplication extends Application {
    //是否开启调试
    private  boolean isDebug =true;
    //全局唯一的context
    private static BaseApplication application;
    //Activity管理器
    private ActivitiesManager mActivitiesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        create();
        initRouter();
    }

    private void create(){
        application = this;
        //activity管理
        mActivitiesManager = new ActivitiesManager();
        //闪退日志
        ExceptionCrashManager.getInstance().init( this );
    }

    /**
     * 初始化路由
     */
    private void initRouter() {
        //必须在初始化之前写入这两行
        if (isDebug) {
            //打印日志
            ARouter.openLog();
            //开始调试
            ARouter.openDebug();
        }
        //ARouter的实例化
        ARouter.init(this);
    }
    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        exitApp();
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        mActivitiesManager.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }



    /**
     * 获取全局唯一上下文
     *
     * @return BaseApplication
     */
    public static BaseApplication getApplication() {
        return application;
    }


    /**
     * 返回Activity管理器
     */
    public ActivitiesManager getActivitiesManager() {
        if (mActivitiesManager == null) {
            mActivitiesManager = new ActivitiesManager();
        }
        return mActivitiesManager;
    }
}
