package com.android.mystock.ui.marketpages.fskx;


import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
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
import com.mystockchart_lib.charting.mychart.impl.MinutesLandImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 横屏分时
 */
public class MinutesLandFragment extends BaseHqFragment {

    @BindView(R.id.line_chart)
    MyLineChart lineChart;
    @BindView(R.id.bar_chart)
    MyBarChart barChart;
    @BindView(R.id.fivespeedlayout)
    LinearLayout fivespeedlayout;

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
    @BindView(R.id.fivespeed)
    LinearLayout fivespeed;
    @BindView(R.id.detailListView)
    ListView detailListView;
    @BindView(R.id.space)
    View space;
    @BindView(R.id.detaillayout)
    LinearLayout detaillayout;

    private Stock stock;
    private int exchid;//股票的市场
    private MinutesLandImpl minutesland;
    private OnFragmentInteractionListener mListener;
    private String[] mTitles1 = {"五档", "明细"};
    private ArrayList<CustomTabEntity> mTabEntities1 = new ArrayList<>();

    public static MinutesLandFragment newInstance(Stock stock) {
        MinutesLandFragment fragment = new MinutesLandFragment();
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
            Logs.e(stock.getStockCodeAndMaket());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart_fs, container, false);
        ButterKnife.bind(this, view);
        fivespeedlayout.setVisibility(View.VISIBLE);
        minutesland = new MinutesLandImpl(oThis, exchid, lineChart, barChart);
        minutesland.setOnMinutesHighLightListener(new MinutesLandImpl.OnMinutesHighLightListener() {
            @Override
            public void onMinutesHighLight(DataParse Data, int dataSetIndex, boolean isvisibility) {
                if (mListener != null) {
                    mListener.onMinutesFragment(Data, dataSetIndex, isvisibility);
                }
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

        p.sendRequest(new FsDataMessage());

        return view;
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
            minutesland.setShowXLabels(setXLabels());
            minutesland.setData(Data);
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
        minutesland.setMinutesCount(mMaxDataLength);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * 高亮线出现后的回调接口
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMinutesFragment(DataParse Data, int dataSetIndex, boolean isvisibility);
    }

}
