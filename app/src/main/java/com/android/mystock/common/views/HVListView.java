package com.android.mystock.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 自定义股票列表联动控件
 * 支持横向滚动的ListView
 */
public class HVListView extends ListView {

    /**
     * 手势
     */
    private GestureDetector mGesture;
    /**
     * 列头
     */
    public LinearLayout mListHead;
    /**
     * 偏移坐标
     */
    private int mOffset = 0;
    /**
     * 屏幕宽度
     */
    private int screenWidth;

    /**
     * 构造函数
     */
    public HVListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGesture = new GestureDetector(context, mOnGesture);
    }

    /**
     * 分发触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGesture.onTouchEvent(ev);
    }

    /**
     * 手势
     */
    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }

        /** 滚动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            synchronized (HVListView.this) {//synchronized (HVListView.this)锁定了一个对象当有多个线程调用该代码块，只能有当一个线程执行完了，下一个线程才能进来执行
                int moveX = (int) distanceX;//滑动的偏移量
                int curX = mListHead.getScrollX();//头部标题的偏移量
                int scrollWidth = getWidth();//屏幕宽度
                int dx = moveX;//偏移量
                //控制越界问题
                if (curX + moveX < 0)
                    dx = 0;
                if (curX + moveX + getScreenWidth() > scrollWidth)
                    dx = scrollWidth - getScreenWidth() - curX;

                mOffset += dx;

                //根据手势滚动Item视图
                for (int i = 0, j = getChildCount(); i < j; i++) {
                    View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
                    if (child.getScrollX() != mOffset)
                        child.scrollTo(mOffset, 0);
                }
                mListHead.scrollBy(dx, 0);
            }
            requestLayout();
            return true;
        }
    };


    /**
     * 获取屏幕可见范围内最大屏幕--固定了第一列
     *
     * @return
     */
    public int getScreenWidth() {
        if (screenWidth == 0) {
            screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            if (getChildAt(0) != null) {
                screenWidth -= ((ViewGroup) getChildAt(0)).getChildAt(0)
                        .getMeasuredWidth();
            } else if (mListHead != null) {
                //减去固定第一列
                screenWidth -= mListHead.getChildAt(0).getMeasuredWidth();
            }
        }
        return screenWidth;
    }

    /**
     * 获取列头偏移量
     */
    public int getHeadScrollX() {
        return mListHead.getScrollX();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            // 此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            getParent().requestDisallowInterceptTouchEvent(true);
//
//            if (Math.abs(mListHead.getScrollX())>=mListHead.getWidth()){
//                getParent().requestDisallowInterceptTouchEvent(false);
//            }
//            Logs.e("mListHead.getWidth()=="+mListHead.getWidth());
//            Logs.e("mListHead.getScrollX()=="+mListHead.getScrollX());
//        }
//        return super.onTouchEvent(ev);
//    }
}

