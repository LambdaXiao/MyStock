package com.android.mystock.ui.marketpages.hqlist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.pullableview.PullToRefreshLayout;
import com.android.mystock.common.views.pullableview.PullableHVListView;
import com.android.mystock.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/*
行情更多
 */
public class MoreActivity extends BaseActivity {

    @BindView(R.id.top_title)
    TextView title;
    @BindView(R.id.head)
    LinearLayout head;
    @BindView(R.id.list)
    PullableHVListView mListView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout ptrl;
    @BindView(R.id.top_refresh)
    RippleView topRefresh;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        title.setText("股票列表");
        topRefresh.setVisibility(View.GONE);
        if (searchButton != null) {
            searchButton.setVisibility(View.VISIBLE);
        }

        //设置列头
        mListView.mListHead = head;
        //设置数据
        mListView.setAdapter(new DataAdapter());

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
                }.sendEmptyMessageDelayed(0, 3000);
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
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });
    }

    private class DataAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 30;//固定显示30行数据
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(oThis).inflate(R.layout.list_item_stockdata, null);
            }

            for (int i = 0; i < 8; i++) {
                ((TextView) convertView.findViewById(R.id.item2 + i)).setText("数据" + position + "行" + (i + 2) + "列");
            }

            //校正（处理同时上下和左右滚动出现错位情况）
            View child = ((ViewGroup) convertView).getChildAt(1);
            int head = mListView.getHeadScrollX();
            if (child.getScrollX() != head) {
                child.scrollTo(mListView.getHeadScrollX(), 0);
            }
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
