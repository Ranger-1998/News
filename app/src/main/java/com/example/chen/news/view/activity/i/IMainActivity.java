package com.example.chen.news.view.activity.i;

import com.example.chen.news.model.event.NewsEvent;

/**
 * Created by chen on 2018/9/15.
 */
public interface IMainActivity extends IBaseActivity{
    public void getNewsReturn(NewsEvent event);
}
