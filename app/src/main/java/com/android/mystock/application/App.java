package com.android.mystock.application;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import com.android.mystock.common.log.Logs;
import com.android.mystock.data.Setting;
import com.android.mystock.net.enoservice.NetHttpWork;

/**
 * 应用程序
 */

public class App extends Application {

    public static Typeface digitaltypeface = null;
    @Override
    public void onCreate() {
        super.onCreate();

//        if(!BlockCanaryEx.isInSamplerProcess(this)) {
//            BlockCanaryEx.install(new Config(this));
//        }

        digitaltypeface = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "fonts/digital.ttf");

        //网络初始化
        NetHttpWork.init(this);
        //Log打印信息
        Logs.isDebug = true;

//        if(Logs.isDebug){
//            LeakCanary.install(this);
//        }

        initDayNightMode();//初始化夜间模式
    }

    private void initDayNightMode() {
        if (Setting.getNightMode(this)) {//获取保存的是哪个肤色
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
