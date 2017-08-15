package com.android.mystock.ui.marketpages.fskx;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.mystockchart_lib.charting.mychart.impl.FiveDayMinutesLandImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 5日横屏分时
 */
public class FiveDayMinutesLandFragment extends BaseHqFragment {

    @BindView(R.id.line_chart)
    MyLineChart lineChart;
    @BindView(R.id.bar_chart)
    MyBarChart barChart;
    @BindView(R.id.fivespeedlayout)
    LinearLayout fivespeedlayout;

    private static final String ARG_PARAM1 = "param1";
    private Stock stock;
    private int exchid;//股票的市场
    private FiveDayMinutesLandImpl fivedayminutesland;
    private OnFragmentInteractionListener mListener;

    public static FiveDayMinutesLandFragment newInstance(Stock stock) {
        FiveDayMinutesLandFragment fragment = new FiveDayMinutesLandFragment();
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
        fivespeedlayout.setVisibility(View.GONE);
        fivedayminutesland = new FiveDayMinutesLandImpl(oThis,exchid,lineChart,barChart);

        fivedayminutesland.setOnMinutesHighLightListener(new FiveDayMinutesLandImpl.OnMinutesHighLightListener() {
            @Override
            public void onMinutesHighLight(DataParse Data, int dataSetIndex, boolean isvisibility) {
                if(mListener != null){
                    mListener.onFiveMinutesFragment(Data,dataSetIndex,isvisibility);
                }
            }
        });
        p.sendRequest(new FiveFsDataMessage());

        return view;
    }

    class FiveFsDataMessage implements I_View_5FS {

        @Override
        public String getStockCode() {
            return stock.getStockCodeAndMaket();
        }

        @Override
        public void onResponseData(DataParse Data) {
            fivedayminutesland.setShowXLabels(Data.getFiveDayLabel());
            fivedayminutesland.setMinutesCount(605);
            fivedayminutesland.setData(Data);
        }

        @Override
        public void errorMessage(int errorId, String error) {

        }
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
        void onFiveMinutesFragment(DataParse Data, int dataSetIndex, boolean isvisibility);
    }

}
