package com.android.mystock.ui.optionalpages;


import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.adapters.CommonAdapter;
import com.android.mystock.common.adapters.ViewHolder;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.common.utils.ScreenUtils;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.smartisanpull.SmartisanRefreshableListView;
import com.android.mystock.data.consts.ENOStyle;
import com.android.mystock.data.database.DBHelper;
import com.android.mystock.data.database.DataBaseManager;
import com.android.mystock.ui.base.BaseHqFragment;
import com.android.mystock.ui.marketpages.bean.BeanHqMaket;
import com.android.mystock.ui.marketpages.bean.Stock;
import com.android.mystock.ui.marketpages.fskx.FsKxActivity;
import com.android.mystock.ui.marketpages.mvp.presenter.HQPresenterImpl;
import com.android.mystock.ui.marketpages.mvp.view.I_View_MyStockList;
import com.android.mystock.ui.marketpages.sort.BeanSort;
import com.android.mystock.ui.marketpages.sort.ComparatorHQList;
import com.android.mystock.ui.optionalpages.bean.MyStock;
import com.android.mystock.ui.optionalpages.optionalEdit.ActivityMyStockEdit;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自选股
 */
public class FragmentMainOptional extends BaseHqFragment {

    @BindView(R.id.edit_zxg)
    RippleView editZXG;//编辑
    @BindView(R.id.top_id)
    FrameLayout topId;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.mystocklistview)
    ListView listview;
    @BindView(R.id.refresh_root)
    SmartisanRefreshableListView refreshableListView;
    @BindView(R.id.add_stock_view)
    LinearLayout addStockView;
    @BindView(R.id.text_title2)
    TextView textTitle2;
    @BindView(R.id.text_title3)
    TextView textTitle3;

    private HQPresenterImpl p;
    private static String myStockStr;
    public Cursor cursor = null;
    public DataBaseManager db;
    public CommonAdapter<BeanHqMaket> madapter;
    private ArrayList<BeanHqMaket> mystocklist = new ArrayList<BeanHqMaket>();
    private ArrayList<BeanHqMaket> copylist = new ArrayList<BeanHqMaket>();//存放最开始顺序的数据列表
    public final static int FIELD_ZDF = 2;//涨跌幅
    public final static int FIELD_ZD = 3;//涨跌
    public final static int FIELD_ZSZ = 4;//总市值
    public final static String[] FIELD_NAME = {"涨跌幅", "涨跌额", "总市值"};
    private int currentField = FIELD_ZDF;//当前显示的字段
    private int currentOrder = ComparatorHQList.GONE;//当前排序
    private TextView[] button_titles;//第一表头
    private BeanSort currentBeanSort;//当前排序
    private TextView previousView = null;//上一个排序的字段
    private boolean isSort = false;//是否取消排序
    private boolean iszjcjSort = true;//是否按最新价排序

    public static FragmentMainOptional newInstance() {
        FragmentMainOptional fragment = new FragmentMainOptional();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DataBaseManager.getInstance(oThis);
        p = new HQPresenterImpl(oThis);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();//请求数据
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_optional, container, false);
        ButterKnife.bind(this, view);

        init();//调用基类初始化头部标题栏
        titleText.setText(R.string.market_optional);
        editZXG.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            // 4.4以上处理
            oThis.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            if (topId != null) {
                ViewGroup.LayoutParams pars = topId.getLayoutParams();
                pars.height = DensityUtils.dp2px(oThis, 48) + ScreenUtils.getStatusHeight(oThis);
                topId.setLayoutParams(pars);
                topId.setPadding(0, ScreenUtils.getStatusHeight(oThis), 0, 0);// 设置上边距为状态栏高度getStatusBarHeight()
            }
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(oThis, FsKxActivity.class);
                intent.putExtra("stockCode", mystocklist.get(position).getStockCode());
                intent.putExtra("stockName", mystocklist.get(position).getStockName());
                intent.putExtra("maketID", mystocklist.get(position).getMaketID());
                intent.putExtra("stockListIndex", position);
                intent.putParcelableArrayListExtra("stockList", mystocklist);
                startActivity(intent);
            }
        });

        if (editZXG != null) {
            editZXG.setVisibility(View.VISIBLE);
//            if (isSort) {
//                ((TextView) editZXG.getChildAt(0)).setText("取消排序");
//            } else {
//                ((TextView) editZXG.getChildAt(0)).setText("编辑");
//            }
            editZXG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSort) {//取消排序
                        if (previousView != null) {
                            Drawable drawable = ContextCompat.getDrawable(oThis, R.mipmap.btn_blue_triangle);
                            drawable.setBounds(0, 0, DensityUtils.dp2px(oThis, 8), DensityUtils.dp2px(oThis, 15));
                            previousView.setCompoundDrawables(null, null, drawable, null);
                            previousView.setCompoundDrawablePadding(DensityUtils.dp2px(oThis, 4));
                        }
                        currentBeanSort = null;
                        mystocklist.clear();
                        mystocklist.addAll(copylist);
                        if (madapter != null) {
                            madapter.notifyDataSetChanged();
                        }
//                        ((TextView) editZXG.getChildAt(0)).setText("编辑");
                        isSort = false;
                    }
                    //进入编辑页
                    Intent intent = new Intent(oThis, ActivityMyStockEdit.class);
                    startActivity(intent);

                }
            });
        }

        //排序字段
        button_titles = new TextView[2];
        button_titles[0] = textTitle2;//最新价排序字段
        button_titles[1] = textTitle3;//涨跌幅排序字段
        button_titles[0].setOnClickListener(mOnClickListener);
        button_titles[1].setOnClickListener(mOnClickListener);
        BeanSort beanSort1 = new BeanSort(1, ComparatorHQList.GONE);
        button_titles[0].setTag(beanSort1);
        if (currentField == FIELD_ZDF) {//涨跌幅
            BeanSort beanSort2 = new BeanSort(2, ComparatorHQList.GONE);
            button_titles[1].setTag(beanSort2);
        } else if (currentField == FIELD_ZD) {//涨跌额
            BeanSort beanSort2 = new BeanSort(3, ComparatorHQList.GONE);
            button_titles[1].setTag(beanSort2);
        } else if (currentField == FIELD_ZSZ) {//总市值
            BeanSort beanSort2 = new BeanSort(4, ComparatorHQList.GONE);
            button_titles[1].setTag(beanSort2);
        }
        Drawable drawable = ContextCompat.getDrawable(oThis, R.mipmap.btn_blue_triangle);
        drawable.setBounds(0, 0, DensityUtils.dp2px(oThis, 8), DensityUtils.dp2px(oThis, 15));
        button_titles[0].setCompoundDrawables(null, null, drawable, null);
        button_titles[0].setCompoundDrawablePadding(DensityUtils.dp2px(oThis, 4));
        button_titles[1].setCompoundDrawables(null, null, drawable, null);
        button_titles[1].setCompoundDrawablePadding(DensityUtils.dp2px(oThis, 4));

        //下拉刷新
        refreshableListView.setOnRefreshListener(new SmartisanRefreshableListView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                p.sendRequest(new HQDataMessage());//发送请求
            }

            @Override
            public void onRefreshFinished() {

            }
        });

        //转股票查询页面
        addStockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(oThis, ActivitySearchStock.class);
//                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void autoRefresh() {
        Logs.e("自选自动刷新！");
        if (p != null)
            p.sendRequest(new HQDataMessage());
    }

    /**
     * 获取数据库中的自选股
     */
    public void initData() {
        ArrayList<MyStock> list = new ArrayList<MyStock>();
//        Collections.synchronizedList(list);
        try {
            cursor = db.queryData2Cursor("select * from " + DBHelper.TABLENAME3
                            + " where selfGroup=? order by  updateTime  desc",
                    new String[]{"0"});

            cursor.moveToFirst();
            myStockStr = null;
            int insertIndex = 0; // stock.state=0时，stock插入list中的位置,
            while (!cursor.isAfterLast()) {
                MyStock stock = new MyStock();
                stock.setStockCode(cursor.getString(cursor
                        .getColumnIndex("stockCode")));
                stock.setStockName(cursor.getString(cursor
                        .getColumnIndex("stockName")));
                stock.setState(cursor.getInt(cursor.getColumnIndex("state")));
                stock.setMaketID(cursor.getInt(cursor.getColumnIndex("maketID")));
                stock.setClassId(cursor.getInt(cursor
                        .getColumnIndex("stockClass")));
                if (stock.getState() == 0) {
                    list.add(insertIndex, stock);
                    insertIndex++;
                    if (myStockStr == null) {
                        myStockStr = stock.getStockCodeAndMaket() + "|";
                    } else {
                        myStockStr += stock.getStockCodeAndMaket() + "|";
                    }
                } else {
                    list.add(stock);
                }
                cursor.moveToNext();
            }
            p.sendRequest(new HQDataMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_news, R.id.btn_report, R.id.btn_browser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_news:
                break;
            case R.id.btn_report:
                break;
            case R.id.btn_browser:
                break;
        }
    }

    /**
     * 数据请求回调函数
     */
    class HQDataMessage implements I_View_MyStockList {
        @Override
        public String getFiled() {
            return null;
        }

        @Override
        public String getStockCodes() {
            return myStockStr;
        }

        @Override
        public void onResponse(final ArrayList<BeanHqMaket> list) {
            refreshableListView.finishRefreshing();
            if (list.size() == 0) {
                addStockView.setVisibility(View.VISIBLE);
            } else {
                addStockView.setVisibility(View.GONE);
            }
            mystocklist = list;
            copylist.clear();
            copylist.addAll(list);

            madapter = new CommonAdapter<BeanHqMaket>(
                    oThis, mystocklist, R.layout.list_stock_data) {

                @Override
                public void convert(ViewHolder helper, BeanHqMaket temp) {
                    handleData(helper, temp);
                }
            };
            listview.setAdapter(madapter);
            if (currentBeanSort != null) {
                sortList();
            }
        }

        @Override
        public void errorMessage(int errorId, String error) {
            mystocklist.clear();
            copylist.clear();
            if (madapter != null) {
                madapter.notifyDataSetChanged();
            }
            addStockView.setVisibility(View.VISIBLE);
//            refreshableListView.finishRefreshing();
        }
    }

    //解析数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleData(ViewHolder helper, BeanHqMaket temp) {

        helper.setText(R.id.my_stock_list_stock_name, temp.getStockName());
        helper.setText(R.id.my_stock_list_stock_code, temp.getStockCode());
        TextView textview1 = helper.getView(R.id.my_stock_list_stock_price);
        TextView textview2 = helper.getView(R.id.my_stock_list_stock_Proportion);

        textview1.setText(temp.getNewPrice());
        textview2.setTextColor(ContextCompat.getColor(oThis, R.color.white));
        textview2.setGravity(Gravity.CENTER);
        //点击涨跌幅切换 涨跌幅，涨跌额，总市值
        textview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentField == FIELD_ZDF) {
                    currentField = FIELD_ZD;
                    button_titles[1].setText(FIELD_NAME[1]);
                } else if (currentField == FIELD_ZD) {
                    currentField = FIELD_ZSZ;
                    button_titles[1].setText(FIELD_NAME[2]);
                } else if (currentField == FIELD_ZSZ) {
                    currentField = FIELD_ZDF;
                    button_titles[1].setText(FIELD_NAME[0]);
                }
                if (currentBeanSort != null && !iszjcjSort && currentBeanSort.getOrder() != ComparatorHQList.GONE) {
                    currentBeanSort.setField(currentField);
                    sortList();
                } else {
                    madapter.notifyDataSetChanged();
                }
            }
        });
        if (temp.getState() < 0) {
            textview2.setBackground(ContextCompat.getDrawable(oThis, R.mipmap.green_rectangle));
            textview1.setTextColor(ENOStyle.hq_down);
        } else if (temp.getState() > 0) {
            textview2.setBackground(ContextCompat.getDrawable(oThis, R.mipmap.red_rectangle));
            textview1.setTextColor(ENOStyle.hq_up);
        } else {
            textview2.setBackground(ContextCompat.getDrawable(oThis, R.mipmap.gray_rectangle));
            textview1.setTextColor(ENOStyle.hq_flat);
        }

        if (temp.getIsDisable() == Stock.DISABLE) {
            textview2.setText("停牌");
        } else {
            if (currentField == FIELD_ZDF) {
                textview2.setText(temp.getZdf());
            } else if (currentField == FIELD_ZD) {
                textview2.setText(temp.getZde());
            } else if (currentField == FIELD_ZSZ) {
                textview2.setText(temp.getZsz());
            }
        }
    }

    /**
     * 标题按钮事件
     */
    View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (previousView != null) {
                Drawable drawable = ContextCompat.getDrawable(oThis, R.mipmap.btn_blue_triangle);
                drawable.setBounds(0, 0, DensityUtils.dp2px(oThis, 8), DensityUtils.dp2px(oThis, 15));
                previousView.setCompoundDrawables(null, null, drawable, null);
                previousView.setCompoundDrawablePadding(DensityUtils.dp2px(oThis, 4));
            }

            switch (v.getId()) {//最新价排序
                case R.id.text_title2:
                    iszjcjSort = true;
                    currentBeanSort = compoundDrawables(button_titles[0]);
                    previousView = button_titles[0];
                    break;
                case R.id.text_title3://涨跌幅，涨跌额，总市值排序
                    iszjcjSort = false;
                    currentBeanSort = compoundDrawables(button_titles[1]);
                    currentBeanSort.setField(currentField);
                    previousView = button_titles[1];
                    break;

            }
            sortList();//对list进行排序
        }
    };


    /**
     * 设置排序字段和图片
     */
    public BeanSort compoundDrawables(TextView v) {
        Drawable drawable = null;
        BeanSort beanSort = (BeanSort) v.getTag();
        int order = beanSort.getOrder();
        switch (order) {
            case ComparatorHQList.ASC:
                beanSort.setOrder(ComparatorHQList.DESC);
                drawable = ContextCompat.getDrawable(oThis, R.mipmap.down);
                isSort = true;
//                ((TextView) editZXG.getChildAt(0)).setText("取消排序");
                break;

            case ComparatorHQList.DESC:
                beanSort.setOrder(ComparatorHQList.GONE);
                drawable = ContextCompat.getDrawable(oThis, R.mipmap.btn_blue_triangle);
                isSort = false;
//                ((TextView) editZXG.getChildAt(0)).setText("编辑");
                mystocklist.clear();
                mystocklist.addAll(copylist);
                if (madapter != null) {
                    madapter.notifyDataSetChanged();
                }
                break;

            case ComparatorHQList.GONE:
                beanSort.setOrder(ComparatorHQList.ASC);
                drawable = ContextCompat.getDrawable(oThis, R.mipmap.up);
                isSort = true;
//                ((TextView) editZXG.getChildAt(0)).setText("取消排序");
                break;
        }

        if (drawable != null) {
            drawable.setBounds(0, 0, DensityUtils.dp2px(oThis, 8), DensityUtils.dp2px(oThis, 15));
        }
        v.setCompoundDrawables(null, null, drawable, null);
        v.setCompoundDrawablePadding(DensityUtils.dp2px(oThis, 4));
        return beanSort;
    }


    /**
     * 排序
     */
    public void sortList() {

        oThis.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (madapter != null) {
                    Collections.sort(mystocklist, new ComparatorHQList(currentBeanSort));
                    madapter.notifyDataSetChanged();
                }
            }
        });

    }
}
