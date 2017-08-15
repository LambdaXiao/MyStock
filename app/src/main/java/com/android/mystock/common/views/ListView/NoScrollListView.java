package com.android.mystock.common.views.ListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Created by xmf on 16/3/26.
 * emil: 693559916@qq.com
 */
public class NoScrollListView extends ListView {


    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
