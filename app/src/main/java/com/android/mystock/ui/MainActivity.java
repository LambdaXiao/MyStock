package com.android.mystock.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.views.viewpager.NoScrollViewPager;
import com.android.mystock.data.Setting;
import com.android.mystock.ui.base.BaseActivity;
import com.android.mystock.ui.base.BaseFragment;
import com.android.mystock.ui.homepages.FragmentMainHome;
import com.android.mystock.ui.marketpages.FragmentMainMarket;
import com.android.mystock.ui.minepages.FragmentMainMe;
import com.android.mystock.ui.optionalpages.FragmentMainOptional;
import com.android.mystock.ui.tradepages.FragmentMainTrade;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页底部导航Tab
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager mPager;
    @BindView(R.id.tabbar)
    JPTabBar mTabbar;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private FragmentMainHome fragmentMainHome;//首页
    private FragmentMainMarket fragmentMainQuote;// 行情
    private FragmentMainOptional fragmentMainOptional;// 自选股
    private FragmentMainTrade fragmentMainTrade;//交易
    private FragmentMainMe fragmentMainMe;//我的

    private String[] mStringItems = {"白色主题", "黑色主题"};
    private BaseAnimatorSet mBasIn;
    private BaseAnimatorSet mBasOut;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mBasIn = new BounceTopEnter();
        mBasOut = new SlideBottomExit();
        navView.setNavigationItemSelectedListener(this);
        currentPageId = this.getIntent().getIntExtra("pageIndex", 0);

        fragmentMainHome = FragmentMainHome.newInstance();//首页
        fragmentMainQuote = FragmentMainMarket.newInstance();//行情
        fragmentMainOptional = FragmentMainOptional.newInstance(); //自选股
        fragmentMainTrade = FragmentMainTrade.newInstance();//交易
        fragmentMainMe = FragmentMainMe.newInstance();//我的
        mFragments.clear();
        mFragments.add(fragmentMainHome);
        mFragments.add(fragmentMainQuote);
        mFragments.add(fragmentMainOptional);
        mFragments.add(fragmentMainTrade);
        mFragments.add(fragmentMainMe);

        mPager.setNoScroll(true);
        mAdapter = new Adapter(getSupportFragmentManager(),mFragments);
        mPager.setAdapter(mAdapter);
        mTabbar.setTitles("首页", "行情", "自选", "交易", "我的")
                .setNormalIcons(R.mipmap.tb1, R.mipmap.tb2,
                        R.mipmap.tb4, R.mipmap.tb3, R.mipmap.tb5)
                .setSelectedIcons(R.mipmap.tb1_1, R.mipmap.tb2_2,
                        R.mipmap.tb4_2, R.mipmap.tb3_2, R.mipmap.tb5_2)
                .generate();

        //显示圆点模式的徽章
        //设置容器
        mTabbar.showCircleBadge(1);
        mTabbar.setContainer(mPager);
        //设置Badge消失的代理
        mTabbar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int index) {
                if(index == 1) {
                    mTabbar.hideBadge(1);
                }
            }

            @Override
            public void onClickMiddle(View middleBtn) {

            }
        });

        MenuItem menuNightMode = navView.getMenu().findItem(R.id.nav_setting);
        TextView skinname = (TextView) MenuItemCompat.getActionView(menuNightMode);
        if (Setting.getNightMode(this)) {//获取保存的是哪个肤色
            skinname.setText(mStringItems[1]);
        } else {
            skinname.setText(mStringItems[0]);
        }
        setNightOrDayMode();
    }

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
    public void autoRefresh() {
//        Logs.e("MainActivity");
        if (mAdapter != null) {
            BaseFragment baseFragment = (BaseFragment) mAdapter.getItem(mPager.getCurrentItem());
            if (baseFragment != null) {
                baseFragment.autoRefresh();
            }
        }
    }

    private void setNightOrDayMode() {
        if (Setting.getNightMode(this)) {//获取保存的是哪个肤色
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * 退出程序
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            final MaterialDialog dialog = new MaterialDialog(oThis);
            dialog.isTitleShow(false)//
                    .content("您确定退出程序么？")//
                    .contentTextSize(18)
                    .contentTextColor(Color.GRAY)//
                    .btnText("取消", "退出")//
                    .showAnim(new BounceTopEnter())//
                    .dismissAnim(new SlideBottomExit())
                    .show();

            dialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                }
            }, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    // 设置你的操作事项
                    // 清空登录信息
                    dialog.superDismiss();
                    finish();
                    System.exit(0);
                }
            });
        }
    }

    /**
     * 侧滑菜单
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_gallery) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_slideshow) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_manage) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_setting) {
            NormalListDialog();
        }

        return true;
    }

    /**
     * 换肤弹出框
     */
    private void NormalListDialog() {
        final NormalListDialog dialog = new NormalListDialog(oThis, mStringItems);
        dialog.title("请选择")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)//
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
//                skinname.setText(mStringItems[position]);
                if (position == 0) {
                    Setting.setNightMode(oThis, false);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    Setting.setNightMode(oThis, true);
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                recreate();
                dialog.dismiss();

            }
        });
    }

    public class Adapter extends FragmentPagerAdapter {
        private List<BaseFragment> list;

        public Adapter(FragmentManager fm, List<BaseFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public BaseFragment getItem(int position) {
            return list.get(position);
        }


        @Override
        public int getCount() {
            return list.size();
        }
    }

}
