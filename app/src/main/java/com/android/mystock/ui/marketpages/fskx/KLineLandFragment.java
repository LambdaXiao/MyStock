package com.android.mystock.ui.marketpages.fskx;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.adapters.CommonAdapter;
import com.android.mystock.common.adapters.ViewHolder;
import com.android.mystock.data.consts.Consts;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_KX;
import com.mystockchart_lib.charting.mychart.MyCombinedChart;
import com.mystockchart_lib.charting.mychart.bean.DataParse;
import com.mystockchart_lib.charting.mychart.impl.KLineLandImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 横屏K线
 */
public class KLineLandFragment extends BaseHqFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    @BindView(R.id.combinedchart)
    MyCombinedChart combinedchart;
    @BindView(R.id.combinedchart2)
    MyCombinedChart combinedchart2;
    @BindView(R.id.bfq)
    TextView bfq;
    @BindView(R.id.qfq)
    TextView qfq;
    @BindView(R.id.index)
    ListView index;
    @BindView(R.id.indexlayout)
    LinearLayout indexlayout;



    private Stock stock = null;
    public int mKLinePeriod = 0;// 默认K线周期为日线
    private int mKLineIndex = 0;//默认指标为成交量
    private int mKLineRehabilitation = 0;//默认不复权
    private KLineLandImpl klinelandimpl;
    private CommonAdapter<Map<String,String>> madapter;
    private List<Map<String,String>> list = new ArrayList<Map<String,String>>();

    private OnFragmentInteractionListener mListener;

    public static KLineLandFragment newInstance(Stock stock, int mKLinePeriod, int mKLineIndex, int mKLineRehabilitation) {
        KLineLandFragment fragment = new KLineLandFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chart_kx, container, false);
        ButterKnife.bind(this, view);
        indexlayout.setVisibility(View.VISIBLE);

        klinelandimpl = new KLineLandImpl(oThis, combinedchart, combinedchart2);
        klinelandimpl.setOnKLineHighLightListener(new KLineLandImpl.OnKLineHighLightListener() {
            @Override
            public void onKLineHighLight(DataParse Data, int dataSetIndex, boolean isvisibility) {
                if (mListener != null) {
                    mListener.onKLineFragment(Data, dataSetIndex, isvisibility);
                }
            }
        });

//        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(oThis, R.layout.singletext_index, Consts.kxzbName);
        for(int i=0;i<Consts.kxzbName.length;i++){
            Map<String,String> map = new HashMap<String,String>();
            map.put("indexName",Consts.kxzbName[i]);
            if(i == mKLineIndex){
                map.put("isSelected","1");
            }else{
                map.put("isSelected","0");
            }
            list.add(map);
        }
        madapter = new CommonAdapter<Map<String,String>>(
                oThis, list, R.layout.singletext_index) {

            @Override
            public void convert(ViewHolder helper, Map<String,String> temp) {
                TextView textView = helper.getView(R.id.single_text);
                textView.setText(temp.get("indexName"));
                if(temp.get("isSelected").equals("1")){
                    textView.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
                }else{
                    textView.setTextColor(ContextCompat.getColor(oThis, R.color.gray));
                }
            }
        };
        index.setAdapter(madapter);
        index.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mKLineIndex = pos;
                for(int i=0;i<Consts.kxzbName.length;i++){
                    if(i == mKLineIndex){
                        list.get(i).put("isSelected","1");
                    }else{
                        list.get(i).put("isSelected","0");
                    }
                }
                madapter.notifyDataSetChanged();
                p.sendRequest(new kxDataMessage());//发送请求
            }
        });
        if (mKLineRehabilitation == 0) {
            bfq.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
        } else {
            qfq.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
        }


        p.sendRequest(new kxDataMessage());//发送请求

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

    @OnClick({R.id.bfq, R.id.qfq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bfq:
                bfq.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
                qfq.setTextColor(ContextCompat.getColor(oThis, R.color.gray));
                mKLineRehabilitation = 0;
                p.sendRequest(new kxDataMessage());//发送请求
                break;
            case R.id.qfq:
                bfq.setTextColor(ContextCompat.getColor(oThis, R.color.gray));
                qfq.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
                mKLineRehabilitation = 1;
                p.sendRequest(new kxDataMessage());//发送请求
                break;
        }
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

            klinelandimpl.setData(Data);
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
        void onKLineFragment(DataParse Data, int dataSetIndex, boolean isvisibility);
    }
}
