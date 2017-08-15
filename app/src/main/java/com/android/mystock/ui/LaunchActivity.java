package com.android.mystock.ui;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.data.Setting;
import com.android.mystock.ui.base.BaseActivity;
import com.android.mystock.ui.homepages.mvp.presenter.I_Presenter;
import com.android.mystock.ui.homepages.mvp.view.I_View_Login;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动程序
 */
public class LaunchActivity extends BaseActivity {

    public I_Presenter p;//所有业务处理接口
    @BindView(R.id.logo_outer_iv)
    ImageView mLogoOuterIv;
    @BindView(R.id.logo_inner_iv)
    ImageView mLogoInnerIv;
    @BindView(R.id.app_name_tv)
    TextView mAppNameTv;

    boolean isShowingRubberEffect = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.zoomin, 0);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明

        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        if (Setting.getShowUserGuideSwitch(oThis)) {
            Intent intent = new Intent(oThis, GuideActivity.class);
            startActivity(intent);
            finish();
        } else {

            initAnimation();

//            p = new AppLoginPresenterImpl(this);
//            p.sendRequest(new LoginDataMessage());
        }
    }

    private void initAnimation() {
        startLogoInner1();
        startLogoOuterAndAppName();
    }

    private void startLogoInner1() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_top_in);
        mLogoInnerIv.startAnimation(animation);
    }

    private void startLogoOuterAndAppName() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                if (fraction >= 0.8 && !isShowingRubberEffect) {
                    isShowingRubberEffect = true;
                    startLogoOuter();
                    startShowAppName();
                    startActivity();
                } else if (fraction >= 0.95) {
                    valueAnimator.cancel();
                    startLogoInner2();
                }

            }
        });
        valueAnimator.start();
    }

    private void startLogoOuter() {
        YoYo.with(Techniques.RubberBand).duration(1000).playOn(mLogoOuterIv);
    }

    private void startShowAppName() {
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(mAppNameTv);
    }

    private void startLogoInner2() {
        YoYo.with(Techniques.Bounce).duration(1000).playOn(mLogoInnerIv);
    }

    /*
请求回调结果
 */
    class LoginDataMessage implements I_View_Login {

        @Override
        public void onResponse() {
            startActivity();
        }

        @Override
        public void errorMessage(int errorId, String error) {

        }
    }


    private void startActivity() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(oThis, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0, android.R.anim.fade_out);
                finish();
            }
        }, 1000);

    }
}
