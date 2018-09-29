package com.example.chen.news.presenter.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.model.impl.BaseFragmentModel;
import com.example.chen.news.model.impl.NewsFragmentModel;
import com.example.chen.news.presenter.i.INewsFragmentPresenter;
import com.example.chen.news.view.fragment.impl.BaseFragment;
import com.example.chen.news.view.fragment.impl.NewsFragment;

import java.util.logging.LogRecord;

/**
 * Created by chen on 2018/9/16.
 */
public class NewsFragmentPresenter extends BaseFragmentPresenter<NewsFragment, NewsFragmentModel, NewsEvent> implements INewsFragmentPresenter {


    public NewsFragmentPresenter(NewsFragment view) {
        super(view);
    }

    @Override
    protected NewsFragmentModel getModel(@NonNull BaseFragmentModel.OnEventReceiveListener<NewsEvent> eventReceiveListener) {
        return new NewsFragmentModel(eventReceiveListener);
    }

    @Override
    public void eventReceive(final NewsEvent event, int what) {
        //切换到主线程
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mView.getNewsReturn(event.getNewsBean());
            }
        });
    }

    @Override
    public void getNewsByType(String type) {
        mModel.getNewsByType(type);
    }
}
