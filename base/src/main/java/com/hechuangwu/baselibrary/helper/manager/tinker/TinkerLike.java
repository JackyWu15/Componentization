package com.hechuangwu.baselibrary.helper.manager.tinker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hechuangwu.baselibrary.base.BaseApplicationManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by cwh on 2019/10/1 0001.
 * 功能: ApplicationLike用于监听整个Application生命周期的代理类
 *      如果直接在Application中初始化，生命周期的监听会非常复杂
 */

@DefaultLifeCycle(application = ".BaseTinkerApplication",//编译时会获取TinkerLike这个子类的注解，自动生成BaseTinkerApplication类
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class TinkerLike extends ApplicationLike {
    BaseApplicationManager mBaseApplicationManager;

    public TinkerLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super( application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent );
        mBaseApplicationManager =  BaseApplicationManager.getInstance( application );
        mBaseApplicationManager.onCreate( );
    }


    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached( base );
        //使用应用支持分包
        MultiDex.install( base );
        //初始化
        TinkerManager.installTinker( this );
    }

    @Override
    public void onTerminate() {
        mBaseApplicationManager.exitApp();
        super.onTerminate();
    }
}
