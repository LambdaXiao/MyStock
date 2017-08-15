package com.android.mystock.ui.marketpages;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.mystock.R;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.ui.base.BaseFragment;
import com.android.mystock.ui.marketpages.hqlist.HKFragment;
import com.android.mystock.ui.marketpages.hqlist.HsFragment;
import com.android.mystock.ui.marketpages.hqlist.MAFragment;
import com.android.mystock.ui.marketpages.hqlist.MoreFragment;
import com.android.mystock.ui.marketpages.hqlist.PlateFragment;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

/**
 * 行情页
 */
public class FragmentMainMarket extends BaseFragment {

    @BindView(R.id.edit_zxg)
    RippleView editZxg;//编辑
    @BindView(R.id.top_id)
    FrameLayout topId;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    public FragmentManager fManager;
    private ArrayList<BaseFragment> fragments;
    private String[] mTitles = {};//沪深,板块,港股,港股通，更多
    private ArrayList<CustomTabEntity> mTab = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;

    public static FragmentMainMarket newInstance() {
        FragmentMainMarket fragment = new FragmentMainMarket();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitles = oThis.getResources().getStringArray(R.array.hqtab);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_market, container, false);
        ButterKnife.bind(this, view);

        init();//调用基类初始化头部标题栏

        editZxg.setVisibility(View.GONE);

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

        fManager = this.getChildFragmentManager();
        fragments = new ArrayList<BaseFragment>();

        fragments.add(HsFragment.newInstance());//沪深
        fragments.add(PlateFragment.newInstance());//板块
        fragments.add(HKFragment.newInstance());//港股
        fragments.add(MAFragment.newInstance());//港股通
        fragments.add(MoreFragment.newInstance());//更多

        myPagerAdapter = new MyPagerAdapter(fManager);
        vp.setAdapter(myPagerAdapter);

        vp.setOffscreenPageLimit(1);
        vp.setCurrentItem(0);//默认进入沪深页

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(vp);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(myPagerAdapter);

        return view;
    }


    @Override
    public void autoRefresh() {
        Logs.e("FragmentMainQuote.java" + vp.getCurrentItem());
        if (myPagerAdapter != null) {
            BaseFragment baseFragment = myPagerAdapter.getItem(vp.getCurrentItem());
            if (baseFragment != null) {
                baseFragment.autoRefresh();
            }
        }

    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public BaseFragment getItem(int position) {
            return fragments.get(position);
        }
    }

}
