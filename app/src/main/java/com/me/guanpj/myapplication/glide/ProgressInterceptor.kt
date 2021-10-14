package com.me.guanpj.myapplication.glide

import okhttp3.Interceptor
import okhttp3.Response

class ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val url = request.url.toString()
        val responseBody = response.body ?: return response
        return response.newBuilder().body(ProgressResponseBody(responseBody, url)).build()
    }

    companion object {
        private val LISTENERS = hashMapOf<String, OnProgressChangeListener>()

        fun addListener(url: String, onProgressChangeListener: OnProgressChangeListener) {
            LISTENERS[url] = onProgressChangeListener
        }

        fun removeListener(url: String) {
            LISTENERS.remove(url)
        }

        fun getListener(url: String) = LISTENERS[url]
    }
}

interface OnProgressChangeListener {
    fun onProgress(progress: Int)
}