package com.mystockchart_lib.charting.data.realm.base;

import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealmObject;
import io.realm.RealmObject;
import io.realm.RealmResults;


public final class RealmUtils {

    /**
     * Prevent class instantiation.
     */
    private RealmUtils() {
    }

    /**
     * Transforms the given Realm-ResultSet into a String array by using the provided xValuesField.
     *
     * @param result
     * @param xValuesField
     * @return
     */
    public static List<String> toXVals(RealmResults<? extends RealmObject> result, String xValuesField) {

        List<String> xVals = new ArrayList<>();

        for (RealmObject object : result) {

            DynamicRealmObject dynamicObject = new DynamicRealmObject(object);
            xVals.add(dynamicObject.getString(xValuesField));
        }

        return xVals;
    }
}
