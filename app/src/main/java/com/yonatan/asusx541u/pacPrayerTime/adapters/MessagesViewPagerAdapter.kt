package com.yonatan.asusx541u.pacPrayerTime.adapters

import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils.px
import com.yonatan.asusx541u.pacPrayerTime.databinding.ItemMessagesViewPagerBinding

class MessagesViewPagerAdapter(private val listImages: ArrayList<String>, val onClickMessageCallBack: OnClickMessageCallBack) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return listImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = DataBindingUtil.inflate<ItemMessagesViewPagerBinding>(
                LayoutInflater.from(container?.context),
                R.layout.item_messages_view_pager,
                container,
                false)
        binding.messagesViewPagerAdapter = this
        container?.addView(binding.root)
        Picasso.with(container?.context).load(listImages[position]).into(binding.ivMessagesVP)
        binding.ivMessagesVP.setOnClickListener { onClickMessageCallBack.onClickMessageListener(listImages[position]) }
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ImageView)
    }
}

public interface OnClickMessageCallBack {
    fun onClickMessageListener(linkImage: String)
}
