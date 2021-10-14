package com.me.guanpj.myapplication.glide

import android.content.Context
import android.graphics.Color
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.BaseRequestOptions

@GlideExtension
object ProgressExtension {

    @GlideOption
    @JvmStatic
    fun progress(options: BaseRequestOptions<*>, context: Context): BaseRequestOptions<*> {
        val progressPlaceholderDrawable =
            ProgressPlaceholderDrawable(
                context,
                options.placeholderDrawable,
                options.placeholderId
            )
        progressPlaceholderDrawable.setTint(Color.GRAY)
        return options.placeholder(progressPlaceholderDrawable)
    }
}