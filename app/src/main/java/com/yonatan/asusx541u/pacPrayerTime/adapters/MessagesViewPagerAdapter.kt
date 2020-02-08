package com.yonatan.asusx541u.pacPrayerTime.adapters

import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.databinding.ItemMessagesViewPagerBinding

class MessagesViewPagerAdapter(private val listImages: ArrayList<String>): PagerAdapter() {
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listImages.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val binding = DataBindingUtil.inflate<ItemMessagesViewPagerBinding>(
                LayoutInflater.from(container?.context),
                R.layout.item_messages_view_pager,
                container,
                false)
        binding.messagesViewPagerAdapter = this
        container?.addView(binding.root)
        Picasso.with(container?.context).load(listImages[position]).resize((150 * Resources.getSystem().displayMetrics.density).toInt(), 220).into(binding.ivMessagesVP)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as ImageView)
    }
}