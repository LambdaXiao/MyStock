package com.android.mystock.ui.tradepages;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 普通交易导航菜单页
 */
public class OrdinaryTradeFragment extends BaseFragment {
    @BindView(R.id.tabs1)
    LinearLayout tabs1;
    @BindView(R.id.tabs2)
    LinearLayout tabs2;
    @BindView(R.id.tabs3)
    LinearLayout tabs3;
    /*
    id编号固定：100是买入，101是卖出，102是撤单，103是资金股份，104是银证转账，105是委托查询，106是成交查询，107是资金流水
                108是新股申购，109是场内基金，110是港股通，111是更多
     */
    private int[] viewId = {100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111};//view的id
    private String[] viewName = {"买入", "卖出", "撤单", "资金股份", "银证转账", "委托查询", "成交查询", "资金流水",
            "新股申购", "场内基金", "港股通", "更多"}; //view的名称
    private ImageView[] viewImg = {};//view的图标

    public OrdinaryTradeFragment() {
        // Required empty public constructor
    }

    public static OrdinaryTradeFragment newInstance() {
        OrdinaryTradeFragment fragment = new OrdinaryTradeFragment();

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
        view = inflater.inflate(R.layout.fragment_ordinary_trade, container, false);

        ButterKnife.bind(this, view);

        for (int i = 0; i < tabs1.getChildCount(); i++) {
            LinearLayout views1 = (LinearLayout) tabs1.getChildAt(i);
            views1.setId(viewId[i]);
            views1.setOnClickListener(allOnClickListener);
            ((TextView) views1.getChildAt(1)).setText(viewName[i]);
        }
        for (int i = 0; i < tabs2.getChildCount(); i++) {
            LinearLayout views1 = (LinearLayout) tabs2.getChildAt(i);
            views1.setId(viewId[i + 4]);
            views1.setOnClickListener(allOnClickListener);
            ((TextView) views1.getChildAt(1)).setText(viewName[i + 4]);
        }
        for (int i = 0; i < tabs3.getChildCount(); i++) {
            RelativeLayout views1 = (RelativeLayout) tabs3.getChildAt(i);
            views1.setId(viewId[i + 8]);
            views1.setOnClickListener(allOnClickListener);
            ((TextView) views1.getChildAt(0)).setText(viewName[i + 8]);
        }

        return view;
    }


    //单击事件
    View.OnClickListener allOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((int) v.getId() == 100) {//买入
//                Intent intent = new Intent(oThis, OrdinaryTradeActivity.class);
//                intent.putExtra("clickTab", 0);
//                startActivity(intent);

            } else if ((int) v.getId() == 101) {//卖出
//                Intent intent = new Intent(oThis, OrdinaryTradeActivity.class);
//                intent.putExtra("clickTab", 1);
//                startActivity(intent);

            } else if ((int) v.getId() == 102) {//撤单
//                Intent intent = new Intent(oThis, OrdinaryTradeActivity.class);
//                intent.putExtra("clickTab", 2);
//                startActivity(intent);

            } else if ((int) v.getId() == 103) {//资金股份
//                Intent intent = new Intent(oThis, OrdinaryTradeActivity.class);
//                intent.putExtra("clickTab", 3);
//                startActivity(intent);

            } else if ((int) v.getId() == 104) {

            } else if ((int) v.getId() == 105) {

            } else if ((int) v.getId() == 106) {

            } else if ((int) v.getId() == 107) {

            } else if ((int) v.getId() == 108) {

            } else if ((int) v.getId() == 109) {

            } else if ((int) v.getId() == 110) {

            } else if ((int) v.getId() == 111) {

            }
        }
    };


}
