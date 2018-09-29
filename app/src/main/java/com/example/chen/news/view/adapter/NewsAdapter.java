package com.example.chen.news.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.news.R;
import com.example.chen.news.model.entity.NewsBean;
import com.example.chen.news.view.activity.impl.WebActivity;

import java.util.List;

/**
 * Created by chen on 2018/9/16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsBean.ResultBean.DataBean> mDataBeanList;
    private Context mContext;

    public NewsAdapter(List<NewsBean.ResultBean.DataBean> mDataBeanList, Context mContext) {
        this.mDataBeanList = mDataBeanList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent,
                false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {
        holder.mTitle.setText(mDataBeanList.get(position).getTitle());
        holder.mDate.setText(mDataBeanList.get(position).getDate());
        holder.mAuthor.setText(mDataBeanList.get(position).getAuthor_name());
        Glide.with(mContext)
                .load(mDataBeanList.get(position).getThumbnail_pic_s())
                .into(holder.mNewsImage1);
        Glide.with(mContext)
                .load(mDataBeanList.get(position).getThumbnail_pic_s02())
                .into(holder.mNewsImage2);
        Glide.with(mContext)
                .load(mDataBeanList.get(position).getThumbnail_pic_s03())
                .into(holder.mNewsImage3);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("uri", mDataBeanList.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataBeanList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mDate;
        private TextView mAuthor;
        private ImageView mNewsImage1, mNewsImage2, mNewsImage3;

        NewsViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDate = itemView.findViewById(R.id.tv_date);
            mNewsImage1 = itemView.findViewById(R.id.im_news_1);
            mNewsImage2 = itemView.findViewById(R.id.im_news_2);
            mNewsImage3 = itemView.findViewById(R.id.im_news_3);
            mAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
