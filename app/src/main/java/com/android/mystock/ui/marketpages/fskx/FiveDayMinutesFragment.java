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
import com.android.mystock.common.log.Logs;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_5FS;
import com.mystockchart_lib.charting.mychart.MyBarChart;
import com.mystockchart_lib.charting.mychart.MyLineChart;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.impl.FiveDayMinutesImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 5日分时
 */
public class FiveDayMinutesFragment extends BaseHqFragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.line_chart)
    MyLineChart lineChart;
    @BindView(R.id.bar_chart)
    MyBarChart barChart;
    @BindView(R.id.fivespeedlayout)
    LinearLayout fivespeedlayout;

    private Stock stock;
    private int exchid;//股票的市场
    private FiveDayMinutesImpl fivedayminutes;

    protected GestureDetector mGestureDetector = null;

    public static FiveDayMinutesFragment newInstance(Stock stock) {
        FiveDayMinutesFragment fragment = new FiveDayMinutesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, stock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        p = new HQPresenterImpl(oThis);

        if (getArguments() != null) {
            stock = getArguments().getParcelable(ARG_PARAM1);
            exchid = stock.getMaketID();
            Logs.e("分时页面======" + stock.getStockCodeAndMaket());
        }

        mGestureDetector = new GestureDetector(oThis, new ENOgestureListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart_fs, container, false);
        ButterKnife.bind(this, view);
        fivespeedlayout.setVisibility(View.GONE);
        fivedayminutes = new FiveDayMinutesImpl(oThis, exchid, lineChart, barChart);


        lineChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });
        barChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mGestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        p.sendRequest(new FiveFsDataMessage());//发送请求

        return view;
    }


    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {
        super.autoRefresh();
        p.sendRequest(new FiveFsDataMessage());//发送请求
    }

    class FiveFsDataMessage implements I_View_5FS {

        @Override
        public String getStockCode() {
            return stock.getStockCodeAndMaket();
        }

        @Override
        public void onResponseData(DataParse Data) {
            fivedayminutes.setShowXLabels(Data.getFiveDayLabel());
            fivedayminutes.setMinutesCount(605);
            fivedayminutes.setData(Data);
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
            intent.putExtra("tabIndex", 1);
            startActivity(intent);

            return false;
        }
    }

}
