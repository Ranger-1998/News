package com.example.chen.news.view.fragment.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chen.news.presenter.impl.BaseFragmentPresenter;
import com.example.chen.news.utils.ThemeUtil;
import com.example.chen.news.view.fragment.i.IBaseFragment;

/**
 * fragment需要继承的基类,调用onCreateView时必须先调用super
 * Created by liao on 2017/4/21.
 */

public abstract class BaseFragment<P extends BaseFragmentPresenter> extends Fragment implements IBaseFragment {
    protected P mPresenter;
    private Handler handler = new Handler();
    private Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = getPresenter();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 获取Presenter，类型应和Fragment的泛型一致
     *
     * @return Presenter
     */

    protected abstract P getPresenter();

    /**
     * 显示Toast,该方法已实现,子类只需要调用即可,短时的Toast.
     *
     * @param message 要显示的信息
     */

    public void showToast(@NonNull final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                cancelToast();

                if (getActivity() != null) {
                    mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
                    mToast.show();
                }
            }
        });
    }

    private void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 显示一个Snackbar
     * @param view
     * @param str
     * @param backgroundColor
     */
    public void showSnackBar(View view,String str,int backgroundColor)
    {
        Snackbar snackbar= Snackbar.make(view, str, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar,backgroundColor);
        snackbar.show();
    }


    public void setSnackbarColor(Snackbar snackbar, int backgroundColor) {
        View view = snackbar.getView();
        if(view!=null){
            view.setBackgroundColor(backgroundColor);
            view.setAlpha(0.6f);
        }
    }

    /**
     * 获取主题色
     * @return 主题色
     */
    public int getMainColor(Context context){
        int kind = context.getSharedPreferences("theme", context.MODE_PRIVATE).getInt("kind", 0);
        int mainColor = context.getSharedPreferences("theme", context.MODE_PRIVATE).getInt("mainColor",
                ThemeUtil.getDefaultColor());
        if(kind%2==1) {//标题栏为黑色 主题色浅色
            ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else{
            //设置标题栏颜色
            ((Activity)context).getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        return mainColor;
    }
}
