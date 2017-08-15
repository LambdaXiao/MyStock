package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.LineData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.ILineDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmLineData extends LineData {

    public RealmLineData(RealmResults<? extends RealmObject> result, String xValuesField, List<ILineDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
