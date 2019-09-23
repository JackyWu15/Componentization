package com.hechuangwu.baselibrary.helper.ioc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by cwh on 2019/9/19 0019.
 * 功能:
 */
public class ButterKnife {
    private static final String TAG = "ButterKnife";
    public static void bind(AppCompatActivity activity){
        bindView(new ViewFinder(activity),activity);
    }

    public static void bind(View view){
        bindView(new ViewFinder(view),view);
    }

    public static void bind(Object object,View view){
        bindView(new ViewFinder(view),object);
    }

    public static void bindView(ViewFinder viewFinder,Object object){
        bindFiled(viewFinder,object);
        bindEvent(viewFinder,object);
    }

    private static void bindFiled(ViewFinder viewFinder, Object object) {
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for(Field field:declaredFields){
            //获取有BindView注解的属性
            BindView annotation = field.getAnnotation(BindView.class);
            if(annotation!=null){
                //找到viewId
                int valueId = annotation.value();
                View view = viewFinder.findViewById(valueId);
                if(view!=null){
                    try {
                        field.setAccessible(true);
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private static void bindEvent(ViewFinder viewFinder, Object object) {
        Class<?> aClass = object.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for(Method method:declaredMethods){
            OnClick onClick = method.getAnnotation(OnClick.class);

            if(onClick!=null){
                int[] value = onClick.value();
                for(int viewId:value){
                    View view = viewFinder.findViewById(viewId);
                    OnCheckNet onCheckNet = method.getAnnotation(OnCheckNet.class);
                    boolean isOnNet = true;
                    if(onCheckNet!=null){
                        isOnNet = isNetWorkAvailable(view.getContext());
                    }
                    if(view!=null){
                        //实现点击事件
                        view.setOnClickListener(new DeclaredOnClickListener(object,method,isOnNet));
                    }
                }
            }

        }


    }

    private static class DeclaredOnClickListener implements View.OnClickListener {
        private Method method;
        private Object object;
        private boolean isOnNet;
        public DeclaredOnClickListener(Object object, Method method, boolean isOnNet) {
            this.object = object;
            this.method = method;
            this.isOnNet = isOnNet;
        }

        @Override
        public void onClick(View v) {
            if(!isOnNet){
                Toast.makeText(v.getContext(),"网络连接异常",Toast.LENGTH_LONG).show();
                return;
            }

            try {
                method.setAccessible(true);
                method.invoke(object,v);//点击时将view传入
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNetWorkAvailable(Context context){
        boolean isAvailable = false ;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isAvailable()){
            isAvailable = true;
        }
        return isAvailable;
    }
}
