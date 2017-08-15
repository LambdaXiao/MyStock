package com.android.mystock.ui.marketpages.fskx;


import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_KX;
import com.mystockchart_lib.charting.listener.BarLineChartTouchListener;
import com.mystockchart_lib.charting.mychart.CoupleChartGestureListener;
import com.mystockchart_lib.charting.mychart.MyCombinedChart;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.impl.KLineImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * K线
 */
public class KLineFragment extends BaseHqFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    @BindView(R.id.combinedchart)
    MyCombinedChart combinedchart;
    @BindView(R.id.combinedchart2)
    MyCombinedChart combinedchart2;
    @BindView(R.id.indexlayout)
    LinearLayout indexlayout;

    private Stock stock = null;
    public int mKLinePeriod = 0;// 默认K线周期为日线
    private int mKLineIndex = 0;//默认指标为成交量
    private int mKLineRehabilitation = 0;//默认不复权
    private KLineImpl klineimpl;

    private BarLineChartTouchListener mChartTouchListener;
    private CoupleChartGestureListener coupleChartGestureListener;

    protected GestureDetector mGestureDetector = null;


    public static KLineFragment newInstance(Stock stock, int mKLinePeriod, int mKLineIndex, int mKLineRehabilitation) {
        KLineFragment fragment = new KLineFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, stock);
        args.putInt(ARG_PARAM2, mKLinePeriod);
        args.putInt(ARG_PARAM3, mKLineIndex);
        args.putInt(ARG_PARAM4, mKLineRehabilitation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        p = new HQPresenterImpl(oThis);

        if (getArguments() != null) {
            stock = getArguments().getParcelable(ARG_PARAM1);
            mKLinePeriod = getArguments().getInt(ARG_PARAM2);
            mKLineIndex = getArguments().getInt(ARG_PARAM3);
            mKLineRehabilitation = getArguments().getInt(ARG_PARAM4);
        }
        mGestureDetector = new GestureDetector(oThis, new ENOgestureListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart_kx, container, false);
        ButterKnife.bind(this, view);
        indexlayout.setVisibility(View.GONE);
        klineimpl = new KLineImpl(oThis, combinedchart, combinedchart2);

        p.sendRequest(new kxDataMessage());//发送请求

        combinedchart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        combinedchart2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        return view;
    }

    /*
   定时刷新
    */
    @Override
    public void autoRefresh() {
        super.autoRefresh();
        p.sendRequest(new kxDataMessage());//发送请求
    }

    class kxDataMessage implements I_View_KX {

        @Override
        public String getStockCode() {
            return stock.getStockCodeAndMaket();
        }

        @Override
        public int getKLinePeriod() {
            return mKLinePeriod;
        }

        @Override
        public int getKLineIndex() {
            return mKLineIndex;
        }

        @Override
        public int getKLineRehabilitation() {
            return mKLineRehabilitation;
        }

        @Override
        public void onResponseData(DataParse Data) {

            klineimpl.setData(Data);
        }

        @Override
        public void errorMessage(int errorId, String error) {

        }
    }


    private class ENOgestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent = new Intent(oThis, FsKxLandActivity.class);
            intent.putExtra("stock", stock);
            intent.putExtra("tabIndex", mKLinePeriod + 2);
            startActivity(intent);

            return false;
        }
    }
}
