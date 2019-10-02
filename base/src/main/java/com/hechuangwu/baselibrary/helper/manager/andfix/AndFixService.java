package com.hechuangwu.baselibrary.helper.manager.andfix;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.UserHandle;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by cwh on 2019/10/1 0001.
 * 功能:
 */
public class AndFixService extends Service {
    private static final String TAG = "AndFixService";
    private static final String FILE_END = ".apatch";
    private static final int UPDATE_PATCH = 0x02;
    private static final int DOWNLOAD_PATCH = 0x01;


    private String mPatchFileDir;
    private FixHandler mFixHandler = new FixHandler(this);
    static class FixHandler extends Handler {
        private AndFixService mAndFixService;
        FixHandler(AndFixService service){
            this.mAndFixService = new WeakReference<>( service ).get();
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PATCH:
                    mAndFixService.checkPatchUpdate();
                    break;
                case DOWNLOAD_PATCH:
                    mAndFixService.checkPatchUpdate();
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mPatchFileDir = getExternalCacheDir().getAbsolutePath()+File.separator+"apatch/";
        File file = new File( mPatchFileDir );
        if(!file.exists()){
           file.mkdir();
        }
    }

    //检查是否有patch文件
    private void checkPatchUpdate(){
        mFixHandler.sendEmptyMessage( DOWNLOAD_PATCH );
    }
    //下载patch文件
    private void downloadPath(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFixHandler.sendEmptyMessage( UPDATE_PATCH );
        return super.onStartCommand( intent, flags, startId );
    }




    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return null;
    }
}
