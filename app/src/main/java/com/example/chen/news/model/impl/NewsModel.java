package com.example.chen.news.model.impl;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.chen.news.model.entity.NewsBean;
import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.model.i.INewsModel;
import com.example.chen.news.network.NetworkInterfaces;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by chen on 2018/9/15.
 */
public class NewsModel extends BaseModel implements INewsModel {

    public NewsModel(Handler handler) {
        super(handler);
    }

    @Override
    public void getNewsByType(String type) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.body() != null) {
                    NewsBean newsBean = gson.fromJson(response.body().toString(), NewsBean.class);
                    NewsEvent event = new NewsEvent();
                    event.setWhat(1);
                    event.setNewsBean(newsBean);
                    postEvent(event);
                }
            }
        };
    }

    @Override
    protected NetworkInterfaces getNetworkInterface() {
        return new NetworkInterfaces();
    }
}
