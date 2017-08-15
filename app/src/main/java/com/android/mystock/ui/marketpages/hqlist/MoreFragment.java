package com.android.mystock.ui.marketpages.hqlist;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.mystock.R;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.gridview.NoScrollGridView;
import com.android.mystock.ui.base.BaseHqFragment;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 行情里更多页面
 */
public class MoreFragment extends BaseHqFragment {

    @BindView(R.id.gridview1)
    NoScrollGridView gridview1;
    @BindView(R.id.gridview2)
    NoScrollGridView gridview2;
    @BindView(R.id.gridview3)
    NoScrollGridView gridview3;
    @BindView(R.id.gridview4)
    NoScrollGridView gridview4;
    @BindView(R.id.gridview5)
    NoScrollGridView gridview5;
    @BindView(R.id.refresh)
    TwinklingRefreshLayout refresh;

    private String[] hs = {"沪深A股", "沪深B股", "上证A股", "深证A股", "上证B股", "深证B股",
            "中小板", "创业板", ""};
    private String[] zs = {"沪深指数", "香港指数", "全球指数"};
    private String[] hk = {"港股", "深港通", "沪港通", "香港主板", "创业板", ""};
    private String[] jj = {"上证基金", "深圳基金", "LOF", "ETF", "", ""};
    private String[] zq = {"沪深债券", "上证债券", "深圳债券"};


    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();

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
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        int itemWidth = ScreenUtils.getScreenWidth(oThis) / 3;
        gridview1.setColumnWidth(itemWidth); // 设置列表项宽
        gridview1.setNumColumns(3); // 设置列数
        gridview2.setColumnWidth(itemWidth); // 设置列表项宽
        gridview2.setNumColumns(3); // 设置列数
        gridview3.setColumnWidth(itemWidth); // 设置列表项宽
        gridview3.setNumColumns(3); // 设置列数
        gridview4.setColumnWidth(itemWidth); // 设置列表项宽
        gridview4.setNumColumns(3); // 设置列数
        gridview5.setColumnWidth(itemWidth); // 设置列表项宽
        gridview5.setNumColumns(3); // 设置列数

        initdata();
        refresh.setPureScrollModeOn(true);
        return view;
    }

    private void initdata() {

        // 沪深
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(oThis,
                R.layout.singletext, R.id.single_text, hs);
        gridview1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        gridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, MoreActivity.class);
                startActivity(intent);
            }
        });

        // 指数
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(oThis,
                R.layout.singletext, R.id.single_text, zs);
        gridview2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        // 港股
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(oThis,
                R.layout.singletext, R.id.single_text, hk);
        gridview3.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();

        // 基金
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(oThis,
                R.layout.singletext, R.id.single_text, jj);
        gridview4.setAdapter(adapter4);
        adapter4.notifyDataSetChanged();

        // 债券
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(oThis,
                R.layout.singletext, R.id.single_text, zq);
        gridview5.setAdapter(adapter5);
        adapter5.notifyDataSetChanged();

    }

}
