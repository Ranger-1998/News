package com.example.chen.news.presenter.impl;

import android.app.Activity;
import android.os.Message;
import android.support.annotation.NonNull;

import com.example.chen.news.model.event.BaseEvent;
import com.example.chen.news.model.i.IBaseFragmentModel;
import com.example.chen.news.model.impl.BaseFragmentModel;
import com.example.chen.news.view.fragment.i.IBaseFragment;
import com.example.chen.news.view.fragment.impl.BaseFragment;

/**
 * 所有Fragment对应的Presenter的父类
 * 对持有的Fragment和Model的类型做了严格的限制!
 * 泛型<V extends FragmentBase & IBaseFragment>是其持有的Fragment对象类型
 * 所持有的Fragment都应该继承BaseFragment
 * 所持有的Fragment都应该实现IBaseFragment 的子接口,以便于Presenter通过接口回调Fragment的方法
 * 泛型<M extends BaseFragmentModel & IBaseFragmentModel>是其持有的Model对象
 * 所持有的的Model对象都应该继承BaseFragmentModel
 * 所持有的Model都应该实现IBaseFragmentModel的子接口,以便于Presenter通过接口调用Model的方法
 *
 * Created by liao on 2017/4/21.
 */

public abstract class BaseFragmentPresenter<V extends BaseFragment & IBaseFragment,
        M extends BaseFragmentModel & IBaseFragmentModel, E extends BaseEvent>
        implements BaseFragmentModel.OnEventReceiveListener<E> {

    V mView;    //Presenter所持有的Activity对象
    M mModel;   //Presenter所持有的Model对象

    public BaseFragmentPresenter(V view) {
        mView = view;
        mModel = getModel(this);    //model需要持有消息回调接口,用于给主线程发送消息
    }

    /**
     * 获取相关联的model
     *
     * @return model
     */
    protected abstract M getModel(@NonNull BaseFragmentModel.OnEventReceiveListener<E>
                                          eventReceiveListener);

    /**
     * 将View添加到Presenter中，Presenter即可操作View
     *
     * @param view 一般为继承接口IBaseView的Activity或Fragment
     */
    public void setView(@NonNull V view) {
        this.mView = view;
    }


    /**
     * 接收处理消息
     * @param event 传递过来的event，自己做处理
     * @param what 网络请求是否失败的消息码
     */
    @Override
    public abstract void eventReceive(E event, int what);
}
