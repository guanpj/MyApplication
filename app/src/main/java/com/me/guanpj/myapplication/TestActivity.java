package com.me.guanpj.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    WorkThread workThread;
    Handler handler;
    int token = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void start(View view) {
        if (workThread == null) {
            workThread = new WorkThread();
        }
        workThread.start();
    }


    public void stop(View view) {
        if (workThread != null) {
            workThread.stopWork();
            workThread = null;
        }
    }

    public void sendAsync(View view) {
        handler = workThread.getWorkHandler();
        if (null != handler) {
            Message syncMessage = handler.obtainMessage();
            syncMessage.what = 1;
            handler.sendMessage(syncMessage);
        }
    }

    public void sendSync(View view) {
        handler = workThread.getWorkHandler();
        if (null != handler) {
            Message asyncMessage = handler.obtainMessage();
            asyncMessage.what = 2;
            asyncMessage.setAsynchronous(true);
            handler.sendMessage(asyncMessage);
        }
    }

    public void postSyncBarrier(View view) {
        handler = workThread.getWorkHandler();
        if (null != handler) {
            MessageQueue queue = handler.getLooper().getQueue();
            try {
                Method method = MessageQueue.class.getDeclaredMethod("postSyncBarrier");
                token = (int) method.invoke(queue);
                Log.e("gpj", "Barrier token is:" + token);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeSyncBarrier(View view) {
        handler = workThread.getWorkHandler();
        if (null != handler && token != -1) {
            MessageQueue queue = handler.getLooper().getQueue();
            try {
                Method method = MessageQueue.class.getDeclaredMethod("removeSyncBarrier", int.class);
                method.invoke(queue, token);
                Log.e("gpj", "Token is removed:" + token);
                token = -1;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void test(View view) {
        WorkThread workThread = new WorkThread();
        workThread.start();

        Handler handler = workThread.getWorkHandler();

        int newToken = 0;

        MessageQueue queue = handler.getLooper().getQueue();
        try {
            Method method = MessageQueue.class.getDeclaredMethod("postSyncBarrier");
            newToken = (int) method.invoke(queue);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Message syncMessage = handler.obtainMessage();
        syncMessage.what = 1;
        handler.sendMessage(syncMessage);

        Message asyncMessage = handler.obtainMessage();
        asyncMessage.what = 2;
        asyncMessage.setAsynchronous(true);
        handler.sendMessage(asyncMessage);

        try {
            Method method = MessageQueue.class.getDeclaredMethod("removeSyncBarrier", int.class);
            method.setAccessible(true);
            method.invoke(queue, newToken);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Message syncMessage3 = handler.obtainMessage();
        syncMessage3.what = 3;
        handler.sendMessageDelayed(syncMessage3, 100);
    }
}
