package com.android.mystock.ui.tradepages;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.mystock.R;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.ui.base.BaseFragment;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 交易页
 */
public class FragmentMainTrade extends BaseFragment {

    @BindView(R.id.top_id)
    FrameLayout topId;
    @BindView(R.id.tl_1)
    SegmentTabLayout mTabLayout;
    @BindView(R.id.viewpageTrade)
    ViewPager mViewPager;

    private ArrayList<Fragment> mFragments;
    private String[] mTitles = {"普通交易", "信用交易"};

    public static FragmentMainTrade newInstance() {
        FragmentMainTrade fragment = new FragmentMainTrade();
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
        view = inflater.inflate(R.layout.fragment_main_trade, container, false);
        ButterKnife.bind(this, view);

        init();//调用基类初始化头部标题栏

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            // 4.4以上处理
            oThis.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            if (topId != null) {
                ViewGroup.LayoutParams pars = topId.getLayoutParams();
                pars.height = DensityUtils.dp2px(oThis, 48) + ScreenUtils.getStatusHeight(oThis);
                topId.setLayoutParams(pars);
                topId.setPadding(0, ScreenUtils.getStatusHeight(oThis), 0, 0);// 设置上边距为状态栏高度getStatusBarHeight()
            }
        }
        mFragments = new ArrayList<>();
        mFragments.add(OrdinaryTradeFragment.newInstance());//普通交易
        mFragments.add(CreditTradeFragment.newInstance());//信用交易

        mTabLayout.setTabData(mTitles);

        mViewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
        return view;
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
