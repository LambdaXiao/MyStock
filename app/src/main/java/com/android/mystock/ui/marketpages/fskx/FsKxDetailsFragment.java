package com.android.mystock.ui.marketpages.fskx;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.beans.FlycoTabEntity;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.NumberTextView;
import com.android.mystock.common.views.scrollview.NoScrollScrollView;
import com.android.mystock.common.views.smartisanpull.SmartisanRefreshableScrollview;
import com.android.mystock.ui.base.BaseFragment;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.HqUtils;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.fskx.f10andnews.F10Fragment;
import com.android.mystock.ui.marketpages.fskx.f10andnews.NewsFragment;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_FSKX;
import com.android.mystock.ui.optionalpages.bean.MyStock;
import com.android.mystock.ui.optionalpages.optionalUtils;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分时K线tab页 外面是个pageView
 */
public class FsKxDetailsFragment extends BaseHqFragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.text_zjcj)
    NumberTextView zjcjView;
    @BindView(R.id.text_zde)
    NumberTextView zdeView;
    @BindView(R.id.text_zdf)
    NumberTextView zdfView;
    @BindView(R.id.text_zg)
    NumberTextView zgView;
    @BindView(R.id.text_zd)
    NumberTextView zdView;
    @BindView(R.id.text_kp)
    NumberTextView jrkpView;
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.text_hsl)
    NumberTextView hslView;
    @BindView(R.id.text_cjl)
    NumberTextView cjslView;
    @BindView(R.id.text_cje)
    NumberTextView cjjeView;
    @BindView(R.id.hideimg)
    ImageView hideimg;
    @BindView(R.id.hidevalue)
    FrameLayout hidevalue;
    @BindView(R.id.hqvalueview)
    LinearLayout hqvalueview;
    @BindView(R.id.tv_zt)
    NumberTextView tvzt;
    @BindView(R.id.tv_dt)
    NumberTextView tvdt;
    @BindView(R.id.tv_zrsp)
    NumberTextView tvzrsp;
    @BindView(R.id.tv_np)
    NumberTextView tvNp;
    @BindView(R.id.tv_wp)
    NumberTextView tvWp;
    @BindView(R.id.tv_jj)
    NumberTextView tvjj;
    @BindView(R.id.tv_wb)
    NumberTextView tvWb;
    @BindView(R.id.tv_lb)
    NumberTextView tvLb;
    @BindView(R.id.tv_zf)
    NumberTextView tvZf;
    @BindView(R.id.tl_1)
    CommonTabLayout mTabLayout_1;
    @BindView(R.id.tl_2)
    CommonTabLayout mTabLayout_2;
    @BindView(R.id.suspension2)
    LinearLayout linear2;
    @BindView(R.id.scrollview)
    NoScrollScrollView scrollview;
    @BindView(R.id.refresh_root)
    SmartisanRefreshableScrollview refresh_root;
    @BindView(R.id.suspension1)
    LinearLayout linear1;
    @BindView(R.id.zxname)
    TextView addstock;
    @BindView(R.id.minuteID)
    TextView minuteid;
    @BindView(R.id.underline)
    View underline;
    @BindView(R.id.minuteview)
    FrameLayout minuteview;
    @BindView(R.id.chart_cont)
    FrameLayout chartCont;


    private FragmentTransaction ft;
    public FragmentManager fManager;
    private String[] mMinute = {"5分钟", "15分钟", "30分钟", "60分钟"};
    private BaseFragment chartFragment;

    private Stock stock;
    private String[] mTitles1 = {"分时", "五日", "日K", "周K", "月K"};
    private ArrayList<CustomTabEntity> mTabEntities1 = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    private String[] mTitles2 = {"F10", "新闻", "公告", "研报"};
    private ArrayList<CustomTabEntity> mTabEntities2 = new ArrayList<>();
    private int LayoutTop;
    private boolean ishide = true;
    private int mPos1 = 0;

    public static FsKxDetailsFragment newInstance(Stock stock) {
        FsKxDetailsFragment fragment = new FsKxDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, stock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = new HQPresenterImpl(oThis);
        if (getArguments() != null) {
            stock = getArguments().getParcelable(ARG_PARAM1);
        }
        fManager = this.getChildFragmentManager();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_fskx, container, false);
        ButterKnife.bind(this, view);
        oThis.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //加自选
                if (optionalUtils.queryCode(oThis, stock.getStockCodeAndMaket())) {
                    addstock.setText("- 自选");
                } else {
                    addstock.setText("+ 自选");
                }

                //显示隐藏头部行情数据
                if (ishide) {
                    hideimg.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.totop));
                } else {
                    hideimg.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.tobottom));
                }

                //分时K线导航条
                for (int i = 0; i < mTitles1.length; i++) {
                    mTabEntities1.add(new FlycoTabEntity(mTitles1[i], 0, 0));
                }

                mTabLayout_1.setTabData(mTabEntities1);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) minuteview.getLayoutParams();
                params.width = ScreenUtils.getScreenWidth(oThis) / 5;
                minuteview.setLayoutParams(params);
                Drawable drawable = ContextCompat.getDrawable(oThis, R.mipmap.btn_blue_triangle);
                drawable.setBounds(0, 0, DensityUtils.dp2px(oThis, 8), DensityUtils.dp2px(oThis, 18));
                minuteid.setCompoundDrawables(null, null, drawable, null);
                //默认显示分时

                switchFsKx(mPos1);
                mTabLayout_1.setCurrentTab(mPos1);

                mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onTabSelect(int position) {
                        switchFsKx(position);
                        mPos1 = position;
                    }

                    @Override
                    public void onTabReselect(int position) {
                        switchFsKx(position);
                        mPos1 = position;
                    }
                });

                //下拉刷新
                refresh_root.setOnRefreshListener(new SmartisanRefreshableScrollview.PullToRefreshListener() {
                    @Override
                    public void onRefresh() {
                        p.sendRequest(new HQDataMessage());
                    }

                    @Override
                    public void onRefreshFinished() {

                    }
                });

                //f10,新闻，公告，研报 导航条
                mFragments2.add(F10Fragment.newInstance());
                mFragments2.add(NewsFragment.newInstance("news"));
                mFragments2.add(NewsFragment.newInstance("notice"));
                mFragments2.add(NewsFragment.newInstance("report"));
                for (int i = 0; i < mTitles2.length; i++) {
                    mTabEntities2.add(new FlycoTabEntity(mTitles2[i], 0, 0));
                }

                mTabLayout_2.setTabData(mTabEntities2);
                //默认显示F10
                ft = fManager.beginTransaction();
                ft.replace(R.id.f10andnews, mFragments2.get(0));
                ft.commit();
                mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
                    @Override
                    public void onTabSelect(int position) {
                        ft = fManager.beginTransaction();
                        ft.replace(R.id.f10andnews, mFragments2.get(position));
                        ft.commit();
                    }

                    @Override
                    public void onTabReselect(int position) {

                    }
                });

                //f10导航条那一栏拉到顶部悬停
                scrollview.post(new Runnable() {
                    @Override
                    public void run() {
                        LayoutTop = view.findViewById(R.id.chart_cont).getBottom();
                    }
                });

                scrollview.setOnScrollListener(new NoScrollScrollView.OnScrollListener() {
                    @Override
                    public void onScroll(int scrollY) {
                        if (scrollY >= LayoutTop) {
                            if (mTabLayout_2.getParent() != linear1) {
                                linear2.removeView(mTabLayout_2);
                                linear1.addView(mTabLayout_2);
                            }
                        } else {
                            if (mTabLayout_2.getParent() != linear2) {
                                linear1.removeView(mTabLayout_2);
                                linear2.addView(mTabLayout_2);
                            }
                        }
                    }
                });

                //发送请求
                isPrepared = true;
                setlazyLoad();

            }
        });


        return view;
    }

    /*
    定时刷新
     */
    @Override
    public void autoRefresh() {

        if (p != null) {
            p.sendRequest(new HQDataMessage());
        }
        if (chartFragment != null) {
            chartFragment.autoRefresh();
        }
    }

    /**
     * 加载数据的方法，只要保证isPrepared和isVisible都为true的时候才往下执行开始加载数据
     */
    @Override
    protected void setlazyLoad() {
        super.setlazyLoad();
        if (!isPrepared || !isVisible) {
            return;
        }

        Logs.e("*************lazyload*****************");
        Logs.e("市场====" + stock.getMaketID());
        p.sendRequest(new HQDataMessage());

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.hidevalue, R.id.addzx, R.id.minuteview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hidevalue://隐藏头部数据
                if (ishide) {
                    hqvalueview.setVisibility(View.VISIBLE);
                    hideimg.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.tobottom));
                    ishide = false;
                } else {
                    hqvalueview.setVisibility(View.GONE);
                    hideimg.setImageDrawable(ContextCompat.getDrawable(oThis, R.mipmap.totop));
                    ishide = true;
                }
                break;
            case R.id.addzx://加自选
                MyStock mStock = new MyStock();
                mStock.setMaketID(stock.getMaketID());
                mStock.setStockName(stock.getStockName());
                mStock.setStockCode(stock.getStockCode());

                if (optionalUtils.queryCode(oThis, stock.getStockCodeAndMaket())) {
                    optionalUtils.deleteStock(oThis, stock.getStockCodeAndMaket());
                    addstock.setText("+ 自选");
                } else {
                    optionalUtils.addStock(oThis, mStock);
                    addstock.setText("- 自选");
                }
                break;
            case R.id.minuteview://分钟点击事件
                ActionSheetDialogNoTitle();

                break;
        }
    }


    /**
     * 数据回调
     */
    class HQDataMessage implements I_View_FSKX {

        @Override
        public String getStockCode() {
            return stock.getStockCodeex();
        }

        @Override
        public void onResponseStock(BeanHqMaket stock) {
            refresh_root.finishRefreshing();
            oThis.runOnUiThread(new UpdateThread(stock));
        }

        private class UpdateThread implements Runnable {

            BeanHqMaket stock;
            public UpdateThread(BeanHqMaket stock) {
                this.stock = stock;
            }

            public void run() {
                zjcjView.setText(stock.getNewPrice());
                zjcjView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
                zdeView.setText(stock.getZde());
                zdeView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
                zdfView.setText(stock.getZdf());
                zdfView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
                hslView.setText(stock.getHsl());
                cjslView.setText(stock.getCjl());
                cjjeView.setText(stock.getCje());
                jrkpView.setText(stock.getJrkp());
                jrkpView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_jrkp()));
                tvzrsp.setText(stock.getZrsp());
                zgView.setText(stock.getZg());
                zgView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_zg()));
                zdView.setText(stock.getZd());
                zdView.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_zd()));
                tvzt.setText(stock.getZt());
                tvzt.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_zt()));
                tvdt.setText(stock.getDt());
                tvdt.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_dt()));
                tvNp.setText(stock.getNp());
                tvWp.setText(stock.getWp());
                tvWb.setText(stock.getWb());
                tvLb.setText(stock.getLb());
                tvZf.setText(stock.getZhf());
                tvjj.setText(stock.getJunj());
                tvjj.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_junj()));
            }
        }

        @Override
        public void errorMessage(int errorId, String error) {
            refresh_root.finishRefreshing();
        }
    }

    /**
     * K线分钟弹出框
     */
    private void ActionSheetDialogNoTitle() {

        View view = View.inflate(oThis, R.layout.singletext_fskx, null);
        final ActionSheetDialog dialog = new ActionSheetDialog(oThis, mMinute, view);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFsKx(position + 5);
                dialog.dismiss();
            }
        });
    }

    /**
     * 分时K线选择事件
     */
    private void switchFsKx(int pos) {
        if (pos < 5) {
            mTabLayout_1.setIndicatorHeight(DensityUtils.dp2px(oThis, 0.8f));
            mTabLayout_1.setTextSelectColor(ContextCompat.getColor(oThis, R.color.blue_skin));
            minuteid.setText("分钟");
            minuteid.setTextColor(ContextCompat.getColor(oThis, R.color.gray));
            underline.setVisibility(View.GONE);
        } else {
            mTabLayout_1.setIndicatorHeight(0);
            mTabLayout_1.setTextSelectColor(ContextCompat.getColor(oThis, R.color.gray));
            minuteid.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
            underline.setVisibility(View.VISIBLE);
            minuteid.setText(mMinute[pos - 5]);
        }
        switch (pos) {
            case 0:
                FragmentTransaction ft1 = fManager.beginTransaction();
                chartFragment = MinutesFragment.newInstance(stock);
                ft1.replace(R.id.chart_cont, chartFragment);//分时
                ft1.commit();
                break;
            case 1:
                FragmentTransaction ft2 = fManager.beginTransaction();
                chartFragment = FiveDayMinutesFragment.newInstance(stock);
                ft2.replace(R.id.chart_cont, chartFragment);//五日
                ft2.commit();
                break;
            case 2:
                FragmentTransaction ft3 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 0, 0,0);
                ft3.replace(R.id.chart_cont, chartFragment);//日K
                ft3.commit();
                break;
            case 3:
                FragmentTransaction ft4 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 1, 0,0);
                ft4.replace(R.id.chart_cont, chartFragment);//周K
                ft4.commit();
                break;
            case 4:
                FragmentTransaction ft5 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 2, 0,0);
                ft5.replace(R.id.chart_cont, chartFragment);//月K
                ft5.commit();
                break;
            case 5:
                FragmentTransaction ft6 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 3, 0,0);
                ft6.replace(R.id.chart_cont, chartFragment);//5分钟
                ft6.commit();
                break;
            case 6:
                FragmentTransaction ft7 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 4, 0,0);
                ft7.replace(R.id.chart_cont, chartFragment);//15分钟
                ft7.commit();
                break;
            case 7:
                FragmentTransaction ft8 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 5, 0,0);
                ft8.replace(R.id.chart_cont, chartFragment);//30分钟
                ft8.commit();
                break;
            case 8:
                FragmentTransaction ft9 = fManager.beginTransaction();
                chartFragment = KLineFragment.newInstance(stock, 6, 0,0);
                ft9.replace(R.id.chart_cont, chartFragment);//60分钟
                ft9.commit();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);

            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }
    }

}
