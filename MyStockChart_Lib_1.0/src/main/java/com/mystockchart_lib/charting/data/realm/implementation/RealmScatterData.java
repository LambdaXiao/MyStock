package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.ScatterData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.IScatterDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmScatterData extends ScatterData {

    public RealmScatterData(RealmResults<? extends RealmObject> result, String xValuesField, List<IScatterDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
