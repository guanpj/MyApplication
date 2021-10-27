package com.me.guanpj.myapplication.event

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.me.guanpj.myapplication.MainActivity
import com.me.guanpj.myapplication.MyEventBusIndex
import com.me.guanpj.myapplication.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class EventBusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        EventBus.getDefault().register(this)
    }

    fun buttonClick(view: android.view.View) {
        EventBus.getDefault().postSticky(MessageEvent("click"))
    }

    fun jump(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    fun onEvent(event : MessageEvent) {
        Log.e("gpj", "EventBusActivity onEvent receive msg: ${event.message} in thread: ${Thread.currentThread().name}")
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 1)
    fun onEvent1(event : MessageEvent) {
        Log.e("gpj", "EventBusActivity onEvent1 receive msg: ${event.message} in thread: ${Thread.currentThread().name}")
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, priority = 2)
    fun onEvent2(event : MessageEvent) {
        Log.e("gpj", "EventBusActivity onEvent2 receive msg: ${event.message} in thread: ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

