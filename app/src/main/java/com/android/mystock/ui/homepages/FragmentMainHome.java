package com.android.mystock.ui.homepages;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.android.mystock.R;
import com.android.mystock.common.utils.ColorUtil;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.banner.DataProvider;
import com.android.mystock.common.views.banner.SimpleImageBanner;
import com.android.mystock.common.views.scrollview.NoScrollScrollView;
import com.android.mystock.ui.base.BaseFragment;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.ZoomOutSlideTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 */
public class FragmentMainHome extends BaseFragment {

    @BindView(R.id.sib_the_most_comlex_usage)
    SimpleImageBanner sibTheMostComlexUsage;
    @BindView(R.id.scrolls)
    NoScrollScrollView scrolls;
    @BindView(R.id.top_id)
    FrameLayout topId;
    @BindView(R.id.researchborder)
    RippleView researchborder;

    public static FragmentMainHome newInstance() {
        FragmentMainHome fragment = new FragmentMainHome();
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
        view = inflater.inflate(R.layout.fragment_main_home, container, false);
        init();//调用基类初始化头部标题栏

        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            // 4.4以上处理
            oThis.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            if (topId != null) {
                ViewGroup.LayoutParams pars = topId.getLayoutParams();
                pars.height = DensityUtils.dp2px(oThis,48)+ScreenUtils.getStatusHeight(oThis);
                topId.setLayoutParams(pars);
                topId.setPadding(0, ScreenUtils.getStatusHeight(oThis), 0, 0);// 设置上边距为状态栏高度getStatusBarHeight()
            }
        }

        scrolls.setOnScrollListener(new NoScrollScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                // 处理标题栏颜色渐变
                float space = Math.abs(scrollY) * 1f;
                float fraction = space/400;
                if (fraction < 0f) fraction = 0f;
                if (fraction > 1f) fraction = 1f;

                if (fraction >= 1f) {
//            viewTitleBg.setAlpha(0f);
//            viewActionMoreBg.setAlpha(0f);
                    topId.setBackgroundColor(oThis.getResources().getColor(R.color.topbarbackground));
                } else {
//            viewTitleBg.setAlpha(1f - fraction);
//            viewActionMoreBg.setAlpha(1f - fraction);
                    topId.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(oThis, fraction, R.color.transparent, R.color.topbarbackground));
                }
            }
        });

        sib_the_most_comlex_usage();

        return view;
    }

    private void sib_the_most_comlex_usage() {
        sibTheMostComlexUsage
                /** methods in BaseIndicatorBanner */
//              .setIndicatorStyle(BaseIndicaorBanner.STYLE_CORNER_RECTANGLE)//set indicator style
//              .setIndicatorWidth(6)                               //set indicator width
//              .setIndicatorHeight(6)                              //set indicator height
//              .setIndicatorGap(8)                                 //set gap btween two indicators
//              .setIndicatorCornerRadius(3)                        //set indicator corner raduis
                .setSelectAnimClass(ZoomInEnter.class)              //set indicator select anim
                /** methods in BaseBanner */
//              .setBarColor(Color.parseColor("#88000000"))         //set bootom bar color
//              .barPadding(5, 2, 5, 2)                             //set bottom bar padding
//              .setBarShowWhenLast(true)                           //set bottom bar show or not when the position is the last
//              .setTextColor(Color.parseColor("#ffffff"))          //set title text color
//              .setTextSize(12.5f)                                 //set title text size
//              .setTitleShow(true)                                 //set title show or not
//              .setIndicatorShow(true)                             //set indicator show or not
//              .setDelay(2)                                        //setDelay before start scroll
//              .setPeriod(10)                                      //scroll setPeriod
                .setSource(DataProvider.getList())                  //data source list
                .setTransformerClass(ZoomOutSlideTransformer.class) //set page transformer
                .startScroll();                                     //start scroll,the last method to call

    }
}
