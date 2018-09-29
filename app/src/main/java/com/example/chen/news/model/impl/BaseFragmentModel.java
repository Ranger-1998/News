package com.example.chen.news.model.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.example.chen.news.model.event.BaseEvent;
import com.example.chen.news.network.NetworkInterfaces;
import com.example.chen.news.presenter.impl.BaseFragmentPresenter;

import java.lang.ref.WeakReference;

/**
 * 所有fragment的model需要继承的父类
 * 该类通过OnEventReceiveListener接口回调发送消息，与presenter和view层进行消息传递
 *
 * Created by chen on 2018/9/16.
 */

public abstract class BaseFragmentModel<T extends BaseEvent> {
    NetworkInterfaces mNetworkInterface;
    private OnEventReceiveListener<T> mEventReceiveListener;

    public BaseFragmentModel(OnEventReceiveListener<T> eventReceiveListener) {
        mNetworkInterface = getNetworkInterface();
        this.mEventReceiveListener = eventReceiveListener;
    }

    /**
     * 回调接口传递消息
     *
     * @param event 需要传递的消息
     */
    void postEvent(T event) {
        mEventReceiveListener.eventReceive(event, event.getWhat());
    }

    /**
     * 获取网络接口,已在构造函数调用,只需要复写,不需要调用
     *
     * @return 网络接口
     */
    protected abstract NetworkInterfaces getNetworkInterface();

    public interface OnEventReceiveListener<Y extends BaseEvent> {
        public void eventReceive(Y event, int what);
    }
}
