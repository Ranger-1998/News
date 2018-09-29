package com.example.chen.news.view.activity.impl;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.chen.news.R;
import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.presenter.impl.NewsPresenter;
import com.example.chen.news.view.activity.i.IMainActivity;
import com.example.chen.news.view.adapter.NewsViewPagerAdapter;
import com.example.chen.news.view.fragment.impl.NewsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<NewsPresenter> implements IMainActivity{

    private ViewPager mViewPager;
    private TabLayout mTab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        setStatusBarImmerse();
    }

    @Override
    protected NewsPresenter getPresenter() {
        return new NewsPresenter(this);
    }

    @Override
    public void failBecauseNotNetworkReturn(int code) {

    }

    @Override
    public void getNewsReturn(NewsEvent event) {

    }

    private void bindView() {
        mViewPager = findViewById(R.id.vp_news);
        mTab = findViewById(R.id.tab);
        NewsFragment fragment = NewsFragment.newInstance("top");
        NewsFragment fragmen2 = NewsFragment.newInstance("shehui");
        NewsFragment fragmen3 = NewsFragment.newInstance("guonei");
        NewsFragment fragmen4 = NewsFragment.newInstance("guoji");
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(fragment);
        fragments.add(fragmen2);
        fragments.add(fragmen3);
        fragments.add(fragmen4);
        mTab.setupWithViewPager(mViewPager, false);
        mViewPager.setAdapter(new NewsViewPagerAdapter(getSupportFragmentManager(), fragments));
    }
}
