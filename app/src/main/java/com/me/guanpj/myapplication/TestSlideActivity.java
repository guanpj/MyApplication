package com.me.guanpj.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ToastUtils;

public class TestSlideActivity extends AppCompatActivity implements View.OnClickListener {
   SlideDetectConstraintLayout clParent;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_test_slide);

      clParent = findViewById(R.id.cl_parent);

      findViewById(R.id.top).setOnClickListener(this);
      findViewById(R.id.middle).setOnClickListener(this);
      findViewById(R.id.bottom).setOnClickListener(this);

      findViewById(R.id.cl_bottom).setOnClickListener(this);
      findViewById(R.id.cl_middle).setOnClickListener(this);

      clParent.setOnClickListener(this);


      clParent.setOnSwipeListener(new SlideDetectConstraintLayout.OnSwipeListener() {
         @Override
         public void onStartSwipe() {
            Log.e("gpj", "onStartSwipe");
         }

         @Override
         public void onSwipe(float distanceX) {
            Log.e("gpj", "onSwipe");
         }

         @Override
         public void onStopSwipe() {
            Log.e("gpj", "onStopSwipe");
         }
      });
   }

   @Override
   public void onClick(View v) {
      ToastUtils.showShort(v.getTooltipText());
   }
}
