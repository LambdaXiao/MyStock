package com.android.mystock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.android.mystock.R;
import com.android.mystock.common.views.banner.DataProvider;
import com.android.mystock.common.views.banner.SimpleGuideBanner;
import com.android.mystock.data.Setting;
import com.android.mystock.ui.base.BaseActivity;
import com.flyco.banner.anim.select.ZoomInEnter;


/*
欢迎页面
 */
public class GuideActivity extends BaseActivity {

    private View decorView;
    private Class<? extends ViewPager.PageTransformer> transformerClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明

        transformerClass = DataProvider.transformers[1];
        decorView = getWindow().getDecorView();
        sgb();
    }

    private void sgb() {
        SimpleGuideBanner sgb = (SimpleGuideBanner) decorView.findViewById(R.id.sgb);

        sgb
                .setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(transformerClass)
                .barPadding(0, 10, 0, 10)
                .setSource(DataProvider.geUsertGuides())
                .startScroll();

        sgb.setOnJumpClickL(new SimpleGuideBanner.OnJumpClickL() {
            @Override
            public void onJumpClick() {
                Intent intent = new Intent(oThis, LaunchActivity.class);
                startActivity(intent);
                Setting.setShowUserGuideSwitch(oThis,false);
                finish();
            }
        });
    }

}
