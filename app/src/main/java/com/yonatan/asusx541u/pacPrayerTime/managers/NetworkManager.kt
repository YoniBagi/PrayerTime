package com.yonatan.asusx541u.pacPrayerTime.managers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object NetworkManager {
    private var adsList =  ArrayList<String>()
    var mDataListener: DataListener? = null
    private val mDataReference = FirebaseDatabase.getInstance().reference

    fun fetchData(){
        fetchAds()
    }

    fun setAdsListener(dataListener: DataListener?){
        mDataListener = dataListener
    }

    private fun fetchAds() {
        mDataReference.child("ads").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                adsList.clear()
                (dataSnapshot?.value as ArrayList<String>).filterNotNullTo(adsList)
                mDataListener?.adsCallback()
            }

        })
    }

    fun getAllAds(): ArrayList<String> {
        return adsList
    }

    interface DataListener{
        fun adsCallback()
    }
}