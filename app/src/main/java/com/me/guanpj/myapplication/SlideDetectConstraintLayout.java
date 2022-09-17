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
    private int touchAreaHeight;

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
        touchAreaHeight = SizeUtils.dp2px(128);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            scrolling = false;
            downX = ev.getX();
            downY = ev.getY();

            //手指按到滑动区域内就开始拦截掉父view的事件，防止滑动冲突
            if (downY > getHeight() - touchAreaHeight) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        } else if (ev.getActionMasked() == MotionEvent.ACTION_MOVE) {
            if (!scrolling) {
                if (downY > getHeight() - touchAreaHeight) {
                    float delta = downX - ev.getX();
                    //满足滑动条件，拦截所有 move 事件，走 onTouchEvent
                    if (Math.abs(delta) > viewConfiguration.getScaledTouchSlop()) {
                        scrolling = true;
                        result = true;
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
        void onSwipe(float distanceX);
        void onStopSwipe();
    }

    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }
}
