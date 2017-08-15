package com.mystockchart_lib.charting.data.realm.implementation;


import com.mystockchart_lib.charting.data.BubbleData;
import com.mystockchart_lib.charting.data.realm.base.RealmUtils;
import com.mystockchart_lib.charting.interfaces.datasets.IBubbleDataSet;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;


public class RealmBubbleData extends BubbleData {

    public RealmBubbleData(RealmResults<? extends RealmObject> result, String xValuesField, List<IBubbleDataSet> dataSets) {
        super(RealmUtils.toXVals(result, xValuesField), dataSets);
    }
}
