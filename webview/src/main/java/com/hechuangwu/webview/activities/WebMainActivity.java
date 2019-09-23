package com.hechuangwu.webview.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.os.UserHandle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.hechuangwu.baselibrary.base.BaseActivity;
import com.hechuangwu.baselibrary.helper.ARouterConfig;
import com.hechuangwu.baselibrary.helper.Config;
import com.hechuangwu.webview.R;


@Route(path = ARouterConfig.COMPONENT_WEB)
public class WebMainActivity extends BaseActivity {
    private static final String TAG = "WebMainActivity";
    @Autowired(name = Config.WEB_URL)
    String url;

    @Override
    protected int getContentId() {
        return R.layout.activity_web_main;
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }


    @Override
    public synchronized ComponentName startForegroundServiceAsUser(Intent service, UserHandle user) {
        return null;
    }
}
