package com.hechuangwu.componentization;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.hechuangwu.baselibrary.base.BaseActivity;
import com.hechuangwu.baselibrary.base.BaseApplication;
import com.hechuangwu.baselibrary.helper.ARouterConfig;
import com.hechuangwu.baselibrary.helper.Config;
import com.hechuangwu.baselibrary.helper.ioc.BindView;
import com.hechuangwu.baselibrary.helper.ioc.OnCheckNet;
import com.hechuangwu.baselibrary.helper.ioc.OnClick;
import com.hechuangwu.baselibrary.helper.jpeg.ImageCompress;
import com.hechuangwu.baselibrary.helper.manager.ExceptionCrashManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


@Route( path = ARouterConfig.MODULE_APP)
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_text)
    TextView tv_text;


    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initData() {
        tv_text.setText( "反射注解" );

//        classLoad();



    }
    @OnClick({R.id.tv_text, R.id.bt_click,R.id.bt_router})
    @OnCheckNet
    private void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_text:
                //                int a = 2 / 0;
                Toast.makeText( this, "点击", Toast.LENGTH_LONG ).show();
                break;
            case R.id.bt_click:
                compress();
                break;
            case R.id.bt_router:
                ARouter.getInstance().build( ARouterConfig.COMPONENT_WEB )
                        .withString( Config.WEB_URL,"https://www.baidu.com/" )
                        .navigation();
                break;
        }
    }

    @Override
    protected void initEvent() {

    }

    private void compress() {
        CompressAsyncTask compressAsyncTask = new CompressAsyncTask();
        compressAsyncTask.execute(  );
    }


    static class CompressAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            String srcPath = Environment.getExternalStorageDirectory() + File.separator + "src.jpg";
            String dstPath_hf = Environment.getExternalStorageDirectory() + File.separator + "compress_hf.jpg";
            String dstPath = Environment.getExternalStorageDirectory() + File.separator + "compress_raw.jpg";
            File file = new File( srcPath );
            Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
            ImageCompress.nativeCompressBitmap( bitmap, 20, dstPath_hf, false );

            //原生压缩
            try {
                bitmap.compress( Bitmap.CompressFormat.JPEG, 20, new FileOutputStream( dstPath ) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText( BaseApplication.getApplication(), "压缩完成", Toast.LENGTH_LONG ).show();
        }

    }


    //上次闪退bug
    private void commitCrashLog(){
        File crashFile = ExceptionCrashManager.getInstance().getCrashFile();
        if (crashFile.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( crashFile ), "UTF-8" ) );
                String crashLog = "";
                while ((crashLog = bufferedReader.readLine()) != null) {
                    Log.i( TAG, "initData:>>>" + crashLog );
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    private void classLoad() {
        Log.i( TAG, "Context的类加载加载器:" + Context.class.getClassLoader() );
        Log.i( TAG, "ListView的类加载器:" + ListView.class.getClassLoader() );
        Log.i( TAG, "应用程序默认加载器:" + getClassLoader() );
        Log.i( TAG, "系统类加载器:" + ClassLoader.getSystemClassLoader() );
        Log.i( TAG, "系统类加载器和Context的类加载器是否相等:" + (Context.class.getClassLoader() == ClassLoader.getSystemClassLoader()) );
        Log.i( TAG, "系统类加载器和应用程序默认加载器是否相等:" + (getClassLoader() == ClassLoader.getSystemClassLoader()) );

        Log.i( TAG, "打印应用程序默认加载器的委派机制:" );
        ClassLoader classLoader = getClassLoader();
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


    @Override
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return null;
    }
}
