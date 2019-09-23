package com.hechuangwu.baselibrary.bean;


/**
 * Describe：EventBus事件类
 * Created by 吴天强 on 2018/10/22.
 */



public class MessageEvent<T> {

    private String action;
    private T data;


    public MessageEvent() {
    }

    public MessageEvent(String action) {
        this.action = action;
    }


    public MessageEvent(String action, T data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
