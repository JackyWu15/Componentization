package com.hechuangwu.baselibrary.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hechuangwu.baselibrary.helper.manager.ActivitiesManager;
import com.hechuangwu.baselibrary.helper.manager.ExceptionCrashManager;
import com.hechuangwu.baselibrary.helper.manager.andfix.AndFixPatchManager;


/**
 * 作者：Created by Ding on 2019/5/25
 * 文件描述：
 */
public class BaseApplicationManager {
    //是否开启调试
    private  boolean isDebug =true;
    //全局唯一的context
    private static BaseApplicationManager baseApplicationManager;
    //全局application
    private static Application mApplication;
    //Activity管理器
    private ActivitiesManager mActivitiesManager;
    private BaseApplicationManager(Application application){
        mApplication = application;
    }
    public static BaseApplicationManager getInstance(Application application){
        if(baseApplicationManager ==null){
            synchronized (BaseApplicationManager.class){
                if(baseApplicationManager ==null){
                    baseApplicationManager = new BaseApplicationManager(application);
                }
            }
        }
        return baseApplicationManager;
    }
    public  void onCreate() {
        baseApplicationManager = this;
        create();
        initRouter();
    }

    private void create(){
        //activity管理
        mActivitiesManager = new ActivitiesManager();
        //闪退日志
        ExceptionCrashManager.getInstance().init( mApplication );

        //AndFix
        AndFixPatchManager.getInstance().initPatch( mApplication );
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
        ARouter.init(mApplication);
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
     * @return BaseApplicationManager
     */
    public static Application getApplication() {
        return mApplication;
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
