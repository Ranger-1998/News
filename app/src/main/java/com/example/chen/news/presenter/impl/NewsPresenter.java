package com.example.chen.news.presenter.impl;

import android.os.Handler;
import android.os.Message;

import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.model.impl.NewsModel;
import com.example.chen.news.presenter.i.INewsPresenter;
import com.example.chen.news.view.activity.impl.MainActivity;

/**
 * Created by chen on 2018/9/15.
 */
public class NewsPresenter extends BasePresenter<MainActivity, NewsModel> implements INewsPresenter {
    public NewsPresenter(MainActivity view) {
        super(view);
    }

    @Override
    protected NewsModel getModel(Handler handler) {
        return new NewsModel(handler);
    }

    @Override
    protected void eventReceive(Message msg) {
        switch (msg.what) {
            case NewsEvent.NEWS_TOP:
                mView.getNewsReturn((NewsEvent) msg.obj);
        }
    }

    @Override
    public void getNews(String type) {
        mModel.getNewsByType(type);
    }
}
