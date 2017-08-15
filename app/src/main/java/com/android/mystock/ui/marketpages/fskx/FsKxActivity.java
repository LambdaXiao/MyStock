package com.android.mystock.ui.marketpages.fskx;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.NumberTextView;
import com.android.mystock.ui.base.BaseActivity;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.bean.Stock;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 分时K线页面
 */
public class FsKxActivity extends BaseActivity {

    public FragmentManager fManager;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.titleCode)
    NumberTextView titleCode;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.topid)
    FrameLayout topid;

    private ArrayList<BeanHqMaket> stockList;//股票列表
    private ArrayList<FsKxDetailsFragment> fragments;
    private int stockListIndex;//当前位置索引用
    private Stock stock;
    private MyPagerAdapter adapter;


    @Override
    protected void onPause() {
        super.onPause();
        stopAutoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoRefresh();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_activity_fskx);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Logs.e("++++++++++++++重启了+++++++++++++++++++++");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            // 4.4以上处理
            oThis.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            if (topid != null) {
                ViewGroup.LayoutParams pars = topid.getLayoutParams();
                pars.height = DensityUtils.dp2px(oThis, 48) + ScreenUtils.getStatusHeight(oThis);
                topid.setLayoutParams(pars);
                topid.setPadding(0, ScreenUtils.getStatusHeight(oThis), 0, 0);// 设置上边距为状态栏高度getStatusBarHeight()
            }
        }
        topid.setBackgroundColor(ContextCompat.getColor(oThis,R.color.topbarbackground));

        fragments = new ArrayList<FsKxDetailsFragment>();
        Intent intent = this.getIntent();
        fManager = this.getSupportFragmentManager();
        stockListIndex = intent.getIntExtra("stockListIndex", -1);
        if (stockListIndex > -1) {
            stockList = intent.getParcelableArrayListExtra("stockList");
            stock = stockList.get(stockListIndex);
            setTitleName(stock);
        } else {
            String stockCode = intent.getStringExtra("stockCode");
            String stockName = intent.getStringExtra("stockName");
            int classId = intent.getIntExtra("classId", 100);
            int maketID = intent.getIntExtra("maketID", -1);
            stock = new BeanHqMaket();
            stock.setStockCode(stockCode);
            stock.setStockName(stockName);
            stock.setClassId(classId);
            stock.setMaketID(maketID);
            stockListIndex = 0;
            setTitleName(stock);
        }
        if (stockList != null && stockList.size() > 0) {
            for (int i = 0; i < stockList.size(); i++) {
                fragments.add(FsKxDetailsFragment.newInstance(stockList.get(i)));
            }

        } else {
            fragments.add(FsKxDetailsFragment.newInstance(stock));

        }

        adapter = new MyPagerAdapter(fManager);
        vp.setOffscreenPageLimit(1);

        vp.setAdapter(adapter);
        vp.setCurrentItem(stockListIndex);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                stock = stockList.get(position);
                setTitleName(stock);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * 设置股票
     *
     * @param stock
     */
    private void setTitleName(Stock stock) {

        String stockCode = stock.getStockCode();
        String stockName = stock.getStockName();
        titleText.setText(stockName);
        titleCode.setText(stockCode);
    }


    /**
     * pageAdapter
     */
    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if (fragments == null) {
                return 0;
            } else {
                return fragments.size();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public FsKxDetailsFragment getItem(int position) {
            return fragments.get(position);
        }
    }

    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {

        adapter.getItem(vp.getCurrentItem()).autoRefresh();

    }


}
