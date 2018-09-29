package com.example.chen.news.presenter.impl;

import android.os.Handler;
import android.os.Message;

import com.example.chen.news.model.i.IBaseModel;
import com.example.chen.news.model.impl.BaseModel;
import com.example.chen.news.view.activity.i.IBaseActivity;
import com.example.chen.news.view.activity.impl.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * 所有Presenter的父类
 * 对持有的Activity和Model的类型做了严格的限制!
 * 泛型<V extends BaseActivity & IBaseActivity>是其持有的Activity对象类型
 * 所持有的Activity都应该继承BaseActivity
 * 所持有的Activity都应该实现IBaseActivity 的子接口,以便于Presenter通过接口回调Activity的方法
 * 泛型<M extends BaseModel & IBaseModel>是其持有的Model对象
 * 所持有的的Model对象都应该继承BaseModel
 * 所持有的Model都应该实现IBaseModel的子接口,以便于Presenter通过接口调用Model的方法
 *
 * Created by chen on 2018/9/15.
 */

public abstract class BasePresenter
        <V extends BaseActivity & IBaseActivity, M extends BaseModel & IBaseModel> {

    V mView;    //Presenter所持有的Activity对象
    M mModel;   //Presenter所持有的Model对象

    public BasePresenter(V view) {
        mView = view;
        mModel = getModel(getHandler());    //model需要持有handel,用于给主线程发送消息
    }

    /**
     * 获取Model对象,Model对象需要持有一个handel
     *
     * @param handler model持有的handel
     * @return model 对象
     */
    protected abstract M getModel(Handler handler);

    /**
     * 获取handel对象,需要持有当前presenter的弱引用
     *
     * @return 主线程的handel
     */
    private Handler getHandler() {
        return new MainHandler(this);
    }

    /**
     * 将Activity对象置空,在Activity销毁时应该调用该方法,以便于JVM回收
     * 注意!在置空后不能再调用Activity的方法,否则会抛出NullPointerException
     */
    public void onDestroy() {
        mView = null;
        if (mModel != null) {
            mModel.onDestroy();
            mModel = null;
        }
    }

    /**
     * 这个方法处理网络请求成功后从model层发送过来的事件
     * 子类在复写此方法时,从msg获取的obj请转换为对应的model中发送的类型
     * 在对应的Activity销毁后在调用这个方法会抛出NullPointerException,在调用这个方法的位置catch异常!!!
     * 异常发生原因:这个方法会使用持有的activity来调用activity的方法,当activity销毁后再调用activity的方法就
     * 会发生异常.将异常从这个函数抛出,在调用这个函数的地方catch.至于为什么不在异常发生的时候就catch,原因如下:
     * 1.如果要在发生的时候就catch需要在每个子类写try-catch代码块,比较繁琐;
     * 2.这个方法最主要的功能就是调用activity的方法,当activity销毁后这个方法就没有继续执行的意义,所以直接从
     * 这个方法抛出异常,没有必要再执行这个方法抛出异常之后的代码
     *
     * @param msg 消息,mes.what用于区分是哪个网络请求返回了,msg.obj包含了网络请求返回的数据
     */
    protected abstract void eventReceive(Message msg);

    /**
     * 用于获取主线程的handler
     * 这个内部类必须是静态的,不能让这个类持有外部类的引用,否则可能会造成内存泄漏
     * 使用弱引用来关联外部类,当只有弱应用存在时，变量会被回收，所以,请注意空指针!!!
     *
     * @see #eventReceive(Message) 调用的这个方法可能会抛出NullPointerException,在这里处理掉,不要让没有
     * 必要的异常使程序崩溃
     */
    private static class MainHandler extends Handler {

        private WeakReference<BasePresenter> presenter;     //弱引用

        MainHandler(BasePresenter presenter) {
            this.presenter = new WeakReference<>(presenter);
        }

        @Override
        public void handleMessage(Message msg) {
            if (presenter.get() != null) {
                try {                                           //在这里catch NullPointerException
                    if (!presenter.get().mView.isFinishing()) { //activity没有正在被finish才回调事件
                        presenter.get().eventReceive(msg);      //接收到事件后的操作,可能会抛出NullPointerException
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        }
    }
}
