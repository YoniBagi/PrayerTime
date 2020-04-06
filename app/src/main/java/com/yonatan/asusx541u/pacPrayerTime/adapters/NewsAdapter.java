package com.yonatan.asusx541u.pacPrayerTime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yonatan.asusx541u.pacPrayerTime.R;
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils;
import com.yonatan.asusx541u.pacPrayerTime.enums.TypeNewsViewHolder;
import com.yonatan.asusx541u.pacPrayerTime.model.News;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by asusX541u on 23/05/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<News> newsArrayList;
    Context context;
    OnClickNewsCallBack mOnClickNewsCallBack;

    public NewsAdapter(ArrayList<News> mNewsArrayList, Context mContext, OnClickNewsCallBack onClickNewsCallBack) {
        this.newsArrayList = mNewsArrayList;
        this.context = mContext;
        mOnClickNewsCallBack = onClickNewsCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        //return position % 3;
        return newsArrayList.get(position).getTypeNewsViewHolder().getInt();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TypeNewsViewHolder.DETAILS_NEWS.getInt()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news, parent, false);
            return new DetailsViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages_view_pager, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TypeNewsViewHolder.DETAILS_NEWS.getInt()) {
            DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setFullSpan(true);
            detailsViewHolder.linearLayoutRoot.setLayoutParams(layoutParams);
            News sinNews = newsArrayList.get(position);
            String link = sinNews.getImg();
            Picasso.with(context).load(link).centerCrop()
                    .resize(UiUtils.INSTANCE.getPx(150), UiUtils.INSTANCE.getPx(100))
                    .into(detailsViewHolder.iv_news);
            if (link == null || link.isEmpty()) {
                detailsViewHolder.iv_news.setVisibility(View.GONE);
            } else {
                detailsViewHolder.iv_news.setVisibility(View.VISIBLE);
            }
            detailsViewHolder.tv_title_news.setText(sinNews.getTitle());
            detailsViewHolder.tv_content_news.setText(HtmlCompat.fromHtml(sinNews.getContent(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            //setAnimation(holder.itemView, position);
            //DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
            detailsViewHolder.tv_date_article_news.setText(sinNews.getDate());
            detailsViewHolder.rootNews.setOnClickListener(view -> {
                mOnClickNewsCallBack.onClickNewsListener(newsArrayList.get(position));
            });
        } else {
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            Picasso.with(context).load(newsArrayList.get(position).getImg()).centerCrop()
                    .resize(UiUtils.INSTANCE.getPx(150), UiUtils.INSTANCE.getPx(200))
                    .into(imageViewHolder.ivMessages);
            imageViewHolder.ivMessages.setOnClickListener(view ->
                    mOnClickNewsCallBack.onClickNewsListener(newsArrayList.get(position)));
        }
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

    public class DetailsViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_news;
        TextView tv_title_news, tv_content_news, tv_date_article_news;
        ConstraintLayout rootNews;
        LinearLayout linearLayoutRoot;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            iv_news = itemView.findViewById(R.id.iv_news);
            tv_title_news = itemView.findViewById(R.id.tv_title_news);
            tv_content_news = itemView.findViewById(R.id.tv_content_news);
            tv_date_article_news = itemView.findViewById(R.id.tv_date_article_news);
            rootNews = itemView.findViewById(R.id.rootRowNews);
            linearLayoutRoot = itemView.findViewById(R.id.linearLayoutRoot);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMessages;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMessages = itemView.findViewById(R.id.ivMessagesVP);
        }
    }

    public interface OnClickNewsCallBack {
        void onClickNewsListener(News news);
    }
}
