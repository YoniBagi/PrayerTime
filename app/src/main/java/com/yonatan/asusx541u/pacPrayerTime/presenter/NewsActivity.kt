package com.yonatan.asusx541u.pacPrayerTime.presenter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.squareup.picasso.Picasso
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts
import com.yonatan.asusx541u.pacPrayerTime.Utils.UiUtils.centerTitle
import com.yonatan.asusx541u.pacPrayerTime.model.News
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var mNews: News
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        intent.getSerializableExtra(Consts.News)?.let {
            mNews = it as News
        }
        setViews()
        setToolbar()
    }

    @SuppressLint("SetTextI18n")
    private fun setViews() {
        setImage()
        tvTitleNews?.text = mNews.title
        tvContent?.text = mNews.content
        tv_date_article_news?.text = mNews.date_create
    }

    private fun setImage() {
        ivNews?.let {
            Picasso.with(this)
                    .load(mNews.link_image[0])
                    .into(it)
        }

        ivNews?.setOnClickListener { drillToImagePopup(mNews.link_image[0]) }
    }

    private fun drillToImagePopup(linkImage: String?) {
        linkImage?.let {
            val intent = Intent(this, ImagePopUp::class.java)
            intent.putExtra(Consts.LINK_IMAGE, linkImage)
            startActivity(intent)
        }
    }

    private fun setToolbar() {
        centerTitle(this)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "חדשות"
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        finish()
        return true
    }
}
