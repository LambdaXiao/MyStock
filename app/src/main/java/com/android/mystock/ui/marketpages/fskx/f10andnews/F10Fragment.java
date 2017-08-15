package com.android.mystock.ui.marketpages.fskx.f10andnews;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseHqFragment;


/**
 * F10简况
 */
public class F10Fragment extends BaseHqFragment {


    public F10Fragment() {
        // Required empty public constructor
    }


    public static F10Fragment newInstance() {
        F10Fragment fragment = new F10Fragment();

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
        view= inflater.inflate(R.layout.fragment_f10, container, false);



        return view;
    }

}
