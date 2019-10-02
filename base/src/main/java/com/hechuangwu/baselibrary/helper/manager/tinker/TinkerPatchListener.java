package com.hechuangwu.baselibrary.helper.manager.tinker;

import android.content.Context;

import com.tencent.tinker.lib.listener.DefaultPatchListener;

/**
 * Created by cwh on 2019/10/2 0002.
 * 功能: 1校验patch文件是否合法 2.启动Service去安装patch文件
 */
public class TinkerPatchListener extends DefaultPatchListener {
    public TinkerPatchListener(Context context) {
        super( context );
    }

    @Override
    protected int patchCheck(String path,String patchMd5) {


        return super.patchCheck(path,patchMd5);
    }
}
