package com.android.mystock.common.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.mystock.application.App;

/*
自定义数字字体
 */
public class NumberTextView extends TextView {

    private Context mContext = null;
    Typeface texttypeface = null;

    public NumberTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (mContext == null) {
            this.mContext = context;
        }
        if (texttypeface == null) {
            texttypeface = App.digitaltypeface;
        }
        this.setTypeface(texttypeface);
    }

    public NumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (mContext == null) {
            this.mContext = context;
        }
        if (texttypeface == null) {
            texttypeface = App.digitaltypeface;
        }
        this.setTypeface(texttypeface);
    }

    public NumberTextView(Context context) {
        super(context);
        if (mContext == null) {
            this.mContext = context;
        }
        if (texttypeface == null) {
            texttypeface = App.digitaltypeface;
        }
        this.setTypeface(texttypeface);
    }

}
