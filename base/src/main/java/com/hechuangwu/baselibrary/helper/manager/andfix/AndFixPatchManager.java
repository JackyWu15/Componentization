package com.hechuangwu.baselibrary.helper.manager.andfix;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.alipay.euler.andfix.patch.PatchManager;
import com.hechuangwu.baselibrary.helper.util.Utils;

import java.io.IOException;

/**
 * Created by cwh on 2019/10/1 0001.
 * 功能:
 */
public class AndFixPatchManager {
    private static final String TAG = "AndFixPatchManager";
    private static AndFixPatchManager andFixPatchManager;
    private PatchManager mPatchManager;
    private AndFixPatchManager() {}

    public static AndFixPatchManager getInstance(){
        if(andFixPatchManager==null){
            synchronized (AndFixPatchManager.class){
                if(andFixPatchManager==null){
                    andFixPatchManager = new AndFixPatchManager();
                }
            }
        }
        return andFixPatchManager;
    }


    public void initPatch(Context context){
        mPatchManager = new PatchManager( context );
        mPatchManager.init( Utils.getVersionName( context ) );
        mPatchManager.loadPatch();
    }

    public void addPatch(String path){
        if(mPatchManager!=null){
            try {
                mPatchManager.addPatch( path );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void classLoad(Activity activity) {
        Log.i( TAG, "Context的类加载加载器:" + Context.class.getClassLoader() );
        Log.i( TAG, "ListView的类加载器:" + ListView.class.getClassLoader() );
        Log.i( TAG, "应用程序默认加载器:" + activity.getClassLoader() );
        Log.i( TAG, "系统类加载器:" + ClassLoader.getSystemClassLoader() );
        Log.i( TAG, "系统类加载器和Context的类加载器是否相等:" + (Context.class.getClassLoader() == ClassLoader.getSystemClassLoader()) );
        Log.i( TAG, "系统类加载器和应用程序默认加载器是否相等:" + (activity.getClassLoader() == ClassLoader.getSystemClassLoader()) );

        Log.i( TAG, "打印应用程序默认加载器的委派机制:" );
        ClassLoader classLoader = activity.getClassLoader();
        while (classLoader != null) {
            Log.i( TAG, "类加载器:" + classLoader );
            classLoader = classLoader.getParent();
        }

        Log.i( TAG, "打印系统加载器的委派机制:" );
        classLoader = ClassLoader.getSystemClassLoader();
        while (classLoader != null) {
            Log.i( TAG, "类加载器:" + classLoader );
            classLoader = classLoader.getParent();
        }

        Log.i( TAG, "打印BootClassLoader的委派机制:" );
        classLoader = Context.class.getClassLoader();
        while (classLoader != null) {
            Log.i( TAG, "类加载器:" + classLoader );
            classLoader = classLoader.getParent();
        }
    }
}
