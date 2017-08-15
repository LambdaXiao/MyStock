package com.android.mystock.ui.marketpages.hqlist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.adapters.CommonAdapter;
import com.android.mystock.common.adapters.ViewHolder;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.gridview.NoScrollGridView;
import com.android.mystock.common.views.smartisanpull.SmartisanRefreshableScrollview;
import com.android.mystock.data.consts.ENOStyle;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.fskx.FsKxActivity;
import com.android.mystock.ui.marketpages.mvp.view.I_View_ZS;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指数
 */
public class PlateFragment extends BaseHqFragment {

    @BindView(R.id.moreplate1)
    FrameLayout moreplate1;
    @BindView(R.id.gridview1)
    NoScrollGridView gridview1;
    @BindView(R.id.moreplate2)
    FrameLayout moreplate2;
    @BindView(R.id.gridview2)
    NoScrollGridView gridview2;
    @BindView(R.id.moreplate3)
    FrameLayout moreplate3;
    @BindView(R.id.gridview3)
    NoScrollGridView gridview3;
    @BindView(R.id.refresh_root)
    SmartisanRefreshableScrollview refreshLayout;

    private ArrayList<BeanHqMaket> lists1 = new ArrayList<BeanHqMaket>();
    private ArrayList<BeanHqMaket> lists2 = new ArrayList<BeanHqMaket>();
    private ArrayList<BeanHqMaket> lists3 = new ArrayList<BeanHqMaket>();
    private CommonAdapter<BeanHqMaket> mAdapter1;//三个适配器
    private CommonAdapter<BeanHqMaket> mAdapter2;
    private CommonAdapter<BeanHqMaket> mAdapter3;

    public PlateFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlateFragment newInstance() {
        PlateFragment fragment = new PlateFragment();

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
        view = inflater.inflate(R.layout.fragment_plate, container, false);
        ButterKnife.bind(this, view);
        init();//初始化

        // 下拉刷新
        refreshLayout.setOnRefreshListener(new SmartisanRefreshableScrollview.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                p.sendRequest(new ZsDataMessage());//发送请求
            }

            @Override
            public void onRefreshFinished() {

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
        p.sendRequest(new ZsDataMessage());//发送请求
    }

    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {
        super.autoRefresh();
        p.sendRequest(new ZsDataMessage());
    }

    public void init() {

        int itemWidth = ScreenUtils.getScreenWidth(oThis) / 3;
        gridview1.setColumnWidth(itemWidth); // 设置列表项宽
        gridview1.setNumColumns(3); // 设置列数
        gridview2.setColumnWidth(itemWidth); // 设置列表项宽
        gridview2.setNumColumns(3); // 设置列数
        gridview3.setColumnWidth(itemWidth); // 设置列表项宽
        gridview3.setNumColumns(3); // 设置列数
        lists1.clear();
        lists2.clear();
        lists3.clear();
        for (int i = 0; i < 6; i++) {
            BeanHqMaket stock = new BeanHqMaket();
            stock.setStockName("--");
            stock.setValue_newPrice(0);
            stock.setValue_zdf(0);
            stock.setValue_zde(0);
            lists1.add(stock);
            lists2.add(stock);
            lists3.add(stock);
        }
        gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lists1.get(position).getValue_newPrice() > 0) {//没有数据的时候不让点击进入股票详情页
                    Intent intent = new Intent(oThis, FsKxActivity.class);
                    intent.putExtra("stockCode", lists1.get(position).getStockCode());
                    intent.putExtra("stockName", lists1.get(position).getStockName());
                    intent.putExtra("maketID", lists1.get(position).getMaketID());
                    intent.putExtra("stockListIndex", position);
                    intent.putParcelableArrayListExtra("stockList", lists1);
                    startActivity(intent);
                }
            }
        });

        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lists2.get(position).getValue_newPrice() > 0) {
                    Intent intent = new Intent(oThis, FsKxActivity.class);
                    intent.putExtra("stockCode", lists2.get(position).getStockCode());
                    intent.putExtra("stockName", lists2.get(position).getStockName());
                    intent.putExtra("maketID", lists2.get(position).getMaketID());
                    intent.putExtra("stockListIndex", position);
                    intent.putParcelableArrayListExtra("stockList", lists2);
                    startActivity(intent);
                }
            }
        });

        gridview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lists3.get(position).getValue_newPrice() > 0) {
                    Intent intent = new Intent(oThis, FsKxActivity.class);
                    intent.putExtra("stockCode", lists3.get(position).getStockCode());
                    intent.putExtra("stockName", lists3.get(position).getStockName());
                    intent.putExtra("maketID", lists3.get(position).getMaketID());
                    intent.putExtra("stockListIndex", position);
                    intent.putParcelableArrayListExtra("stockList", lists3);
                    startActivity(intent);
                }
            }
        });

        mAdapter1 = new CommonAdapter<BeanHqMaket>(
                oThis, lists1, R.layout.list_item_zs) {

            @Override
            public void convert(ViewHolder helper, BeanHqMaket temp) {
                handleData(helper, temp);
            }
        };
        gridview1.setAdapter(mAdapter1);// 设置菜单Adapter


        mAdapter2 = new CommonAdapter<BeanHqMaket>(
                oThis, lists2, R.layout.list_item_zs) {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void convert(ViewHolder helper, BeanHqMaket temp) {
                handleData(helper, temp);
            }
        };
        gridview2.setAdapter(mAdapter2);// 设置菜单Adapter


        mAdapter3 = new CommonAdapter<BeanHqMaket>(
                oThis, lists3, R.layout.list_item_zs) {

            @Override
            public void convert(ViewHolder helper, BeanHqMaket temp) {
                handleData(helper, temp);
            }
        };
        gridview3.setAdapter(mAdapter3);// 设置菜单Adapter


        view.findViewById(R.id.moreplate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(oThis, PlateMoreActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    请求数据回调
     */
    private class ZsDataMessage implements I_View_ZS {
        @Override
        public void onResponse(ArrayList<BeanHqMaket> list1, ArrayList<BeanHqMaket> list2, ArrayList<BeanHqMaket> list3) {
            refreshLayout.finishRefreshing();
            if (list1.size() > 0) {
                lists1.clear();
                lists1 = list1;
            }
            if (list2.size() > 0) {
                lists2.clear();
                lists2 = list2;
            }
            if (list3.size() > 0) {
                lists3.clear();
                lists3 = list3;
            }
            mAdapter1.setList(lists1);
            mAdapter2.setList(lists2);
            mAdapter3.setList(lists3);
            mAdapter1.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            mAdapter3.notifyDataSetChanged();
        }

        @Override
        public void errorMessage(int errorId, String error) {
            refreshLayout.finishRefreshing();
        }
    }

    //具体显示数据
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void handleData(ViewHolder helper, BeanHqMaket temp) {
        View rootview = helper.getView(R.id.zsview);
        rootview.setBackground(ContextCompat.getDrawable(oThis,R.drawable.main_list_pressed));

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
}
