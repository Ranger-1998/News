package com.example.chen.news.network;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

/**
 * Created by chen on 2018/9/15.
 */
public class NetworkInterfaces {
    private static final String SERVER_HOST = "http://v.juhe.cn/toutiao/index?key=e24783a1a0f8cdcb72d365caa356738a&type=";

    public void getNews(String type, Callback callback) {
        Request request = new Request.Builder().url(SERVER_HOST + type).get().build();
        Call call = NetworkManager.getNetWorkManager().getHttpClient().newCall(request);
        call.enqueue(callback);
    }
}
