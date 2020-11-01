package com.yonatan.asusx541u.pacPrayerTime.managers

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yonatan.asusx541u.pacPrayerTime.enums.TypeNewsViewHolder
import com.yonatan.asusx541u.pacPrayerTime.model.News
import com.yonatan.asusx541u.pacPrayerTime.model.Prayer
import java.util.*
import kotlin.collections.ArrayList

object NetworkManager {
    private var adsList =  ArrayList<String>()
    //var mDataListener: DataListener? = null
    private val mDataReference = FirebaseDatabase.getInstance().reference
    private var allPrayers =  ArrayList<Prayer>()
    private lateinit var currentTime : Calendar
    private var allPrayerFetched = false
    private var newsFetched = false
    private val newsArrayList = ArrayList<News>()
    private var mDataListener : DataListener? = null

    fun fetchData(){
        //fetchAds()
        fetchAllPrayers()
        fetchNews()
    }

    private fun fetchNews() {
        mDataReference.child("newsAndAds").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    newsArrayList.clear()
                    for (itemDataSnapshot in dataSnapshot.children) {
                        val news = itemDataSnapshot.getValue(News::class.java)
                        if (news.content != null && !news.content.isEmpty()) {
                            news.typeNewsViewHolder = TypeNewsViewHolder.DETAILS_NEWS
                        } else {
                            news.typeNewsViewHolder = TypeNewsViewHolder.IMAGE_NEWS
                        }
                        newsArrayList.add(news)
                    }
                    //todo newsAdapter.notifyDataSetChanged()
                    newsFetched = true
                    checkIfAllDataFetched()
                } catch (e: Exception) {
                    if (e.message != null) Log.d("Request News:", e.message)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun checkIfAllDataFetched() {
        if (allPrayerFetched && newsFetched){
            mDataListener?.firstDataFetched()
        }
    }

    private fun fetchAllPrayers() {
        mDataReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val prayersMap = dataSnapshot.value as Map<*, *>
                for ((key, value) in prayersMap) {
                    if (key == "sahrit" || key == "mincha" || key == "arvit") {
                        val sinPrayer: Map<*, *> = value as Map<*, *>
                        for ((_, value1) in sinPrayer) {
                            val singMinyan = value1 as Map<*, *>
                            val prayer = Prayer(
                                    singMinyan["place"] as String?,
                                    singMinyan["time"] as String?
                            )
                            prayer.kind = key as String
                            allPrayers.add(prayer)
                        }
                    }
                }
                currentTime = Calendar.getInstance()
                if (currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || currentTime.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) for (prayer in allPrayers) {
                    if (prayer.place == "שערי ציון" && prayer.time == "6:00") prayer.time = "5:50" else if (prayer.place == "מרכזי" && prayer.time == "6:00") prayer.time = "5:50"
                }
                allPrayers.sort()
                allPrayerFetched = true
                checkIfAllDataFetched()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun removePost(idPost: String){
        mDataReference.child("newsAndAds").child(idPost).removeValue()
    }

    /*fun setAdsListener(dataListener: DataListener?){
        mDataListener = dataListener
    }*/

    private fun fetchAds() {
        mDataReference.child("adsSorek").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                adsList.clear()
                dataSnapshot?.let {
                    for (dataSnap in it.children){
                        val item = dataSnap.getValue(String::class.java)
                        item?.let { it1 -> adsList.add(it1) }
                    }
                    //mDataListener?.adsCallback()
                }
            }

        })
    }

    fun getAllPrayers() = allPrayers

    fun getNewsList() = newsArrayList

    fun getAllAds(): ArrayList<String> {
        return adsList
    }

    fun setDataListener(dataListener: DataListener){
        mDataListener = dataListener
    }

    interface DataListener{
        fun adsCallback()
        fun firstDataFetched()
    }
}