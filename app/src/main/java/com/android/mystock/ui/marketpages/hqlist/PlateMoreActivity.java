package com.android.mystock.ui.marketpages.hqlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.adapters.CommonAdapter;
import com.android.mystock.common.adapters.ViewHolder;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.pullableview.PullToRefreshLayout;
import com.android.mystock.common.views.pullableview.PullableListView;
import com.android.mystock.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
板块更多页面
 */
public class PlateMoreActivity extends BaseActivity {

    @BindView(R.id.top_title)
    TextView title;
    @BindView(R.id.list)
    PullableListView mListView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout ptrl;
    @BindView(R.id.top_refresh)
    RippleView topRefresh;

    private CommonAdapter<Map<String, String>> madapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_plate_more);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        title.setText("热门行业");
        topRefresh.setVisibility(View.GONE);
        if (searchButton != null) {
            searchButton.setVisibility(View.VISIBLE);
        }

        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                // 下拉刷新操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 2000);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                // 加载操作
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 2000);
            }
        });

        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 20; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("platename", "公共交通");
            map.put("stockzdf", "10.01%");
            map.put("stockname", "莱克电气");
            list.add(map);
        }
        madapter = new CommonAdapter<Map<String, String>>(oThis, list, R.layout.list_3item) {
            @Override
            public void convert(ViewHolder helper, Map<String, String> item) {
                helper.setText(R.id.platename, item.get("platename"));
                helper.setText(R.id.stockzdf, item.get("stockzdf"));
                helper.setText(R.id.stockname, item.get("stockname"));
            }
        };
        mListView.setAdapter(madapter);
        madapter.notifyDataSetChanged();
    }
}
