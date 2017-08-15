package com.android.mystock.ui.marketpages.hqlist;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.android.mystock.common.views.crefreshlayout.CRefreshLayout;
import com.android.mystock.data.consts.ENOStyle;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.fskx.FsKxActivity;
import com.android.mystock.ui.marketpages.mvp.view.I_View_HS;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 沪深
 */
public class HsFragment extends BaseHqFragment {

    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.zfbimg)
    ImageView zfimg;
    @BindView(R.id.zfb)
    NoScrollListView mNoScrollListView_zf;
    @BindView(R.id.dfbimg)
    ImageView dfimg;
    @BindView(R.id.dfb)
    NoScrollListView mNoScrollListView_df;
    @BindView(R.id.hslimg)
    ImageView hslimg;
    @BindView(R.id.hslb)
    NoScrollListView mNoScrollListView_hsl;
    @BindView(R.id.cjeimg)
    ImageView cjeimg;
    @BindView(R.id.cjeb)
    NoScrollListView mNoScrollListView_cje;
    @BindView(R.id.crefreshLayout)
    CRefreshLayout crefreshLayout;

    public CommonAdapter<BeanHqMaket> madapter_zs;
    public CommonAdapter<BeanHqMaket> madapter_zf;
    public CommonAdapter<BeanHqMaket> madapter_df;
    public CommonAdapter<BeanHqMaket> madapter_hsl;
    public CommonAdapter<BeanHqMaket> madapter_cje;

    private ArrayList<BeanHqMaket> list_zs;
    private ArrayList<BeanHqMaket> list_zf;
    private ArrayList<BeanHqMaket> list_df;
    private ArrayList<BeanHqMaket> list_hsl;
    private ArrayList<BeanHqMaket> list_cje;
    private boolean zfbvisibility = true;
    private boolean dfbvisibility = false;
    private boolean hslvisibility = false;
    private boolean cjevisibility = false;

    public static HsFragment newInstance() {
        HsFragment fragment = new HsFragment();
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
        view = inflater.inflate(R.layout.fragment_hs, container, false);
        ButterKnife.bind(this, view);
        init();//初始化

        // 下拉刷新
        crefreshLayout.setOnRefreshListener(new CRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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

        int itemWidth = ScreenUtils.getScreenWidth(oThis) / 3;
        gridview.setColumnWidth(itemWidth); // 设置列表项宽
        gridview.setNumColumns(3); // 设置列数

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list_zs.get(position).getValue_newPrice() > 0) {//没有数据的时候不让点击进入股票详情页
                    Intent intent = new Intent(oThis, FsKxActivity.class);
                    intent.putExtra("stockCode", list_zs.get(position).getStockCode());
                    intent.putExtra("stockName", list_zs.get(position).getStockName());
                    intent.putExtra("maketID", list_zs.get(position).getMaketID());
                    intent.putExtra("stockListIndex", position);
                    intent.putParcelableArrayListExtra("stockList", list_zs);
                    startActivity(intent);
                }
            }
        });
        mNoScrollListView_zf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_zf.get(position).getStockCode());
                intent.putExtra("stockName", list_zf.get(position).getStockName());
                intent.putExtra("maketID", list_zf.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_zf);
                startActivity(intent);
            }
        });
        mNoScrollListView_df.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_df.get(position).getStockCode());
                intent.putExtra("stockName", list_df.get(position).getStockName());
                intent.putExtra("maketID", list_df.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_df);
                startActivity(intent);
            }
        });
        mNoScrollListView_hsl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_hsl.get(position).getStockCode());
                intent.putExtra("stockName", list_hsl.get(position).getStockName());
                intent.putExtra("maketID", list_hsl.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_hsl);
                startActivity(intent);
            }
        });
        mNoScrollListView_cje.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", list_cje.get(position).getStockCode());
                intent.putExtra("stockName", list_cje.get(position).getStockName());
                intent.putExtra("maketID", list_cje.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", list_cje);
                startActivity(intent);
            }
        });
    }

    // 页面单击事件

    @OnClick({R.id.zfbmore, R.id.zfbrela, R.id.dfbmore, R.id.dfbrela, R.id.hslmore, R.id.hslrela, R.id.cjemore, R.id.cjerela})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zfbmore:
                break;
            case R.id.zfbrela:
                if (mNoScrollListView_zf.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_zf.setVisibility(View.GONE);
                    zfimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    zfbvisibility = false;
                } else {
                    mNoScrollListView_zf.setVisibility(View.VISIBLE);
                    zfimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    zfbvisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;
            case R.id.dfbmore:
                break;
            case R.id.dfbrela:
                if (mNoScrollListView_df.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_df.setVisibility(View.GONE);
                    dfimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    dfbvisibility = false;
                } else {
                    mNoScrollListView_df.setVisibility(View.VISIBLE);
                    dfimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    dfbvisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;
            case R.id.hslmore:
                break;
            case R.id.hslrela:
                if (mNoScrollListView_hsl.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_hsl.setVisibility(View.GONE);
                    hslimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    hslvisibility = false;
                } else {
                    mNoScrollListView_hsl.setVisibility(View.VISIBLE);
                    hslimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    hslvisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;
            case R.id.cjemore:
                break;
            case R.id.cjerela:
                if (mNoScrollListView_cje.getVisibility() == View.VISIBLE) {
                    mNoScrollListView_cje.setVisibility(View.GONE);
                    cjeimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jia));
                    cjevisibility = false;
                } else {
                    mNoScrollListView_cje.setVisibility(View.VISIBLE);
                    cjeimg.setImageDrawable(getResources().getDrawable(
                            R.mipmap.jian));
                    cjevisibility = true;
                    p.sendRequest(new RankDataMessage());//发送请求
                }
                break;
        }
    }

    //具体显示指数数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleData(ViewHolder helper, BeanHqMaket temp) {
        View rootview = helper.getView(R.id.zsview);

        TextView textview1 = helper.getView(R.id.zjcj);
        ImageView img4 = helper.getView(R.id.zhangdie);

        helper.setText(R.id.name, temp.getStockName());
        helper.setText(R.id.zjcj, temp.getNewPrice());
        helper.setText(R.id.zhd, temp.getZde());
        helper.setText(R.id.zdf, temp.getZdf());

        if (temp.getState() < 0) {
            img4.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.zsdie));
            textview1.setTextColor(ENOStyle.hq_down);
        } else if (temp.getState() > 0) {
            img4.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.zszhang));
            textview1.setTextColor(ENOStyle.hq_up);
        } else {
            img4.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.zszhang));
            textview1.setTextColor(ENOStyle.hq_flat);
        }
    }

    //排行榜数据
    private void handleRankData(int flag, ViewHolder helper, BeanHqMaket temp) {

        helper.setText(R.id.my_stock_list_stock_name, temp.getStockName());
        helper.setText(R.id.my_stock_list_stock_code, temp.getStockCode());
        TextView textview1 = helper.getView(R.id.my_stock_list_stock_price);
        TextView textview2 = helper.getView(R.id.my_stock_list_stock_Proportion);
        textview1.setText(temp.getNewPrice());

        if (flag == 2) {//换手率榜
            textview2.setText(temp.getHsl());
        } else if (flag == 3) {//成交额榜
            textview2.setText(temp.getCje());
        } else {//涨幅榜，跌幅榜
            textview2.setText(temp.getZdf());
            if (temp.getState() < 0) {
                textview2.setTextColor(ENOStyle.hq_down);
            } else if (temp.getState() > 0) {
                textview2.setTextColor(ENOStyle.hq_up);
            } else {
                textview2.setTextColor(ENOStyle.hq_flat);
            }
        }

        if (temp.getState() < 0) {
            textview1.setTextColor(ENOStyle.hq_down);
        } else if (temp.getState() > 0) {
            textview1.setTextColor(ENOStyle.hq_up);
        } else {
            textview1.setTextColor(ENOStyle.hq_flat);
        }
    }


    /*
    数据回调
     */
    class RankDataMessage implements I_View_HS {
        @Override
        public boolean getVisibilityZfb() {
            return zfbvisibility;
        }

        @Override
        public boolean getVisibilityDfb() {
            return dfbvisibility;
        }

        @Override
        public boolean getVisibilityHsl() {
            return hslvisibility;
        }

        @Override
        public boolean getVisibilityCje() {
            return cjevisibility;
        }

        //指数
        @Override
        public void onResponseZs(ArrayList<BeanHqMaket> list) {
            crefreshLayout.setRefreshing(false);
            list_zs = list;
            madapter_zs = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_item_zs) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleData(helper, temp);
                }
            };

            gridview.setAdapter(madapter_zs);// 设置菜单Adapter
            madapter_zs.notifyDataSetChanged();
        }

        //涨幅榜
        @Override
        public void onResponseZfb(ArrayList<BeanHqMaket> list) {
            list_zf = list;
            madapter_zf = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(0, helper, temp);
                }
            };
            mNoScrollListView_zf.setAdapter(madapter_zf);
            madapter_zf.notifyDataSetChanged();

        }

        //跌幅榜
        @Override
        public void onResponseDfb(ArrayList<BeanHqMaket> list) {
            list_df = list;
            madapter_df = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(1, helper, temp);
                }
            };
            mNoScrollListView_df.setAdapter(madapter_df);
            madapter_df.notifyDataSetChanged();
        }

        //换手率榜
        @Override
        public void onResponseHsl(ArrayList<BeanHqMaket> list) {
            list_hsl = list;
            madapter_hsl = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(2, helper, temp);
                }
            };
            mNoScrollListView_hsl.setAdapter(madapter_hsl);
            madapter_hsl.notifyDataSetChanged();
        }

        @Override
        public void onResponseCje(ArrayList<BeanHqMaket> list) {
            list_cje = list;
            madapter_cje = new CommonAdapter<BeanHqMaket>(
                    oThis, list, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleRankData(3, helper, temp);
                }
            };
            mNoScrollListView_cje.setAdapter(madapter_cje);
            madapter_cje.notifyDataSetChanged();
        }

        //成交额榜
        @Override
        public void errorMessage(int errorId, String error) {
            crefreshLayout.setRefreshing(false);
        }
    }
}
