package com.android.mystock.ui.optionalpages.optionalEdit;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.mystock.R;
import com.android.mystock.common.views.RippleView;
import com.android.mystock.common.views.dslv.DragSortController;
import com.android.mystock.common.views.dslv.DragSortListView;
import com.android.mystock.data.database.DBHelper;
import com.android.mystock.data.database.DataBaseManager;
import com.android.mystock.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 自选股编辑Activity
 */
public class ActivityMyStockEdit extends BaseActivity {

    @BindView(R.id.complete)
    RippleView complete;
    @BindView(R.id.dragSortListView)
    DragSortListView mDslv;
    @BindView(R.id.selectAllParent)
    LinearLayout selectAllParent;
    @BindView(R.id.deleteText)
    TextView deleteText;
    @BindView(R.id.deleteTextParent)
    LinearLayout deleteTextParent;
    @BindView(R.id.selectAll)
    ImageView selectAll;

    private StockListAdapter adapter;
    private ArrayList<StockEdit> list;
    private DragSortController mController;
    public int dragStartMode = DragSortController.ON_DOWN;
    public boolean removeEnabled = true;
    public int removeMode = DragSortController.FLING_REMOVE;
    public boolean sortEnabled = true;

    private String deleteStrText;
    private int moveIndex = 0; // 自选股中 state=0与state =1,的分界索引
    private DataBaseManager db;
    private List<ContentValues> deletelist;

    /**
     * 子类通过重写此方法来加载布局文件
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_stock_edit);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        db = DataBaseManager.getInstance(oThis);

        mDslv.setSelected(false);
        mController = buildController(mDslv);
        mDslv.setDropListener(onDrop);
        mDslv.setRemoveListener(onRemove);
        mDslv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (list.get(position).selected) {
                    list.get(position).selected = false;
                } else {
                    list.get(position).selected = true;
                }

                oThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                setDeleteText(getSelectNumInList(list));
            }
        });

        if (deleteStrText == null)
            deleteStrText = oThis.getResources().getString(R.string.market_delete);

        selectAll.setSelected(false);
        setSelectViewBg(selectAll);
        setDeleteText(-1);
        initMyStock();
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {

                StockEdit item = adapter.getItem(from);
                StockEdit item2 = adapter.getItem(to);
                if (item.state == 1 || item2.state == 1) {
                    return;
                }
                adapter.remove(from);
                adapter.insert(item, to);

            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            StockEdit stock = adapter.getItem(which);
            if (stock.state == 0) // 判断是否已经列被删列表
            {
                list.get(which - 1).state = 1;
                stock.state = 1;
                adapter.remove(which);
                adapter.insert(stock, moveIndex);
                moveIndex--;

            } else {
                adapter.remove(which);
            }
        }


    };

    /**
     * Called in onCreateView. Override this to provide a custom
     * DragSortController.
     */
    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        //controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(removeEnabled);
        controller.setSortEnabled(sortEnabled);
        controller.setDragInitMode(dragStartMode);
        controller.setRemoveMode(removeMode);
        return controller;
    }

    public void initMyStock() {
        setListAdapter();
    }

    /**
     * 构建适配器
     */
    public void setListAdapter() {

        try {
            Cursor cursor = db.queryData2Cursor("select * from " + DBHelper.TABLENAME3
                            + " where selfGroup=? order by updateTime  desc",
                    new String[]{"0"});

            list = new ArrayList<StockEdit>();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                StockEdit stockEdit = new StockEdit();
                stockEdit.code = cursor.getString(cursor
                        .getColumnIndex("stockCode"));
                stockEdit.maket = cursor.getInt(cursor.getColumnIndex("maketID"));
                stockEdit.stockName = cursor.getString(cursor
                        .getColumnIndex("stockName"));
                list.add(stockEdit);

                cursor.moveToNext();
            }
            adapter = new StockListAdapter(oThis, R.layout.list_item_my_stock_edit, list);
            mDslv.setAdapter(adapter);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 自选股编辑适配器
     */
    class StockListAdapter extends BaseAdapter {

        private int resourceId;
        private LayoutInflater mInflater;
        private List<StockEdit> items;

        public StockListAdapter(Context context, int textViewResourceId, List<StockEdit> items) {

            this.mInflater = LayoutInflater.from(context);
            this.resourceId = textViewResourceId;
            this.items = items;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return items.size();
        }

        @Override
        public StockEdit getItem(int arg0) {
            // TODO Auto-generated method stub
            return items.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public void remove(int arg0) {//删除指定位置的item
            items.remove(arg0);
            oThis.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();//不要忘记更改适配器对象的数据源
                }
            });

        }

        public void insert(StockEdit item, int arg0) {
            items.add(arg0, item);
            oThis.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StockEdit stock = getItem(position);

            if (convertView == null) {
                convertView = mInflater.inflate(resourceId, null);
                final ViewHolder holder = new ViewHolder();
                holder.selectView = (ImageView) convertView.findViewById(R.id.viewTag);

                holder.stockCode = (TextView) convertView.findViewById(R.id.my_stock_list_stock_code);
                holder.stockName = (TextView) convertView.findViewById(R.id.my_stock_list_stock_name);
                holder.topButton = (View) convertView.findViewById(R.id.up_top_view);

                convertView.findViewById(R.id.viewTagParent).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       int position = (Integer) holder.selectView.getTag();
                        changeStockSingleSelect(list, position);

                        oThis.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        setDeleteText(getSelectNumInList(list));
                    }
                });
                holder.topButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        if (position > 0) {
                            StockEdit stock = adapter.getItem(position);
                            adapter.remove(position);
                            adapter.insert(stock, 0);
                        }
                        setDeleteText(getSelectNumInList(list));
                    }
                });

                convertView.setTag(holder);// 绑定ViewHolder对象
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对像

            holder.topButton.setTag(position);
            holder.selectView.setTag(position);

            if (stock.selected) {
                holder.selectView.setImageResource(R.mipmap.fuxuan_btn_choose);
            } else {
                holder.selectView.setImageResource(R.mipmap.fuxuan_btn);
            }

            holder.stockCode.setText(stock.code);
            holder.stockName.setText(stock.stockName);

            return convertView;
        }

    }

    @OnClick({R.id.selectAllParent, R.id.complete, R.id.deleteTextParent})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.selectAllParent:
                boolean isSelected = selectAll.isSelected();
                selectAll.setSelected(!isSelected);
                setSelectViewBg(selectAll);
                changeStockAllSelect(list, !isSelected);
                oThis.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                setDeleteText(getSelectNumInList(list));
                break;
            case R.id.complete:
                db.delectMyStockBatch(deletelist, "0");
                db.updateMyStockBatch(getUpadateList(list), "0");
                oThis.finish();
                break;

            case R.id.deleteTextParent:
                showDeleteDialog(oThis);
                break;
        }

    }


    private void showDeleteDialog(Context context) {
        new AlertDialog.Builder(context).setTitle("确定删除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        setDeleteList(list);
                        oThis.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deletStockInAdapter(list);
                                adapter.notifyDataSetChanged();
                                setDeleteText(getSelectNumInList(list));
                            }
                        });


                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        }).create().show();
    }

    private List<ContentValues> setDeleteList(List<StockEdit> list) {
        if (deletelist == null)
            deletelist = new ArrayList<ContentValues>();
        Iterator<StockEdit> it = list.iterator();
        while (it.hasNext()) {
            StockEdit stock = it.next();
            if (stock.selected) {
                ContentValues values = new ContentValues();
                values.put("stockCode", stock.code);
                values.put("market", stock.maket);
                deletelist.add(values);
            }
        }
        return deletelist;
    }

    /**
     * 删除
     *
     * @param list
     */
    private void deletStockInAdapter(List<StockEdit> list) {

        Iterator<StockEdit> it = list.iterator();
        for (it = list.iterator(); it.hasNext(); ) {
            StockEdit stock = it.next();
            if (stock.selected) {
                it.remove();
                list.remove(stock);
            }
        }

    }

    private void setDeleteText(int num) {


        if (num > 0) {
            deleteText.setSelected(true);
            deleteTextParent.setEnabled(true);
            deleteText.setText(String.format(deleteStrText, "（" + num + "）"));

            if (num == list.size()) {
                selectAll.setSelected(true);
                setSelectViewBg(selectAll);
            }

        } else {
            deleteText.setSelected(false);
            deleteTextParent.setEnabled(false);
            deleteText.setText(String.format(deleteStrText, ""));
            selectAll.setSelected(false);

        }
        if (list != null) {
            if (num < list.size()) {
                selectAll.setSelected(false);
            }
        }
        setSelectViewBg(selectAll);
    }

    private int getSelectNumInList(List<StockEdit> list) {
        int num = 0;
        Iterator<StockEdit> it = list.iterator();
        while (it.hasNext()) {
            StockEdit stock = it.next();
            if (stock.selected) {
                num++;
            }
        }
        return num;
    }

    /**
     * 修改 list中，所有的StockHq中的tag
     *
     * @param list
     */
    private void changeStockAllSelect(List<StockEdit> list, boolean isSelected) {
        for (int index = 0; index < list.size(); index++) {
            StockEdit stock = list.get(index);
            stock.selected = isSelected;
            list.set(index, stock);
        }
    }

    private void changeStockSingleSelect(List<StockEdit> list, int index) {
        if (index >= 0 && index < list.size()) {
            StockEdit stock = list.get(index);
            boolean selected = stock.selected;
            stock.selected = !selected;
            list.set(index, stock);
        }
    }

    private List<ContentValues> getUpadateList(List<StockEdit> list) {
        List<ContentValues> content = new ArrayList<ContentValues>();
        int i = 0;
        Iterator<StockEdit> it = list.iterator();
        while (it.hasNext()) {
            StockEdit stock = it.next();
            ContentValues values = new ContentValues();
            values.put("stockCode", stock.code);
            values.put("stockName", stock.stockName);
            values.put("maketID", stock.maket);
            values.put("state", stock.state);
            values.put("updateTime", System.currentTimeMillis() - i);
            i = i + 100;
            content.add(values);
        }
        return content;
    }

    public final class ViewHolder {
        public ImageView selectView;//当前是否被选中
        public TextView stockCode;// 股票代码
        public TextView stockName;// 股票名称
        public View topButton;//置顶
        public View dragButton;//拖动排序

    }

    /**
     * 股票编辑对象
     */
    protected class StockEdit {
        public String code;
        public String stockName;
        public int maket;
        public int state;
        public boolean selected = false;

        @Override
        public String toString() {
            return code + "." + maket;
        }
    }

    protected void setSelectViewBg(ImageView v) {
        if (v.isSelected()) {
            v.setImageResource(R.mipmap.fuxuan_btn_choose);
        } else {
            v.setImageResource(R.mipmap.fuxuan_btn);
        }
    }
}
