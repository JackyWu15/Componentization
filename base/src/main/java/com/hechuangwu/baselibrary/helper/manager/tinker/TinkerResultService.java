package com.hechuangwu.baselibrary.helper.manager.tinker;

import com.tencent.tinker.lib.service.DefaultTinkerResultService;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.TinkerServiceInternals;

import java.io.File;

/**
 * Created by cwh on 2019/10/2 0002.
 * 功能:patch安装完以后，默认实现是杀进程，可以根据业务需求改掉
 */
public class TinkerResultService extends DefaultTinkerResultService {
    private static final String TAG = "TinkerResultService";

    //最终安装结果回调
    @Override
    public void onPatchResult(PatchResult result) {
        if(result==null){
            TinkerLog.e(TAG, "DefaultTinkerResultService received null result!!!!");
            return;
        }
        TinkerLog.i(TAG, "DefaultTinkerResultService received a result:%s ", result.toString());
        //先杀服务
        TinkerServiceInternals.killTinkerPatchServiceProcess(getApplicationContext());

        if (result.isSuccess) {
            deleteRawPatchFile(new File(result.rawPatchFilePath));
            if (checkIfNeedKill(result)) {
                //安装成功后，默认杀死了进程
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                TinkerLog.i(TAG, "I have already install the newly patch version!");
            }
        }
    }
}
