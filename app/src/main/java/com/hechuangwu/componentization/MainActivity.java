package com.hechuangwu.componentization;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Environment;
import android.os.UserHandle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hechuangwu.baselibrary.base.BaseActivity;
import com.hechuangwu.baselibrary.helper.ARouterConfig;
import com.hechuangwu.baselibrary.helper.Config;
import com.hechuangwu.baselibrary.helper.ioc.BindView;
import com.hechuangwu.baselibrary.helper.ioc.OnCheckNet;
import com.hechuangwu.baselibrary.helper.ioc.OnClick;
import com.hechuangwu.baselibrary.helper.jpeg.ImageCompress;
import com.hechuangwu.baselibrary.helper.manager.ExceptionCrashManager;
import com.hechuangwu.baselibrary.helper.manager.andfix.AndFixPatchManager;
import com.hechuangwu.baselibrary.helper.manager.tinker.TinkerManager;
import com.hechuangwu.baselibrary.helper.plugin.PluginInfo;
import com.hechuangwu.baselibrary.helper.plugin.PluginManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Route( path = ARouterConfig.MODULE_APP)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    //图片原文件
    private String srcPath = Environment.getExternalStorageDirectory() + File.separator + "src.jpg";
    //图片压缩文件
    private String dstPathHF = Environment.getExternalStorageDirectory() + File.separator + "compress_hf.jpg";
    //andFix修复文件
    private String andFixPath = Environment.getExternalStorageDirectory() + File.separator + "fix.apatch";
    //Tinker修复文件
    private String tinkerFixPath = Environment.getExternalStorageDirectory() + File.separator + "patch_signed.apk";
    //插件文件
    private String bundlePath = Environment.getExternalStorageDirectory() + File.separator + "bundle.apk";


    @BindView(R.id.tv_reflect)
    TextView mTvReflect;


    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initData() {
        mTvReflect.setText( "反射注解" );
    }

    @Override
    protected void initEvent() {
        //类加载器打印
        AndFixPatchManager.getInstance().classLoad( this );
////        上次闪退日志
        ExceptionCrashManager.getInstance().getCrashFile();

        //模拟闪退
        findViewById( R.id.bt_crash ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( MainActivity.this, "有bug", Toast.LENGTH_LONG ).show();
            }
        } );

    }


    @OnClick({R.id.tv_reflect, R.id.bt_compress,R.id.bt_router,R.id.bt_fix,R.id.bt_plugin})
    @OnCheckNet
    private void onClick(View view) {
        switch (view.getId()){
            //反射注解
            case R.id.tv_reflect:
                Toast.makeText( this, "点击", Toast.LENGTH_LONG ).show();
                break;
            //图片压缩
            case R.id.bt_compress:
                ImageCompress.compress(srcPath,dstPathHF);
                break;
            //ARouter跳转
            case R.id.bt_router:
                ARouter.getInstance().build( ARouterConfig.COMPONENT_WEB )
                        .withString( Config.WEB_URL,"https://www.baidu.com/" )
                        .navigation();
                break;
            //修复bug
            case R.id.bt_fix:
               tinker();
                break;
            case R.id.bt_plugin:
                loadApk( );
                break;
        }
    }

    //AndFix修复
    private void andFix(){
        AndFixPatchManager.getInstance().addPatch( andFixPath);
    }
    //Tinker修复
    private void tinker(){
        TinkerManager.loadPatch( tinkerFixPath );
    }
    //插件加载
    private void loadApk(){
        PluginInfo pluginInfo = PluginManager.getInstance( getApplicationContext() ).loadApk( bundlePath );
        try {
            Class<?> aClass = pluginInfo.mClassLoader.loadClass( "com.hechuangwu.bundle.BundleTest" );
            if(aClass!=null){
                Object instance = aClass.newInstance();
                Method method = aClass.getMethod( "test" );
                method.invoke( instance );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }








    @Override
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return null;
    }
}
