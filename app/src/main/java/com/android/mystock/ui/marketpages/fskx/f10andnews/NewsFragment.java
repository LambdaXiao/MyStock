package com.android.mystock.ui.marketpages.fskx.f10andnews;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseHqFragment;


/**
 * 新闻公告研报
 */
public class NewsFragment extends BaseHqFragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


    public NewsFragment() {
        // Required empty public constructor
    }


    public static NewsFragment newInstance(String param1) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);


        return  view;
    }

}
