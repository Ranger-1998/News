package com.example.chen.news.model.impl;

import android.support.annotation.NonNull;

import com.example.chen.news.model.entity.NewsBean;
import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.model.i.INewsFragmentModel;
import com.example.chen.news.network.NetworkInterfaces;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by chen on 2018/9/16.
 */
public class NewsFragmentModel extends BaseFragmentModel<NewsEvent> implements INewsFragmentModel {

    public NewsFragmentModel(OnEventReceiveListener<NewsEvent> eventReceiveListener) {
        super(eventReceiveListener);
    }

    @Override
    protected NetworkInterfaces getNetworkInterface() {
        return new NetworkInterfaces();
    }

    @Override
    public void getNewsByType(String type) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                Gson gson = new Gson();
                if (response.body() != null) {
                    NewsBean newsBean = gson.fromJson(response.body().string(), NewsBean.class);
                    NewsEvent event = new NewsEvent();
                    event.setNewsBean(newsBean);
                    event.setWhat(1);
                    postEvent(event);
                }
            }
        };
        mNetworkInterface.getNews(type, callback);
    }
}
