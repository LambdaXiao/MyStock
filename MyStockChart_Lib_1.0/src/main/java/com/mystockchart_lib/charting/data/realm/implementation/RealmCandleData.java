package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.CandleData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.ICandleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmCandleData extends CandleData {

    public RealmCandleData(RealmResults<? extends RealmObject> result, String xValuesField, List<ICandleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
