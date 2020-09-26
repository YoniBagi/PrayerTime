package com.yonatan.asusx541u.pacPrayerTime.managers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object NetworkManager {
    private var adsList =  ArrayList<String>()
    //var mDataListener: DataListener? = null
    private val mDataReference = FirebaseDatabase.getInstance().reference

    fun fetchData(){
        fetchAds()
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

    fun getAllAds(): ArrayList<String> {
        return adsList
    }
/*
    interface DataListener{
        fun adsCallback()
    }*/
}