package com.hechuangwu.baselibrary.helper.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by cwh on 2019/10/3 0003.
 * 功能: 自定义插件加载管理器
 */
public class PluginManager {
    private static PluginManager mInstance;
    private static Context mContext;
    private static File mOptFile;
    private static HashMap<String, PluginInfo> mPluginCacheMap;
    private PluginManager(Context context) {
        mContext = context;
        mOptFile = mContext.getDir("opt", Context.MODE_PRIVATE );
        mPluginCacheMap = new HashMap<>(  );
    }

    public static PluginManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PluginManager.class) {
                if (mInstance == null) {
                    mInstance = new PluginManager(context);
                }
            }
        }
        return mInstance;
    }

    public  PluginInfo loadApk(String apkPath){
        if (mPluginCacheMap.get(apkPath) != null) {
            return mPluginCacheMap.get(apkPath);
        }
        PluginInfo pluginInfo = new PluginInfo();
        pluginInfo.mClassLoader = createPluginDexClassLoader(apkPath);
        pluginInfo.mAssetManager = createPluginAssetManager(apkPath);
        pluginInfo.mResouces = createPluginResources(apkPath);
        mPluginCacheMap.put(apkPath, pluginInfo);
        return pluginInfo;
    }


    /**
     * @param apkPath
     * @return 为插件创建DexClassLoader
     */
    private  DexClassLoader createPluginDexClassLoader(String apkPath) {
        return new DexClassLoader( apkPath, mOptFile.getAbsolutePath(), null, null );
    }

    /**
     * @param apkPath
     * @return 为插件创建AssetManager
     */
    private  AssetManager createPluginAssetManager(String apkPath){
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod( "addAssetPath", String.class );
            addAssetPath.invoke( assetManager,apkPath );
            return assetManager;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * @param apkPath
     * @return 为插件创建Resources
     */
    private  Resources createPluginResources(String apkPath) {
        AssetManager pluginAssetManager = createPluginAssetManager( apkPath );
        Resources appResources = mContext.getResources();
        Resources pluginResources = new Resources( pluginAssetManager, appResources.getDisplayMetrics(), appResources.getConfiguration() );
        return pluginResources;


    }

}
