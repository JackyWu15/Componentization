package com.hechuangwu.baselibrary.helper.manager.tinker;

import android.content.Context;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * Created by cwh on 2019/10/1 0001.
 * 功能:
 */
public class TinkerManager {

    private static boolean isInstalled = false;//tinker是否已初始化
    private static ApplicationLike mAppLike;

    /**
     *  完成tinker初始化
     * @param applicationLike
     */
    public static  void installTinker(ApplicationLike applicationLike){
        mAppLike = applicationLike;
        if(isInstalled){
            return;
        }

//        TinkerPatchListener tinkerPatchListener = new TinkerPatchListener( getApplicationContext() );
//        LoadReporter loadReporter = new DefaultLoadReporter(getApplicationContext());
//        PatchReporter patchReporter = new DefaultPatchReporter(getApplicationContext());
//        AbstractPatch upgradePatchProcessor = new UpgradePatch();
//        TinkerInstaller.install(mAppLike,loadReporter,
//                patchReporter, tinkerPatchListener,
//                TinkerResultService.class, upgradePatchProcessor);
        TinkerInstaller.install( mAppLike );

        isInstalled = true;
    }

    /**
     * 加载patch文件
     * @param path
     */
    public static void loadPatch(String path){
        if(Tinker.isTinkerInstalled()){
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext() ,path );
        }

    }

    /**
     * 通过ApplicationLike获取Context
     * @return
     */
    private static Context getApplicationContext(){
        if(mAppLike !=null){
            return mAppLike.getApplication().getApplicationContext();
        }
        return null;
    }
}
