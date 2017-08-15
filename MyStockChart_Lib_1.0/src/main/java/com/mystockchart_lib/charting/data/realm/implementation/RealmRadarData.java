package com.mystockchart_lib.charting.data.realm.implementation;

import com.mystockchart_lib.charting.data.RadarData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.IRadarDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmRadarData extends RadarData {

    public RealmRadarData(RealmResults<? extends RealmObject> result, String xValuesField, List<IRadarDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
