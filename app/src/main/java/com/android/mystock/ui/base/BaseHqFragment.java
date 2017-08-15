package com.android.mystock.ui.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.mystock.common.log.Logs;
import com.android.mystock.ui.homepages.mvp.presenter.I_Presenter;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;

import java.lang.reflect.Field;

/**
 * 行情 fragment 的基类
 */

public class BaseHqFragment extends BaseFragment {

    public boolean isPrepared;
    /**
     * 当前界面是否呈现给用户的状态标志
     */
    public boolean isVisible;
    public I_Presenter p;//所有业务处理接口
//    protected CursorTouchEventDate cursorTouchEventDate;
//
//    public void setCursorTouchEventDate(CursorTouchEventDate cursorTouchEventDate) {
//        this.cursorTouchEventDate = cursorTouchEventDate;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        p = new HQPresenterImpl(oThis);//示例化行情业务处理对象
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        Logs.e("我回来了————————————————————————" + hidden);
        if (!hidden) {
            oThis = this.getActivity();
            p = new HQPresenterImpl(oThis);//示例化行情业务处理对象

        }


    }



    /**
     * 重写Fragment父类生命周期方法，在onCreate之前调用该方法，实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser 当前是否已将界面显示给用户的状态
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 当界面呈现给用户，即设置可见时执行，进行加载数据的方法
     * 在用户可见时加载数据，而不在用户不可见的时候加载数据，是为了防止控件对象出现空指针异常
     */
    protected void onVisible() {
        setlazyLoad();
    }

    /**
     * 当界面还没呈现给用户，即设置不可见时执行
     */
    protected void onInvisible() {
    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */

    protected void setlazyLoad() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isPrepared = false;
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





