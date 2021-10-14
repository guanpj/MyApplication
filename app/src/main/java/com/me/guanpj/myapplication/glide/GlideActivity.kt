package com.me.guanpj.myapplication.glide

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.me.guanpj.myapplication.R

class GlideActivity : AppCompatActivity() {
    private val URL = "https://pic.rmb.bdstatic.com/1530971282b420d77bdfb6444d854f952fe31f0d1e.jpeg"

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        image = findViewById(R.id.image)
    }

    fun load(view: View) {
        /*Glide.with(this).load(URL)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(image)*/

        GlideApp.with(this)
            .load(URL)
            .placeholder(R.color.teal_200)
            .progress(this)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(ProgressImageViewTarget(URL, image))
    }
}