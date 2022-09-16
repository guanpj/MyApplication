package com.me.guanpj.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;

public class SlideDetectConstraintLayout extends ConstraintLayout {

    private float downX = 0f;
    private float downY = 0f;
    private boolean scrolling = false;

    private ViewConfiguration viewConfiguration;

    public SlideDetectConstraintLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlideDetectConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideDetectConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewConfiguration = ViewConfiguration.get(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            scrolling = false;
            downX = ev.getX();
            downY = ev.getY();

            /*Log.e("gpj", "height:" + getHeight());
            Log.e("gpj", "downY:" + downY);
            Log.e("gpj", "size:" + SizeUtils.dp2px(108));*/

            if (downY < getHeight() - SizeUtils.dp2px(150)) {
                result = true;
            }
        } else if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (!scrolling) {
                float delta = downX - ev.getX();
                if (Math.abs(delta) > SizeUtils.dp2px(5)) {
                    scrolling = true;
                    getParent().requestDisallowInterceptTouchEvent(true);
                    result = true;
                    if (null != onSwipeListener) {
                        onSwipeListener.onStartSwipe();
                    }
                }
            }
        }
        return result;
    }

    private GestureDetector detector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e("gpj", "onScroll");
            if (null != onSwipeListener) {
                onSwipeListener.onSwipe(-distanceX);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        Log.e("gpj", "action = " + action);
        if (action == MotionEvent.ACTION_UP) {
            if (null != onSwipeListener) {
                onSwipeListener.onStopSwipe();
            }
        }
        return detector.onTouchEvent(event);
    }

    public interface OnSwipeListener {
        void onStartSwipe();
        void onSwipe(float distanceX);
        void onStopSwipe();
    }

    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }
}
