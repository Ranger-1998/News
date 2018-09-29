package com.example.chen.news.view.activity.i;

/**
 * Activity需要实现的接口的父接口
 * Presenter通过Activity实现的接口来回调
 * *
 * Created by chen on 2018/9/15.
 */

public interface IBaseActivity {

    /**
     * Activity在进行网络请求时如果未连接到网络Presenter会通过这个接口回调
     * @param code  用于区分Activity在调用哪个接口时由于没有成功连接到服务器而回调
     */
    void failBecauseNotNetworkReturn(int code);
}
