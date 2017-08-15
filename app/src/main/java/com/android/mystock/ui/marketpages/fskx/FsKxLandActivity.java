package com.android.mystock.ui.marketpages.fskx;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.beans.FlycoTabEntity;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.utils.Utils;
import com.android.mystock.common.views.NumberTextView;
import com.android.mystock.ui.base.BaseActivity;
import com.android.mystock.ui.base.BaseFragment;
import com.android.mystock.ui.homepages.mvp.presenter.I_Presenter;
import com.android.mystock.ui.marketpages.HqUtils;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_FSKX;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.mystockchart_lib.charting.mychart.bean.DataParse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FsKxLandActivity extends BaseActivity implements MinutesLandFragment.OnFragmentInteractionListener
,FiveDayMinutesLandFragment.OnFragmentInteractionListener,KLineLandFragment.OnFragmentInteractionListener {

    @BindView(R.id.text_stockName)
    TextView textStockName;
    @BindView(R.id.text_stockCode)
    TextView textStockCode;
    @BindView(R.id.tl_1)
    CommonTabLayout mTabLayout_1;
    @BindView(R.id.minuteID)
    TextView minuteID;
    @BindView(R.id.underline)
    View underline;
    @BindView(R.id.minuteview)
    FrameLayout minuteview;
    @BindView(R.id.text_zjcj)
    NumberTextView textZjcj;
    @BindView(R.id.text_zde)
    NumberTextView textZde;
    @BindView(R.id.text_zdf)
    NumberTextView textZdf;
    @BindView(R.id.text_zg)
    NumberTextView textZg;
    @BindView(R.id.text_zd)
    NumberTextView textZd;
    @BindView(R.id.text_kp)
    NumberTextView textKp;
    @BindView(R.id.text_hsl)
    NumberTextView textHsl;
    @BindView(R.id.text_cjsl)
    NumberTextView textCjsl;
    @BindView(R.id.text_cjje)
    NumberTextView textCjje;
    @BindView(R.id.basevalue)
    LinearLayout basevalue;
    @BindView(R.id.fs_zjcj)
    NumberTextView fsZjcj;
    @BindView(R.id.fs_zde)
    NumberTextView fsZde;
    @BindView(R.id.fs_zdf)
    NumberTextView fsZdf;
    @BindView(R.id.fs_cjsl)
    NumberTextView fsCjsl;
    @BindView(R.id.fs_junprice)
    NumberTextView fsJunprice;
    @BindView(R.id.fs_time)
    NumberTextView fsTime;
    @BindView(R.id.minutevalue)
    LinearLayout minutevalue;
    @BindView(R.id.kline_zjcj)
    NumberTextView klineZjcj;
    @BindView(R.id.kline_zde)
    NumberTextView klineZde;
    @BindView(R.id.kline_zdf)
    NumberTextView klineZdf;
    @BindView(R.id.kline_zg)
    NumberTextView klineZg;
    @BindView(R.id.kline_zd)
    NumberTextView klineZd;
    @BindView(R.id.kline_kp)
    NumberTextView klineKp;
    @BindView(R.id.kline_sp)
    NumberTextView klineSp;
    @BindView(R.id.kline_cjsl)
    NumberTextView klineCjsl;
    @BindView(R.id.kline_cjje)
    NumberTextView klineCjje;
    @BindView(R.id.kline_date)
    NumberTextView klineDate;
    @BindView(R.id.klinevalue)
    LinearLayout klinevalue;

    private Stock stock;
    private String[] mMinute = {"5分钟", "15分钟", "30分钟", "60分钟"};
    private String[] mTitles1 = {"分时", "五日", "日K", "周K", "月K"};
    private ArrayList<CustomTabEntity> mTabEntities1 = new ArrayList<>();
    private BaseFragment chartFragment;
    private I_Presenter p;
    private int mKLinePeriod = 0;//默认为第一个
    private PopupWindow popupWindow;
    private int lotsize = 100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fs_kx_land);
        ButterKnife.bind(this);

        oThis.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 状态栏透明

        mKLinePeriod = this.getIntent().getIntExtra("tabIndex", 0);
        stock = this.getIntent().getParcelableExtra("stock");
        textStockName.setText(stock.getStockName());
        textStockCode.setText(stock.getStockCode());

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
        minuteID.setCompoundDrawables(null, null, drawable, null);
        if(mKLinePeriod < 5){
            mTabLayout_1.setCurrentTab(mKLinePeriod);
        }
        switchFsKx(mKLinePeriod);

        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelect(int position) {
                switchFsKx(position);
            }

            @Override
            public void onTabReselect(int position) {
                switchFsKx(position);
            }
        });

        p = new HQPresenterImpl(this);
        p.sendRequest(new HQDataMessage());
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick({R.id.back_button, R.id.minuteview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.minuteview:
                if (popupWindow == null) {
                    showPopupWindow();
                    popupWindow.showAsDropDown(minuteview, Gravity.RIGHT, 0, 0);
                } else if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(minuteview, Gravity.RIGHT, 0, 0);
                } else {
                    popupWindow.dismiss();
                }
                break;
        }
    }

    class HQDataMessage implements I_View_FSKX {

        @Override
        public String getStockCode() {
            return stock.getStockCodeAndMaket();
        }

        @Override
        public void onResponseStock(BeanHqMaket stock) {
            lotsize = stock.getLotsize();

            textZjcj.setText(stock.getNewPrice());
            textZjcj.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
            textZde.setText(stock.getZde());
            textZde.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
            textZdf.setText(stock.getZdf());
            textZdf.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_newPrice()));
            textZg.setText(stock.getZg());
            textZg.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_zg()));
            textZd.setText(stock.getZd());
            textZd.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_zd()));
            textKp.setText(stock.getJrkp());
            textKp.setTextColor(HqUtils.getHQColor2(stock.getValue_zrsp(),stock.getValue_jrkp()));
            textHsl.setText(stock.getHsl());
            textCjsl.setText(stock.getCjl());
            textCjje.setText(stock.getCje());
        }

        @Override
        public void errorMessage(int errorId, String error) {

        }
    }
    /**
     * 分时K线选择事件
     */
    private void switchFsKx(int pos) {
        if (pos < 5) {
            mTabLayout_1.setIndicatorHeight(DensityUtils.dp2px(oThis, 1f));
            mTabLayout_1.setTextSelectColor(ContextCompat.getColor(oThis, R.color.blue_skin));
            minuteID.setText("分钟");
            minuteID.setTextColor(ContextCompat.getColor(oThis, R.color.gray));
            underline.setVisibility(View.GONE);

        } else {
            mTabLayout_1.setIndicatorHeight(0);
            mTabLayout_1.setTextSelectColor(ContextCompat.getColor(oThis, R.color.gray));
            minuteID.setTextColor(ContextCompat.getColor(oThis, R.color.blue_skin));
            underline.setVisibility(View.VISIBLE);
            minuteID.setText(mMinute[pos - 5]);
        }
        switch (pos) {
            case 0:
                FragmentTransaction ft1 = fManager.beginTransaction();
                chartFragment = MinutesLandFragment.newInstance(stock);
                ft1.replace(R.id.chart_cont, chartFragment);//分时
                ft1.commit();
                break;
            case 1:
                FragmentTransaction ft2 = fManager.beginTransaction();
                chartFragment = FiveDayMinutesLandFragment.newInstance(stock);
                ft2.replace(R.id.chart_cont, chartFragment);//五日
                ft2.commit();
                break;
            case 2:
                FragmentTransaction ft3 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 0, 0,0);
                ft3.replace(R.id.chart_cont, chartFragment);//日K
                ft3.commit();
                break;
            case 3:
                FragmentTransaction ft4 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 1, 0,0);
                ft4.replace(R.id.chart_cont, chartFragment);//周K
                ft4.commit();
                break;
            case 4:
                FragmentTransaction ft5 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 2, 0,0);
                ft5.replace(R.id.chart_cont, chartFragment);//月K
                ft5.commit();
                break;
            case 5:
                FragmentTransaction ft6 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 3, 0,0);
                ft6.replace(R.id.chart_cont, chartFragment);//5分钟
                ft6.commit();
                break;
            case 6:
                FragmentTransaction ft7 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 4, 0,0);
                ft7.replace(R.id.chart_cont, chartFragment);//15分钟
                ft7.commit();
                break;
            case 7:
                FragmentTransaction ft8 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 5, 0,0);
                ft8.replace(R.id.chart_cont, chartFragment);//30分钟
                ft8.commit();
                break;
            case 8:
                FragmentTransaction ft9 = fManager.beginTransaction();
                chartFragment = KLineLandFragment.newInstance(stock, 6, 0,0);
                ft9.replace(R.id.chart_cont, chartFragment);//60分钟
                ft9.commit();
                break;
        }
    }

    /**
     * 弹出popupwindow窗口
     */
    private void showPopupWindow() {
        View view = LayoutInflater.from(oThis).inflate(R.layout.popup_minute, null);
        popupWindow = new PopupWindow(view, mTabLayout_1.getWidth() / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.contextMenuAnim);

        ListView listview = (ListView) view.findViewById(R.id.minutelistview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(oThis,
                R.layout.singletext_fskx, R.id.single_text, mMinute);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFsKx(position + 5);

                popupWindow.dismiss();
            }
        });

    }

    /**
     * 分时高亮线出现后的回调接口
     * @param Data
     */
    @Override
    public void onMinutesFragment(DataParse Data, int dataSetIndex, boolean isvisibility) {
        if(isvisibility){
            basevalue.setVisibility(View.GONE);
            minutevalue.setVisibility(View.VISIBLE);
            klinevalue.setVisibility(View.GONE);
            fsZjcj.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).cjprice,Data.getPresicion()));
            fsZde.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).cha,Data.getPresicion()));
            fsZdf.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).per*100)+"%");
            fsJunprice.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).avprice,Data.getPresicion()));
            fsCjsl.setText(Utils.formatLongNumber(Data.getMinutesDatas().get(dataSetIndex).cjnum)+"手");
            fsTime.setText(Data.getMinutesDatas().get(dataSetIndex).time);

            fsZjcj.setTextColor(HqUtils.getHQColor2(Data.getZrsp(),Data.getMinutesDatas().get(dataSetIndex).cjprice));
            fsZde.setTextColor(HqUtils.getHQColor2(Data.getZrsp(),Data.getMinutesDatas().get(dataSetIndex).cjprice));
            fsZdf.setTextColor(HqUtils.getHQColor2(Data.getZrsp(),Data.getMinutesDatas().get(dataSetIndex).cjprice));
            fsJunprice.setTextColor(HqUtils.getHQColor2(Data.getZrsp(),Data.getMinutesDatas().get(dataSetIndex).cjprice));
        }else{
            basevalue.setVisibility(View.VISIBLE);
            minutevalue.setVisibility(View.GONE);
            klinevalue.setVisibility(View.GONE);
        }
    }
    /**
     * 5日分时高亮线出现后的回调接口
     * @param Data
     */
    @Override
    public void onFiveMinutesFragment(DataParse Data, int dataSetIndex, boolean isvisibility) {
        if(isvisibility){
            basevalue.setVisibility(View.GONE);
            minutevalue.setVisibility(View.VISIBLE);
            klinevalue.setVisibility(View.GONE);
            fsZjcj.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).cjprice,Data.getPresicion()));
            fsZde.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).cha,Data.getPresicion()));
            fsZdf.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).per*100)+"%");
            fsJunprice.setText(Utils.getDecimals(Data.getMinutesDatas().get(dataSetIndex).avprice,Data.getPresicion()));
            fsCjsl.setText(Utils.formatLongNumber(Data.getMinutesDatas().get(dataSetIndex).cjnum/lotsize)+"手");
            fsTime.setText(Data.getMinutesDatas().get(dataSetIndex).time);

            fsZjcj.setTextColor(HqUtils.getHQColor2(0,Data.getMinutesDatas().get(dataSetIndex).cha));
            fsZde.setTextColor(HqUtils.getHQColor2(0,Data.getMinutesDatas().get(dataSetIndex).cha));
            fsZdf.setTextColor(HqUtils.getHQColor2(0,Data.getMinutesDatas().get(dataSetIndex).cha));
            fsJunprice.setTextColor(HqUtils.getHQColor2(0,Data.getMinutesDatas().get(dataSetIndex).cha));
        }else{
            basevalue.setVisibility(View.VISIBLE);
            minutevalue.setVisibility(View.GONE);
            klinevalue.setVisibility(View.GONE);
        }
    }

    /**
     * K线高亮线出现后的回调接口
     * @param Data
     */
    @Override
    public void onKLineFragment(DataParse Data,int dataSetIndex,boolean isvisibility) {
        if(isvisibility){
            basevalue.setVisibility(View.GONE);
            klinevalue.setVisibility(View.VISIBLE);
            minutevalue.setVisibility(View.GONE);
            klineZjcj.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).zaverj,Data.getPresicion()));
            klineZde.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).cha,Data.getPresicion()));
            klineZdf.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).per*100)+"%");
            klineKp.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).open,Data.getPresicion()));
            klineSp.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).close,Data.getPresicion()));
            klineZg.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).high,Data.getPresicion()));
            klineZd.setText(Utils.getDecimals(Data.getKLineDatas().get(dataSetIndex).low,Data.getPresicion()));
            klineCjsl.setText(Utils.formatLongNumber(Data.getKLineDatas().get(dataSetIndex).vol)+"手");
            klineCjje.setText(Utils.formatLongNumber(Data.getKLineDatas().get(dataSetIndex).cjmoney));
            klineDate.setText(Data.getKLineDatas().get(dataSetIndex).date+"");

            klineZjcj.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).zaverj));
            klineZde.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).zaverj));
            klineZdf.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).zaverj));
            klineKp.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).open));
            klineZg.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).high));
            klineZd.setTextColor(HqUtils.getHQColor2(Data.getKLineDatas().get(dataSetIndex).close,Data.getKLineDatas().get(dataSetIndex).low));
        }else{
            basevalue.setVisibility(View.VISIBLE);
            klinevalue.setVisibility(View.GONE);
            minutevalue.setVisibility(View.GONE);
        }
    }
}
