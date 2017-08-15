package com.android.mystock.ui.marketpages.fskx;


import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.mystock.R;
import com.android.mystock.common.beans.FlycoTabEntity;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.views.NumberTextView;
import com.android.mystock.data.consts.Consts;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_FS;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mystockchart_lib.charting.mychart.MyBarChart;
import com.mystockchart_lib.charting.mychart.MyLineChart;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.impl.MinutesImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分时
 */
public class MinutesFragment extends BaseHqFragment {

    @BindView(R.id.line_chart)
    MyLineChart lineChart;
    @BindView(R.id.bar_chart)
    MyBarChart barChart;

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.sell_price5)
    NumberTextView sellPrice5;
    @BindView(R.id.sell_vol5)
    NumberTextView sellVol5;
    @BindView(R.id.sell_price4)
    NumberTextView sellPrice4;
    @BindView(R.id.sell_vol4)
    NumberTextView sellVol4;
    @BindView(R.id.sell_price3)
    NumberTextView sellPrice3;
    @BindView(R.id.sell_vol3)
    NumberTextView sellVol3;
    @BindView(R.id.sell_price2)
    NumberTextView sellPrice2;
    @BindView(R.id.sell_vol2)
    NumberTextView sellVol2;
    @BindView(R.id.sell_price1)
    NumberTextView sellPrice1;
    @BindView(R.id.sell_vol1)
    NumberTextView sellVol1;
    @BindView(R.id.buy_price1)
    NumberTextView buyPrice1;
    @BindView(R.id.buy_vol1)
    NumberTextView buyVol1;
    @BindView(R.id.buy_price2)
    NumberTextView buyPrice2;
    @BindView(R.id.buy_vol2)
    NumberTextView buyVol2;
    @BindView(R.id.buy_price3)
    NumberTextView buyPrice3;
    @BindView(R.id.buy_vol3)
    NumberTextView buyVol3;
    @BindView(R.id.buy_price4)
    NumberTextView buyPrice4;
    @BindView(R.id.buy_vol4)
    NumberTextView buyVol4;
    @BindView(R.id.buy_price5)
    NumberTextView buyPrice5;
    @BindView(R.id.buy_vol5)
    NumberTextView buyVol5;
    @BindView(R.id.tl_1)
    CommonTabLayout mTabLayout_1;
    @BindView(R.id.fivespeedlayout)
    LinearLayout fivespeedlayout;
    @BindView(R.id.fivespeed)
    LinearLayout fivespeed;
    @BindView(R.id.detailListView)
    ListView detailListView;
    @BindView(R.id.space)
    View space;
    @BindView(R.id.detaillayout)
    LinearLayout detaillayout;

    private String[] mTitles1 = {"五档", "明细"};
    private ArrayList<CustomTabEntity> mTabEntities1 = new ArrayList<>();
    private Stock stock;
    private int exchid;//股票的市场
    private MinutesImpl minutes;

    protected GestureDetector mGestureDetector = null;

    public static MinutesFragment newInstance(Stock stock) {
        MinutesFragment fragment = new MinutesFragment();
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
        fivespeedlayout.setVisibility(View.VISIBLE);
        minutes = new MinutesImpl(oThis, exchid, lineChart, barChart);


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

        for (int i = 0; i < mTitles1.length; i++) {
            mTabEntities1.add(new FlycoTabEntity(mTitles1[i], 0, 0));
        }
        mTabLayout_1.setTabData(mTabEntities1);
        mTabLayout_1.setCurrentTab(0);
        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    fivespeed.setVisibility(View.VISIBLE);
                    detaillayout.setVisibility(View.GONE);
                } else {
                    fivespeed.setVisibility(View.GONE);
                    detaillayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mTabLayout_1.getCurrentTab() == 0) {
                    mTabLayout_1.setCurrentTab(1);
                    fivespeed.setVisibility(View.GONE);
                    detaillayout.setVisibility(View.VISIBLE);
                } else {
                    mTabLayout_1.setCurrentTab(0);
                    fivespeed.setVisibility(View.VISIBLE);
                    detaillayout.setVisibility(View.GONE);
                }
            }

        });

        p.sendRequest(new FsDataMessage());//发送请求

        return view;
    }


    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {
        super.autoRefresh();
        p.sendRequest(new FsDataMessage());//发送请求
    }


    @OnClick({R.id.fivespeed,R.id.space})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fivespeed:
            case R.id.space:
                if (mTabLayout_1.getCurrentTab() == 0) {
                    mTabLayout_1.setCurrentTab(1);
                    fivespeed.setVisibility(View.GONE);
                    detaillayout.setVisibility(View.VISIBLE);
                } else {
                    mTabLayout_1.setCurrentTab(0);
                    fivespeed.setVisibility(View.VISIBLE);
                    detaillayout.setVisibility(View.GONE);
                }
                break;

        }
    }


    class FsDataMessage implements I_View_FS {

        @Override
        public String getStockCode() {
            return stock.getStockCodeAndMaket();
        }

        @Override
        public void onResponseData(DataParse Data) {
            minutes.setShowXLabels(setXLabels());
            minutes.setData(Data);
        }

        @Override
        public void errorMessage(int errorId, String error) {

        }
    }

    /**
     * 设置分时线的x轴刻度值
     *
     * @return
     */
    private SparseArray<String> setXLabels() {
        int mMaxDataLength = 241;//全天共返回241个采样点
        minutes.setMinutesCount(mMaxDataLength);
        SparseArray<String> xLabels = new SparseArray<>();
        switch (exchid) {
            case Consts.MARKET_SZ:
            case Consts.MARKET_SH:
                xLabels.put(0, "09:30");
                xLabels.put(mMaxDataLength / 4, "10:30");
                xLabels.put(mMaxDataLength / 2, "11:30/13:00");
                xLabels.put(mMaxDataLength / 4 * 3, "14:00");
                xLabels.put(mMaxDataLength - 1, "15:00");
                break;
            case Consts.MARKET_HK:
                xLabels.put(0, "09:30");
                xLabels.put(60, "12:00/13:00");
                xLabels.put(mMaxDataLength - 1, "16:00");
                break;
            default:
                xLabels.put(0, "09:30");
                xLabels.put(mMaxDataLength / 4, "10:30");
                xLabels.put(mMaxDataLength / 2, "11:30/13:00");
                xLabels.put(mMaxDataLength / 4 * 3, "14:00");
                xLabels.put(mMaxDataLength - 1, "15:00");
                break;
        }

        return xLabels;
    }

    private class ENOgestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent = new Intent(oThis, FsKxLandActivity.class);
            intent.putExtra("stock", stock);
            intent.putExtra("tabIndex", 0);
            startActivity(intent);

            return false;
        }
    }

}
