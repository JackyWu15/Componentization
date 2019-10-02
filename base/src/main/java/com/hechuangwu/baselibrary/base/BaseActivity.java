package com.hechuangwu.baselibrary.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hechuangwu.baselibrary.bean.MessageEvent;
import com.hechuangwu.baselibrary.helper.ioc.ButterKnife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        initBase();
        initData();
        initEvent();
    }

    private void initBase(){
        setContentView( getContentId());
        ButterKnife.bind( this );
//        ARouter.getInstance().inject( this );
        if(registerEvent())
            EventBus.getDefault().register( this );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(registerEvent())EventBus.getDefault().unregister( this );
    }

    /**
     * 子类接受事件 重写该方法
     */
    protected boolean registerEvent(){
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageEvent event) {

    }



    protected abstract int getContentId();
    protected abstract void initData();
    protected abstract void initEvent();


}
