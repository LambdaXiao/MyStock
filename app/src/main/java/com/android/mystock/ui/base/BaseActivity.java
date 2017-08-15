package com.android.mystock.ui.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.android.mystock.R;
import com.android.mystock.common.log.Logs;
import com.android.mystock.data.Setting;
import com.android.mystock.net.enoservice.NetWorkInstance;


/**
 * Created by xmf on 16/3/17.
 * 所以activity的基类
 */
public class BaseActivity extends AppCompatActivity {
    public Activity oThis;//全局上下文对象
    public FragmentManager fManager;
    public FragmentTransaction ft;
    public Resources res;
    public NetWorkInstance netWorkInstance;

    public static int currentPageId = 0;//主页导航菜单的编号，第几个tab
    public View backButton;//返回按钮
    public View searchButton;//查询按钮


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        oThis = this;
        fManager = getSupportFragmentManager();
        res = getResources();
        netWorkInstance = NetWorkInstance.getInstance();

        setNightOrDayMode();//初始化夜间模式


        backButton = (View) findViewById(R.id.top_back);
        searchButton = (View) findViewById(R.id.top_search);

        if (searchButton != null) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(oThis, ActivitySearchStock.class);
//                    startActivity(intent);
//                    ToastUtils.showShort(oThis, "点击了查询股票功能");

                }
            });
        }


        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    /**
     * 子类通过重写此方法来加载布局文件
     */
    public void setContentView() {

    }

    private void setNightOrDayMode() {
        if (Setting.getNightMode(this)) {//获取保存的是哪个肤色
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    Handler autoRefreshHandler;
    Runnable runnable;
    // 开始定时刷新
    public void startAutoRefresh() {
        // getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
        final long autoRequestTime = Setting.getAutoRefreshTime(oThis);//默认5秒

        if (autoRequestTime > 0) {
            if (autoRefreshHandler == null) {
                autoRefreshHandler = new Handler();
            }
            runnable = new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub
                    // 要做的事情
                    autoRefreshHandler.postDelayed(runnable, autoRequestTime);

//                     if (HqUtils.isMarketOpen()) {
//                         autoRefresh();//暂时关闭定时刷新
//                     }

                }
            };
            // 2.啟動計時器：
            autoRefreshHandler.postDelayed(runnable, autoRequestTime);// 每兩秒執行一次runnable.
            // 3.停止計時器：
            // handler.removeCallbacks(runnable);
        } else {
            // android.util.Log.d("auto", "isAutoRequest is false");
            if (runnable != null) {
                autoRefreshHandler.removeCallbacks(runnable);
            }
        }
    }

    /**
     * 停止自动刷新
     */
    public void stopAutoRefresh() {
        Logs.e("刷新关闭了");
        if (runnable != null) {
            if (autoRefreshHandler != null) {
                autoRefreshHandler.removeCallbacks(runnable);
            }
        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoRefresh();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopAutoRefresh();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}