package com.android.mystock.common.views;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.log.Logs;
import com.android.mystock.common.utils.DensityUtils;
import com.android.mystock.data.database.DBHelper;
import com.android.mystock.data.database.DataBaseManager;


/**
 * 用来专门输入股票代码的TextView
 */
public class AutoStockTextView extends AutoCompleteTextView {

    private MyCursorAdpter adapter;
    private Context context;
    private OnFocusChangeListener onFocusChangeListener;
    private int keyType = 0;
    private ListView listview;
    private DataBaseManager db;
    private String hintText = "代码/拼音/首字母";
    private Cursor cursor = null;


    public AutoStockTextView(Context context) {
        super(context);
        this.context = context;
        db = DataBaseManager.getInstance(context);
        init();
    }

    public AutoStockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        db = DataBaseManager.getInstance(context);
        init();
    }

    public AutoStockTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        db = DataBaseManager.getInstance(context);
        init();
    }

    public void init() {

        //  adpter=new MyCursorAdpter(context);

        // this.setAdapter(arrayAdapter);


        this.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                return false;
            }
        });

        this.addTextChangedListener(new TextWatcher() {//价格改变监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                Logs.e("输入的字符======" + s);
                searchStockDatas(s.toString());

            }
        });

//        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               Logs.e("输入的字符===2===" + position);
//            }
//        });


    }


    private class MyCursorAdpter extends CursorAdapter {

        public MyCursorAdpter(Context context, Cursor c) {
            super(context, c);

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.list_item_search_trade, parent, false);


            TextView stockCodeView = (TextView) view.findViewById(R.id.search_list_stock_code);
            TextView stockNameView = (TextView) view.findViewById(R.id.search_list_stock_name);
//            TextView addStockView = (TextView) view.findViewById(R.id.search_list_stock_add);
            TextView stockPYView = (TextView) view.findViewById(R.id.search_list_stock_py);

            String stockCode = cursor.getString(cursor.getColumnIndex("stockCode"));

            String stockName = cursor.getString(cursor.getColumnIndex("stockName"));

            stockCodeView.setText(stockCode);

            stockNameView.setText(stockName);


            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView stockCodeView = (TextView) view.findViewById(R.id.search_list_stock_code);
            TextView stockNameView = (TextView) view.findViewById(R.id.search_list_stock_name);

            String stockCode = cursor.getString(cursor.getColumnIndex("stockCode"));

            String stockName = cursor.getString(cursor.getColumnIndex("stockName"));

            stockCodeView.setText(stockCode);

            stockNameView.setText(stockName);


        }

        @Override
        public CharSequence convertToString(Cursor cursor) {

            String stockCode = cursor.getString(cursor.getColumnIndex("stockCode"));


            return stockCode;
        }


    }


    private void searchStockDatas(String condition) {
        condition = condition.toLowerCase();

        if (cursor != null) {
            cursor.close();
            cursor = null;
        }

        try {
            String queryUrl = "select * from " + DBHelper.TABLENAME2 + " where stockCode like '" + condition + "%' limit 0,30";
            if (keyType == 1) {
                queryUrl = "select * from " + DBHelper.TABLENAME2 + " where stockTag like '" + condition + "%' limit 0,30";
            }


            cursor = db.getDBHelper().ExecQuery(queryUrl);

            while (cursor.moveToNext()) {
                Logs.e(cursor.getString(cursor.getColumnIndex("stockCode")));

            }

            adapter = new MyCursorAdpter(context, cursor);
            setAdapter(adapter);

            setDropDownHeight(DensityUtils.dp2px(context, 200));
            setThreshold(1);
            //setCompletionHint("最近的5条记录");
            setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (onFocusChangeListener != null) {
                        onFocusChangeListener.onFocusChange(v, hasFocus);
                    }
                    AutoCompleteTextView view = (AutoCompleteTextView) v;
                    if (hasFocus) {
                        view.showDropDown();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setOnFocusChangeListener2(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
        onFocusChangeListener = l;
    }


}
