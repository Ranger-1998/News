package com.example.chen.news.model.event;

import java.util.Observable;

/**
 * 网络请求成功后给主线程发送的消息,通过Handler发送
 *
 * @see #mWhat 传递给Handler作为what.通过这个变量标识是哪个变量请求成功返回,这个值强烈建议在整个程序唯一,并
 * 从100开始编码,100以内的值保留.
 * 请求失败时,通过这个变量标识是在进行哪个网络请求的时候失败.强烈建议与对应请求成功的状态码互补
 * *
 * Created by chen on 2018/9/15.
 */

public class BaseEvent extends Observable{

    private int mWhat;  //事件的标识

    public int getWhat() {
        return mWhat;
    }

    public void setWhat(int mWhat) {
        this.mWhat = mWhat;
    }

}
