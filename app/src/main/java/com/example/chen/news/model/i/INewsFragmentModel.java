package com.example.chen.news.model.i;

/**
 * Created by chen on 2018/9/16.
 */
public interface INewsFragmentModel extends IBaseFragmentModel {
    void getNewsByType(String type);
}
