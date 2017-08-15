package com.android.mystock.common.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.mystock.R;
import com.android.mystock.common.log.Logs;
import com.android.mystock.data.consts.ENOStyle;


public class SerachEditView extends View {

    private int mFocusPostion = 0;
    private StringBuilder mInputString = new StringBuilder();
    private Paint mPaint = new Paint(1);
    private Paint mCursorPaint = new Paint(1);
    private boolean mCursorVisible = false;
    private boolean mShowCursor = true;
    private float mTotalDrawWidth = 0.0f;
    private int mResDefineSpaceHeight = 0x28;
    private int mDrawY = 0;
    private String mEmptyShowTips = "";
    private Paint mEmptyTipsPaint = new Paint(0x1);
    private int mEmptyTipsColor = ENOStyle.hq_flat_bg;
    private int mCursorColor = ENOStyle.hq_flat_bg;
    private int mInputTextColor = ENOStyle.hq_flat;
    private long delayMillis = 500;

    public SerachEditView(Context context) {
        super(context);
        init();
    }

    public SerachEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SerachEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setStrokeWidth(1.0f);
        handler.postDelayed(runnable, delayMillis);
        Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        mPaint.setTypeface(font);
        int nFontSize = (int) getContext().getResources().getDimension(R.dimen.setting_mzsm);
        mPaint.setTextSize((float) nFontSize);
        mCursorPaint.setColor(mCursorColor);
        mCursorPaint.setStrokeWidth(2.0f);
        mResDefineSpaceHeight = (int) getContext().getResources().getDimension(R.dimen.searchview_height);
        mDrawY = (getTop() + ((int) (((float) mResDefineSpaceHeight - mPaint.descent()) - mPaint.ascent()) / 2));
        mEmptyTipsPaint.setTypeface(font);
        mEmptyTipsPaint.setTextSize((float) nFontSize);
        mEmptyTipsPaint.setColor(mEmptyTipsColor);

    }

    // public void setPaint(Paint paint) {
    //    mPaint = paint;
    // }

    public void setEmptyShowTips(String words) {
        mEmptyShowTips = words;
    }

    public void showCursorTips(boolean on) {
        mShowCursor = on;
    }

    public void clearAllValue() {
        mInputString.setLength(0);
        mFocusPostion = 0;
        invalidate();
    }

    public void delOneValue() {
        if (mFocusPostion > 0) {
            mInputString.deleteCharAt((mFocusPostion - 1));
            mFocusPostion--;
        }
        invalidate();
    }

    public void inputValue(String keyValue) {
        if (keyValue == null) {
            return;
        }
        if (mFocusPostion == mInputString.length()) {
            mInputString.append(keyValue);
        } else if (mFocusPostion < mInputString.length()) {
            mInputString.insert(mFocusPostion, keyValue);
        }
        mFocusPostion = (mFocusPostion + keyValue.length());
        int size = mInputString.length();
        mTotalDrawWidth = 0.0f;
        for (int i = 0; i < size; i = i + 1) {
            float oneLen = mPaint.measureText(mInputString.substring(i, (i + 1)));
            mTotalDrawWidth = (mTotalDrawWidth + oneLen);
        }
        invalidate();
    }

    public String getEditValue() {
        String value = "";
        if (mInputString != null) {
            value = mInputString.toString();
        }
        return value;
    }

    public int getEditVauleLength() {
        int len = 0;
        if (mInputString != null) {
            len = mInputString.length();
        }
        return len;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cursor_x = 10;
        int draw_x = getLeft() + cursor_x;
        int size = mInputString.length();
        int start_draw_pos = 0;
        if (mTotalDrawWidth > (float) getWidth()) {
            int end_x = getRight() - 2;
            for (int i = size; i > 0; i = i - 1) {
                float oneLen = mPaint.measureText(mInputString.substring((i - 1), i));
                end_x = (int) ((float) end_x - oneLen);
                start_draw_pos = i;
                if ((end_x + 2) < getLeft()) {
                    break;
                }
            }
        }
        for (int i = start_draw_pos; i < size; i = i + 1) {
            canvas.drawText(mInputString, i, (i + 1), (float) draw_x, (float) mDrawY, mPaint);
            float oneLen = mPaint.measureText(mInputString.substring(i, (i + 1)));
            draw_x = (int) ((float) draw_x + oneLen);
            if (mFocusPostion == (i + 1)) {
                cursor_x = draw_x;
            }
        }
        if (mCursorVisible && mShowCursor) {

            float mPaintScent = mPaint.ascent() - mPaint.descent();
            float startY = mDrawY + mPaintScent;
            float stopY = mDrawY + mPaint.descent() * 2;

            canvas.drawLine((float) cursor_x, startY, (float) cursor_x, stopY, mCursorPaint);
        }
        if ((size == 0) && (mEmptyShowTips.length() > 0)) {
            canvas.drawText(mEmptyShowTips, (float) (draw_x + 5), (float) mDrawY, mEmptyTipsPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        Logs.d("============>>>>>>>>>>> onTouchEvent  mInputString.length()=" + mInputString.length());
        if (mInputString.length() > 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int touch_x = (int) event.getX();
                mFocusPostion = -1;
                int draw_x = getLeft();
                int size = mInputString.length();
                int start_draw_pos = 0;
                if (mTotalDrawWidth > (float) getWidth()) {
                    int end_x = getRight() - 2;
                    for (int i = size; i > 0; i = i - 1) {
                        float oneLen = mPaint.measureText(mInputString.substring((i - 1), i));
                        end_x = (int) ((float) end_x - oneLen);
                        start_draw_pos = i;
                        if ((end_x + 2) < getLeft()) {
                            break;
                        }
                    }
                }
                for (int i = start_draw_pos; i < size; i = i + 1) {
                    float oneLen = mPaint.measureText(mInputString.substring(i, (i + 1)));
                    draw_x = (int) ((float) draw_x + oneLen);
                    if ((float) Math.abs((draw_x - touch_x)) < oneLen) {
                        mFocusPostion = i;
                    }
                }
                if (mFocusPostion == -1) {
                    mFocusPostion = size;
                }
            }
            invalidate();
        }
        return true;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            mCursorVisible = (!mCursorVisible);
            invalidate();
            handler.postDelayed(this, delayMillis);
        }
    };

}
