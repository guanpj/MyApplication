package com.me.guanpj.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

class WorkThread extends Thread {

    private Handler mHandler;
    private Looper mLooper;

    @Override
    public void run() {
        Looper.prepare();

        synchronized (this) {
            mLooper = Looper.myLooper();
            Log.e("gpj", Thread.currentThread().getName() + ":Looper prepared");
            //通知等待Looper的线程
            notifyAll();
        }

        Looper.loop();
    }

    public Handler getWorkHandler() {
        if (mHandler == null) {
            /*mHandler = Handler.createAsync(getLooper(), msg -> {
                Log.e("gpj", "WorkThread received msg:" + msg.what);
                return true;
            });*/
            mHandler = new Handler(getLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Log.e("gpj", "WorkThread received msg:" + msg.what);
                }
            };
        }
        return mHandler;
    }

    public Looper getLooper() {
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    //等待Looper
                    Log.e("gpj", Thread.currentThread().getName() + ":Wait for looper");
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }

    public void startWork() {
        if (Looper.myLooper() != null) {
            return;
        }
        start();
    }

    public void stopWork() {
        Looper looper = getLooper();
        if (looper != null) {
            looper.quit();
        }
    }
}
