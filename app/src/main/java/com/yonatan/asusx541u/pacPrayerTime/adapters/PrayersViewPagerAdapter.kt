package com.yonatan.asusx541u.pacPrayerTime.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.databinding.ViewPagerPrayersItemBinding
import com.yonatan.asusx541u.pacPrayerTime.enums.TypePrayer
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer

class PrayersViewPagerAdapter(
        private val allPrayers: ArrayList<Prayer>,
        private val clickPrayerCallBack: ClickPrayerCallBack) : PagerAdapter() {
    override fun isViewFromObject(view: View, myObject: Any): Boolean {
        return view == myObject
    }

    override fun getCount(): Int {
        return allPrayers.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var binding = DataBindingUtil.inflate<ViewPagerPrayersItemBinding>(
                LayoutInflater.from(container.context),
                R.layout.view_pager_prayers_item,
                container,
                false)
        binding.prayersVPadapter = this
        binding.position = position
        binding.prayer = "תפילת "
        binding.atTime = "בשעה "
        container.addView(binding.root)
        return binding.root
    }

    private fun namePrayerToHeb(kindPrayer: String): String {
        when (kindPrayer){
            "sahrit" -> return "שחרית"
            "mincha" -> return "מנחה"
        }
        return "ערבית"
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as androidx.cardview.widget.CardView)
    }

    fun getPrayer(position: Int): String{
        return namePrayerToHeb(allPrayers[position].kind)
    }

    fun getTimePrayer(position: Int):String{
        return allPrayers[position].time
    }

    fun getAdapterSynagogue(position: Int) : SynagogueGridViewAdapter{
        return SynagogueGridViewAdapter(allPrayers[position].synagogueList as ArrayList<String>)
    }

    fun onClickItem(position: Int){
        clickPrayerCallBack.onClickPrayer(allPrayers[position].typePrayer)
    }

    fun onClickAlert(position: Int){
        clickPrayerCallBack.onClickAlert(allPrayers[position])
    }

    interface ClickPrayerCallBack{
        fun onClickPrayer(typePrayer: TypePrayer)
        fun onClickAlert(prayer: Prayer)
    }

}