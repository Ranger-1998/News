package com.example.chen.news.model.impl;

import android.os.Handler;
import android.os.Message;

import com.example.chen.news.model.event.BaseEvent;
import com.example.chen.news.network.NetworkInterfaces;

/**
 * model的基类
 * 所有的model都应该继承这个基类,基类中提供了必要的方法
 * 并且应该实现{@link com.example.chen.news.model.i.IBaseModel}接口的子接口
 * presenter通过IBaseModel的子接口调用model中的方法以获取网络数据
 * model中持有一个主线程的Handler,在异步的网络请求返回时,通过这个Handler给主线程发送消息
 *
 * @see #postEvent(BaseEvent) 子线程调用这个方法给主线程发送消息,需要一个BaseEvent子类的对象,这个对象包
 * 含了message所需的what,且整个对象作为Message的obj进行传递
 * @see #getNetworkInterface() 这个方法返回一个网络接口类的对象,model通过这个对象调用网络层进行网络请求
 *
 * Created by chen on 2018/9/15.
 */

public abstract class BaseModel {

    NetworkInterfaces mNetworkInterface;   //网络接口
    private Handler mHandler;             //主线程的Handler

    public BaseModel(Handler handler) {
        this.mHandler = handler;
        this.mNetworkInterface = getNetworkInterface();
    }

    /**
     * 这个方法给主线程发送消息
     * 这个方法必须同步,在通常情况下,Activity对应的Model只有一个,所以也只有一个postEvent方法,但是可能会有
     * 多个线程同时调用这个方法给主线程发送消息,为了避免数据出现错乱,这个方法必须保持同步
     *
     * @param event 这个对象包含了message所需的what和obj
     */
    synchronized void postEvent(BaseEvent event) {
        if (mHandler != null) {
            Message message = mHandler.obtainMessage();
            message.what = event.getWhat();
            message.obj = event;
            mHandler.sendMessage(message);
        }
    }

    /**
     * 获取网络接口,已在构造函数调用,只需要复写,不需要调用
     *
     * @return 网络接口
     */
    protected abstract NetworkInterfaces getNetworkInterface();

    /**
     * 释放持有的Handler,在activity onDestroy之后,通过presenter调用
     */
    public void onDestroy() {
        mHandler = null;
    }

}
