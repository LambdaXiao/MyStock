package com.android.mystock.ui.base;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.android.mystock.R;
import com.android.mystock.net.enoservice.NetWorkInstance;
import com.android.mystock.ui.homepages.mvp.presenter.I_Presenter;


/**
 * 所以 fragment 的基类
 */

public class BaseFragment extends Fragment {
    public FragmentActivity oThis = null;
    public Resources res;
    public NetWorkInstance netWorkInstance;
    public View view;
    public I_Presenter p;//所有业务处理接口

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oThis = this.getActivity();
        res = getResources();
        netWorkInstance = NetWorkInstance.getInstance();

    }

    public void init(){
        View personal = view.findViewById(R.id.personal);
        if(personal != null){
            personal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawerLayout = (DrawerLayout) oThis.findViewById(R.id.drawer_layout);
                    if(drawerLayout != null){
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }else{
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    }
                }
            });
        }

        View backButton = view.findViewById(R.id.top_back);
        View searchButton = view.findViewById(R.id.top_search);

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
                    oThis.finish();
                }
            });
        }
    }
    /**
     * 子类重写此方法定时刷新
     */
    public void autoRefresh() {}


}
