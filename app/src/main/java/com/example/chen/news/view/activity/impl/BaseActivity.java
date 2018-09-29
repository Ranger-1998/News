package com.example.chen.news.view.activity.impl;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chen.news.presenter.i.IBasePresenter;
import com.example.chen.news.presenter.impl.BasePresenter;
import com.example.chen.news.utils.ThemeUtil;
import com.example.chen.news.view.activity.ActivityManager;
import com.example.chen.news.view.activity.StatusBarManager;


/**
 * Activity的父类
 * 只能以standard或singleTask模式启动activity!否则activity管理类会混乱
 * *
 * 对与之绑定的Presenter对象类型做出严格的限制!
 * 泛型<P extends BasePresenter & IBasePresenter>是Activity对应的Presenter的类型
 * Presenter都继承于BasePresenter,在Activity的onDestroy()方法会调用BasePresenter的onDestroy()方法
 * 以此来销毁Presenter所持有的Activity对象
 * Presenter都应该实现的IBasePresenter接口的子接口,Activity通过接口来调用Presenter中的方法来获取数据
 *
 * @see #onDestroy() 可能会导致Presenter抛出NullPointerException,
 * 请在Presenter中调用Actitivy方法时确定Activity不为null
 * *
 * @see #isFinish 关于这个变量的说明:
 * 工程自建了一个Activity栈用于维护Activity.便于随时在任何类中获取Activity,这个栈应该与系统的栈保持同步,即在
 * Activity创建时将其入栈,在Activity在销毁时将其出栈.在创建时入栈只需要在onCreate()将其入栈即可.但是在销毁时
 * 出栈会出现一些问题:Activity在执行finish()后并不会即时的调用onDestroy(),所以为了保证实时与系统的栈同步,最
 * 好是在finish()中出栈.但是当一个启动过的activity以singleTask模式再次启动时,在它第一次启动之后的activity
 * 会被结束,但是系统并不是调用finish()来结束(至于是怎么结束的,我现在也不知道,如果后人知道可以在这里优化),如果只
 * 在finish()中将自己栈中的Activity出栈,在这种情况下被结束的activity就不会被出栈,自己的栈就与系统的栈不同步了.
 * 但是系统在结束activity时,正常情况下会调用onDestroy().所以可以在onDestroy()中判断这个activity有没有被调用
 * finish,如果被调用,那么说明该activity已经被出栈,如果没有调用,说明该activity没有被出栈,那么在onDestroy()将
 * 它出栈.(可以优化的条件:有办法知道一个activity正在被销毁(即从开始销毁到完成销毁的这段时间)||知道activity在以
 * singleTask模式启动时,在它之后的activiy是怎么结束掉的)
 * *
 * Created by chen on 2018/9/15.
 */

public abstract class BaseActivity<T extends BasePresenter & IBasePresenter>
        extends AppCompatActivity {

    protected T mPresenter;     //Activity所持有的Presenter对象
    private boolean isFinish;   //Activity被finish的标志,注意:被finish不一定立即destroy
    private Toast mToast;       //toast对象

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFinish = false;                                           //没有执行finish()
        ActivityManager.getActivityManager().pushActivity(this);    //将启动的activity压入栈中
        mPresenter = getPresenter();                                //获取对应的presenter对象

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //设置为强制竖屏
    }

    @Override
    public void finish() {
        //已经被finish()的activity在第一次finish就被出栈,所以不能再次出栈
        if (!isFinishing()) {
            isFinish = true;                                            //调用了finish()
            ActivityManager.getActivityManager().popActivity();         //将销毁的activity出栈
        }
            super.finish();
        }

        /**
         * 在退出Activity后,子线程的操作可能还在执行,有可能会导致内存泄漏
         * 在退出Activity时,销毁对应Presenter所持有的View
         * 注意!在销毁后,presenter调用Activity的方法会抛出NullPointerException,需要在Presenter中做处理
         */
        @Override
        protected void onDestroy () {
            if (mPresenter != null) {
                mPresenter.onDestroy();
                mPresenter = null;
            }
            //如果Activity没有被调用finish(),但是被调用了onDestroy(),那么将它出栈
            //如果被调用了finish(),则在finish()中就已经出栈,不需要再次出栈
            if (!isFinish) {
                ActivityManager.getActivityManager().popActivity();     //将销毁的activity出栈
            }
            super.onDestroy();
        }

        /**
         * 显示Toast,该方法已实现,子类只需要调用即可,短时的Toast.
         *打断之前的toast，在显示短时的toast
         * @param message 要显示的信息
         */

    public void showToast(@NonNull String message) {
        if (mToast == null){
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 显示Toast,该方法已实现,子类只需要调用即可,长时的Toast.
     *打断之前的toast，在显示短时的toast
     * @param message 要显示的信息
     */

    public void showToastLong(@NonNull String message) {
        if (mToast == null){
            mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        }else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    /**
     * 设置状态栏透明,适用于非DrawerLayout
     */
    public void setStatusBarTransparent() {
        new StatusBarManager(this).setStatusBarTransparent();
    }

    /**
     * 设置状态栏透明,适用于DrawerLayout
     *
     * @param drawerLayout drawerLayout
     */
    public void
    setStatusBarTransparent(DrawerLayout drawerLayout) {
        new StatusBarManager(this).setStatusBarTransparentForDrawerLayout(drawerLayout);
    }

    /**
     * 设置沉浸式状态栏(状态栏与Toolbar颜色相同)
     * 适用于非DrawerLayout
     */
    public void setStatusBarImmerse() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getMainColor()));
        }
        new StatusBarManager(this).setStatusBarColor(getMainColor());
    }

    /**
     * 设置沉浸式状态栏(状态栏与Toolbar颜色相同)
     * 适用于非DrawerLayout
     * 需自己传入颜色
     */
    public void setStatusBarImmerse(int resourseId) {
        new StatusBarManager(this)
                .setStatusBarColor(ContextCompat.getColor(this, resourseId));
    }

    /**
     * 设置沉浸式状态栏(状态栏与Toolbar颜色相同)
     * 适用于DrawerLayout
     *
     * @param drawerLayout drawerLayout
     */
    public void setStatusBarImmerse(DrawerLayout drawerLayout) {
        new StatusBarManager(this).setStatusBarColorForDrawerLayout(drawerLayout,getMainColor());
    }

    /**
     * 返回一个持有Activity对象的Presenter对象
     *
     * @return 返回的presenter对象
     * @see #onCreate(Bundle) 中调用了该方法,子类只需要复写,不需要调用
     */
    protected abstract T getPresenter();


    /**
     * 获取主题色
     * @return 主题色
     */
    public int getMainColor(){
        int kind = getSharedPreferences("theme", MODE_PRIVATE).getInt("kind", 0);
        int mainColor = getSharedPreferences("theme", MODE_PRIVATE).getInt("mainColor",
                ThemeUtil.getDefaultColor());
        if(kind%2==1) {//标题栏为黑色 主题色浅色
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } else{
            //设置标题栏颜色
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        return mainColor;
    }

    /**
     * 获取图标主色
     * @return 图标主色
     */
    public int getIconColor(){
        int kind = getSharedPreferences("theme", MODE_PRIVATE).getInt("kind", 0);
        int defaultColor;
        if(kind%2==1)
            defaultColor = Color.rgb(0,0,0);
        else defaultColor = Color.rgb(255,255,255);
        return getSharedPreferences("theme", MODE_PRIVATE).getInt("iconColor",
                defaultColor);
    }

    /**
     * 修改图标的颜色
     * @param icon 要修改的图标
     */
    public void changeIconColor(ImageView icon){
        if(icon!=null){
            int iconColor = getIconColor();
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(iconColor),
                    0, 0, 0, 0, Color.green(iconColor),
                    0, 0, 0, 0, Color.blue(iconColor),
                    0, 0, 0, (float) Color.alpha(iconColor) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    /**
     * 修改图标的颜色
     * @param icon 要修改的图标
     */
    public void changeIconColor(Drawable icon){
        if(icon!=null){
            int iconColor = getIconColor();
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(iconColor),
                    0, 0, 0, 0, Color.green(iconColor),
                    0, 0, 0, 0, Color.blue(iconColor),
                    0, 0, 0, (float) Color.alpha(iconColor) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }

    }


    /**
     * 修改图标的颜色
     * @param icon 要修改的图标
     */
    public void changeIconColor(ImageView icon,int color){
        if(icon!=null){
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(color),
                    0, 0, 0, 0, Color.green(color),
                    0, 0, 0, 0, Color.blue(color),
                    0, 0, 0, (float) Color.alpha(color) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        }
    }

    /**
     * 修改图标为你要的颜色的颜色
     * @param icon 要修改的图标
     * @param color 要修改的颜色
     */
    public void changeIconColor(Drawable icon,int color){
        if(icon!=null){
            float[] colorMatrix = new float[]{
                    0, 0, 0, 0, Color.red(color),
                    0, 0, 0, 0, Color.green(color),
                    0, 0, 0, 0, Color.blue(color),
                    0, 0, 0, (float) Color.alpha(color) / 255, 0
            };
            icon.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
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
     * 判断网络是否正常
     *
     * @return 返回true代表网络正常，返回false代表网络异常
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
