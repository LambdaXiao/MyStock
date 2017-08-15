package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.BarData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.IBarDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmBarData extends BarData {

    public RealmBarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
