package com.android.mystock.ui.tradepages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseFragment;


/**
 *信用交易导航菜单页
 */
public class CreditTradeFragment extends BaseFragment {

    public CreditTradeFragment() {
        // Required empty public constructor
    }


    public static CreditTradeFragment newInstance() {
        CreditTradeFragment fragment = new CreditTradeFragment();

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
        view = inflater.inflate(R.layout.fragment_credit_trade, container, false);

        return view;
    }


}
