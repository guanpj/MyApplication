package com.me.guanpj.myapplication;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

class Test {
   void test() {
      abc abc = new abc();
      Continuation continuation = new Continuation() {
         @NonNull
         @Override
         public CoroutineContext getContext() {
            return null;
         }

         @Override
         public void resumeWith(@NonNull Object o) {

         }
      };
      ExtKt.sus(abc, continuation);
   }

}
