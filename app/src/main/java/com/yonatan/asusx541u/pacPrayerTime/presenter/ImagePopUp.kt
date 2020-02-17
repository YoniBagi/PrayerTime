package com.yonatan.asusx541u.pacPrayerTime.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.squareup.picasso.Picasso
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils.centerTitle
import kotlinx.android.synthetic.main.activity_image_pop_up.*

class ImagePopUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_pop_up)
        setToolbar()
        intent.getStringExtra(Consts.LINK_IMAGE)?.let { setViews(it) }
    }

    private fun setViews(linkImage: String) {
        photoViewZoom?.let {
            Picasso.with(this).load(linkImage).into(it)
        }
    }

    private fun setToolbar() {
        centerTitle(this)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }
}
