package com.me.guanpj.myapplication

import android.os.SystemClock

class Leak(var obj: Any?) : Thread() {
    override fun run() {
        super.run()
        val local = obj;
        obj = null
        SystemClock.sleep(100000)
    }

    fun leak() {
        start()
    }
}