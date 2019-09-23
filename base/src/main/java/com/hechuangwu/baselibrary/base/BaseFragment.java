package com.hechuangwu.baselibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hechuangwu.baselibrary.bean.MessageEvent;
import com.hechuangwu.baselibrary.helper.ioc.ButterKnife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by cwh on 2019/9/23 0023.
 * 功能:
 */
public abstract class BaseFragment extends Fragment {
    Context mContext;
    /**
     * 创建fragment的静态方法，方便传递参数
     * @param args 传递的参数
     * @return
     */
    public static <T extends Fragment> T getInstance(Class clazz, Bundle args) {
        T mFragment = null;
        try {
            mFragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (args != null) {
            mFragment.setArguments(args);
        }
        return mFragment;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext= context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate( getContentId(), container, false );
        initBase( contentView );
        initView();
        initData();
        initEvent();
        return contentView;

    }
    private void initBase(View contentView){
        ButterKnife.bind( this,contentView );
        ARouter.getInstance().inject( this );
        if(registerEvent())EventBus.getDefault().register( this );
    }


    protected boolean registerEvent(){
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBus(MessageEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registerEvent()) {
            EventBus.getDefault().unregister(this);
        }
    }
    protected abstract int getContentId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initEvent();
}
