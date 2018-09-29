package com.example.chen.news.network;

import android.support.v4.util.ArrayMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

/**
 * 请求参数的类,只在本包使用,类不用public修饰
 * @see #sendRequest() 这个方法将会拼装系统参数,并发送网络请求
 * Created by chen on 2018/9/15.
 */

class NetworkRequest {

    private ArrayMap<String, String> mRequestParam;     //业务参数,其中包含了一个系统参数"method"
    private String mUrl;
    private Callback mCallback;

    NetworkRequest(ArrayMap<String, String> businessParam, String url, Callback callback) {
        this.mRequestParam = businessParam;
        this.mUrl = url;
        this.mCallback = callback;
    }

    /**
     * 拼装业务参数与系统参数,添加数字签名并发送请求
     */
    void sendRequest() {
        Request request = new Request.Builder().url(mUrl).get().build();   //创建一个请求
        Call call = NetworkManager.getNetWorkManager().getHttpClient().newCall(request);
        call.enqueue(mCallback);    //发送请求
    }

}
