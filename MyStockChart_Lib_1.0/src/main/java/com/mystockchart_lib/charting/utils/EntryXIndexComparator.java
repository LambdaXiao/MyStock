package com.mystockchart_lib.charting.utils;


import com.mystockchart_lib.charting.data.Entry;

import java.util.Comparator;

/**
 * Comparator for comparing Entry-objects by their x-index.
 *
 */
public class EntryXIndexComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry entry1, Entry entry2) {
        return entry1.getXIndex() - entry2.getXIndex();
    }
}
