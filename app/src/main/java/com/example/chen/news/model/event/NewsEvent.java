package com.example.chen.news.model.event;

import com.example.chen.news.model.entity.NewsBean;

/**
 * Created by chen on 2018/9/15.
 */
public class NewsEvent extends BaseEvent {
    public static final int NEWS_TOP = 1;
    public static final int NEWS_SOCIETY= 2;
    public static final int NEWS_DOMESTIC = 3;
    public static final int NEWS_INTERNATIONAL = 4;

    private NewsBean newsBean;

    public void setNewsBean (NewsBean newsBean) {
        this.newsBean = newsBean;
    }

    public NewsBean getNewsBean () {
        return newsBean;
    }
}
