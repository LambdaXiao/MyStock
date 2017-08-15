
package com.mystockchart_lib.charting.jobs;

import android.view.View;

import com.mystockchart_lib.charting.utils.Transformer;
import com.mystockchart_lib.charting.utils.ViewPortHandler;



public class MoveViewJob extends ViewPortJob {

    public MoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v) {
        super(viewPortHandler, xValue, yValue, trans, v);
    }

    @Override
    public void run() {

        pts[0] = xValue;
        pts[1] = yValue;

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);
    }
}
