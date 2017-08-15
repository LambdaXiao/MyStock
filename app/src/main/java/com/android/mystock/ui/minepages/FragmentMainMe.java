package com.android.mystock.ui.minepages;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.utils.AppUtils;
import com.android.mystock.common.utils.ColorUtil;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.scrollview.NoScrollScrollView;
import com.android.mystock.common.views.toast.TastyToast;
import com.android.mystock.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的页
 */
public class FragmentMainMe extends BaseFragment {

    @BindView(R.id.top_id)
    FrameLayout topId;//标题栏
    @BindView(R.id.scrolls)
    NoScrollScrollView scrolls;
    @BindView(R.id.login_logo)
    ImageView loginLogo;
    @BindView(R.id.wave_bg)
    FrameLayout waveBg;
    @BindView(R.id.settings)
    RippleView settings;
    @BindView(R.id.versionname)
    TextView versionname;

    public static FragmentMainMe newInstance() {
        FragmentMainMe fragment = new FragmentMainMe();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_me, container, false);
        init();//调用基类初始化头部标题栏
        ButterKnife.bind(this, view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            // 4.4以上处理
            oThis.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            ViewGroup.LayoutParams pars = topId.getLayoutParams();
            pars.height = DensityUtils.dp2px(oThis, 48) + ScreenUtils.getStatusHeight(oThis);
            topId.setLayoutParams(pars);
            if (topId != null) {
                topId.setPadding(0, ScreenUtils.getStatusHeight(oThis), 0, 0);// 设置上边距为状态栏高度getStatusBarHeight()
            }
        }

        versionname.setText("alpha " + AppUtils.getVersionName(oThis));

        scrolls.setOnScrollListener(new NoScrollScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                // 处理标题栏颜色渐变
                float space = Math.abs(scrollY) * 1f;
                float fraction = space / 400;
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


        return view;
    }


    @OnClick({R.id.login_logo, R.id.tolookmoney, R.id.settings, R.id.ri1, R.id.ri2, R.id.ri3, R.id.ri4, R.id.ri5, R.id.ri6
            , R.id.ri7, R.id.loginout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_logo:
            case R.id.tolookmoney:

                break;
            case R.id.settings:
                Intent i = new Intent(oThis, SettingActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.ri1:
                TastyToast.makeText(oThis, "Download Successful !", TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS);
                break;
            case R.id.ri2:
                break;
            case R.id.ri3:
                break;
            case R.id.ri4:
                break;
            case R.id.ri5:
                break;
            case R.id.ri6:
                break;
            case R.id.ri7:
                break;
            case R.id.loginout:
                break;
        }
    }

}
