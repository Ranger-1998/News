package com.example.chen.news.view.fragment.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.news.R;
import com.example.chen.news.model.entity.NewsBean;
import com.example.chen.news.model.event.NewsEvent;
import com.example.chen.news.presenter.impl.NewsFragmentPresenter;
import com.example.chen.news.view.adapter.NewsAdapter;
import com.example.chen.news.view.fragment.i.INewsFragment;

import java.util.List;

/**
 * Created by chen on 2018/9/16.
 */
public class NewsFragment extends BaseFragment<NewsFragmentPresenter> implements INewsFragment{

    private View mRoot;
    private RecyclerView mRecyclerView;
    private NewsBean mNews;
    private NewsAdapter mAdapter;
    private String mType;

    public static final NewsFragment newInstance(String type) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("NewsType", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mType = getArguments().getString("NewsType");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_news, container, false);
            bindView();
            mPresenter.getNewsByType(mType);
        }
        return mRoot;
    }

    @Override
    protected NewsFragmentPresenter getPresenter() {
        return new NewsFragmentPresenter(this);
    }

    public void bindView() {
        mRecyclerView = mRoot.findViewById(R.id.rv_news);
    }

    @Override
    public void failBecauseNotNetworkReturn(int code) {

    }

    @Override
    public void getNewsReturn(NewsBean newsBean) {
        this.mNews = newsBean;
        if (getActivity() != null) {
            mAdapter = new NewsAdapter(mNews.getResult().getData(), getActivity());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}
