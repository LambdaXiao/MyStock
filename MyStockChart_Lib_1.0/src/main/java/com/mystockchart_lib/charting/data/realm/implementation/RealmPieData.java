package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.PieData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.IPieDataSet;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmPieData extends PieData {

    public RealmPieData(RealmResults<? extends RealmObject> result, String xValuesField, IPieDataSet dataSet) {
        super(RealmUtils.toXVals(result, xValuesField), dataSet);
    }
}
