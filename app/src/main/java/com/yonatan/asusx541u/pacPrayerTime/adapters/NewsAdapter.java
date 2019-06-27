package com.yonatan.asusx541u.pacPrayerTime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.model.News;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by asusX541u on 23/05/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    ArrayList<News> newsArrayList;
    Context context;
    public NewsAdapter(ArrayList<News> mNewsArrayList, Context mContext){
        this.newsArrayList = mNewsArrayList;
        this.context = mContext;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News sinNews = newsArrayList.get(position);
        Picasso.with(context).load(sinNews.getLink_image().get(0)).resize(540,400).into(holder.iv_news);
        holder.tv_title_news.setText(sinNews.getTitle());
        holder.tv_content_news.setText(sinNews.getContent());
        holder.tv_writer_news.setText(sinNews.getWriter());
        holder.tv_time_article_news.setText(sinNews.getTime_create());
        //setAnimation(holder.itemView, position);
        //DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
        holder.tv_date_article_news.setText(sinNews.getDate_create());
    }
  /*  private void setAnimation(View itemView, int position) {
        int lastPosition = -1;
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastPosition = position;
        }
    }*/

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_news;
        TextView tv_title_news, tv_content_news, tv_writer_news, tv_time_article_news, tv_date_article_news;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_news = itemView.findViewById(R.id.iv_news);
            tv_title_news = itemView.findViewById(R.id.tv_title_news);
            tv_content_news = itemView.findViewById(R.id.tv_content_news);
            tv_writer_news = itemView.findViewById(R.id.tv_writer_news);
            tv_time_article_news = itemView.findViewById(R.id.tv_time_article_news);
            tv_date_article_news = itemView.findViewById(R.id.tv_date_article_news);
        }
    }
}
