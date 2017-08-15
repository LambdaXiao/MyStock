package com.android.mystock.ui.marketpages.hqlist;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.adapters.CommonAdapter;
import com.android.mystock.common.adapters.ViewHolder;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.ListView.NoScrollListView;
import com.android.mystock.data.consts.ENOStyle;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.fskx.FsKxActivity;
import com.android.mystock.ui.marketpages.mvp.view.I_View_HS;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 港股通
 */
public class MAFragment extends BaseHqFragment {

    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.shimg)
    ImageView shimg;
    @BindView(R.id.mashlist)
    NoScrollListView mNoScrollListView_sh;
    @BindView(R.id.szimg)
    ImageView szimg;
    @BindView(R.id.maszlist)
    NoScrollListView mNoScrollListView_sz;
    @BindView(R.id.refresh)
    TwinklingRefreshLayout refresh;

    public CommonAdapter<BeanHqMaket> madapter_zs;
    public CommonAdapter<BeanHqMaket> madapter_sh;
    public CommonAdapter<BeanHqMaket> madapter_sz;
    private ArrayList<BeanHqMaket> list_zs;
    private ArrayList<BeanHqMaket> list_sh;
    private ArrayList<BeanHqMaket> list_sz;
    private boolean shbvisibility = true;
    private boolean szbvisibility = false;

    public static MAFragment newInstance() {
        MAFragment fragment = new MAFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ma, container, false);
        ButterKnife.bind(this, view);
        init();//初始化

        // 下拉刷新
        SinaRefreshView headerView = new SinaRefreshView(oThis);
        refresh.setHeaderView(headerView);
        refresh.setOverScrollRefreshShow(false);
        refresh.setEnableLoadmore(false);
        refresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                p.sendRequest(new RankDataMessage());//发送请求
            }

        });

        this.isPrepared = true;
        setlazyLoad();


        return view;
    }


    /*
加载数据
*/
    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }
        p.sendRequest(new RankDataMessage());//发送请求
    }

    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {
        super.autoRefresh();
        p.sendRequest(new RankDataMessage());//发送请求
    }

    public void init() {

        int itemWidth = ScreenUtils.getScreenWidth(oThis) / 2;
        gridview.setColumnWidth(itemWidth); // 设置列表项宽
        gridview.setNumColumns(2); // 设置列数

        mNoScrollListView_sh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_sh.get(position).getStockCode());
                intent.putExtra("stockName", list_sh.get(position).getStockName());
                intent.putExtra("maketID", list_sh.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_sh);
                startActivity(intent);
            }
        });
        mNoScrollListView_sz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_sz.get(position).getStockCode());
                intent.putExtra("stockName", list_sz.get(position).getStockName());
                intent.putExtra("maketID", list_sz.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_sz);
                startActivity(intent);
            }
        });

    }


    //排行榜数据
    private void handleRankData(int flag, ViewHolder helper, BeanHqMaket temp) {

        helper.setText(R.id.my_stock_list_stock_name, temp.getStockName());
        helper.setText(R.id.my_stock_list_stock_code, temp.getStockCode());
        TextView textview1 = helper.getView(R.id.my_stock_list_stock_price);
        TextView textview2 = helper.getView(R.id.my_stock_list_stock_Proportion);
        textview1.setText(temp.getNewPrice());


        textview2.setText(temp.getZdf());
        if (temp.getState() < 0) {
            textview2.setTextColor(ENOStyle.hq_down);
        } else if (temp.getState() > 0) {
            textview2.setTextColor(ENOStyle.hq_up);
        } else {
            textview2.setTextColor(ENOStyle.hq_flat);
        }


        if (temp.getState() < 0) {
            textview1.setTextColor(ENOStyle.hq_down);
        } else if (temp.getState() > 0) {
            textview1.setTextColor(ENOStyle.hq_up);
        } else {
            textview1.setTextColor(ENOStyle.hq_flat);
        }
    }

    @OnClick({R.id.shmore, R.id.shrela, R.id.szmore, R.id.szrela})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.shrela:
                if (mNoScrollListView_sh.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_sh.setVisibility(View.GONE);
                    shimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    shbvisibility = false;
                } else {
                    mNoScrollListView_sh.setVisibility(View.VISIBLE);
                    shimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    shbvisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;

            case R.id.szrela:
                if (mNoScrollListView_sz.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_sz.setVisibility(View.GONE);
                    szimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    szbvisibility = false;
                } else {
                    mNoScrollListView_sz.setVisibility(View.VISIBLE);
                    szimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    szbvisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;

            case R.id.szmore:

                break;

            case R.id.shmore:

                break;
        }
    }

    /*
    数据回调
     */
    class RankDataMessage implements I_View_HS {
        @Override
        public boolean getVisibilityZfb() {
            return shbvisibility;
        }

        @Override
        public boolean getVisibilityDfb() {
            return szbvisibility;
        }

        @Override
        public boolean getVisibilityHsl() {
            return false;
        }

        @Override
        public boolean getVisibilityCje() {
            return false;
        }


        @Override
        public void onResponseZs(ArrayList<BeanHqMaket> list) {
            list.clear();
            BeanHqMaket b = new BeanHqMaket();
            b.setStockName("沪市港股通");
            b.setStockCode("120.44亿");
            list.add(b);
            BeanHqMaket b1 = new BeanHqMaket();
            b1.setStockName("深市港股通");
            b1.setStockCode("99.45万");
            list.add(b1);

            list_zs = list;
            madapter_zs = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_item_ma) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    helper.setText(R.id.maname, temp.getStockName());
                    helper.setText(R.id.mavalue, temp.getStockCode());
                }
            };

            gridview.setAdapter(madapter_zs);// 设置菜单Adapter
            madapter_zs.notifyDataSetChanged();
        }

        //沪港通
        @Override
        public void onResponseZfb(ArrayList<BeanHqMaket> list) {
            refresh.finishRefreshing();
            list_sh = list;
            madapter_sh = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(0, helper, temp);
                }
            };
            mNoScrollListView_sh.setAdapter(madapter_sh);
            madapter_sh.notifyDataSetChanged();

        }

        //深港通
        @Override
        public void onResponseDfb(ArrayList<BeanHqMaket> list) {
            list_sz = list;
            madapter_sz = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(1, helper, temp);
                }
            };
            mNoScrollListView_sz.setAdapter(madapter_sz);
            madapter_sz.notifyDataSetChanged();
        }

        @Override
        public void onResponseHsl(ArrayList<BeanHqMaket> list) {

        }

        @Override
        public void onResponseCje(ArrayList<BeanHqMaket> list) {

        }

        @Override
        public void errorMessage(int errorId, String error) {
            refresh.finishRefreshing();
        }
    }
}
