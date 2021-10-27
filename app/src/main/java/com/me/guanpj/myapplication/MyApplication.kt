package com.me.guanpj.myapplication

import android.app.Application
import org.greenrobot.eventbus.EventBus

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        EventBus.builder().addIndex(MyEventBusIndex()).installDefaultEventBus()
    }
}