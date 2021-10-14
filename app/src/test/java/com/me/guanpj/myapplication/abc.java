package com.me.guanpj.myapplication;

import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class abc {
   static void abc() {
      try {
         Method def = abc.class.getMethod("def", Void.class);
         def.invoke(new abc());
      } catch (NoSuchMethodException e) {
         e.printStackTrace();
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
   }

   public void def() {
      System.out.println("aaa");
   }

   @Test
   void test() {
      abc();
   }

   public static void main(String[] args) {
      ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue();
      final int _1M = 1024 * 1024;

      Object value = new Object();
      Map<Object, Object> map = new HashMap<>();
      for(int i = 0;i < 10000;i++) {
         byte[] bytes = new byte[_1M];
         WeakReference<byte[]> weakReference = new WeakReference(bytes, referenceQueue);
         map.put(weakReference, value);
      }
      System.out.println("map.size->" + map.size());

      Thread thread = new Thread(() -> {
         try {
            int cnt = 0;
            WeakReference<byte[]> k;
            while((k = (WeakReference) referenceQueue.remove()) != null) {
               System.out.println((cnt++) + "回收了:" + k);
            }
         } catch(InterruptedException e) {
            //结束循环
         }
      });
      thread.setDaemon(true);
      thread.start();
   }
}
