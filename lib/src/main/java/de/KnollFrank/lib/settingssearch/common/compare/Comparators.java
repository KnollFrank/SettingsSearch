package de.KnollFrank.lib.settingssearch.common.compare;

import java.util.Comparator;

class Comparators {

    public static <T> Comparator<T> asComparator(final ComparatorWithCompareResult<T> comparatorWithCompareResult) {
        return (o1, o2) -> comparatorWithCompareResult.compare(o1, o2).value;
    }

    public static <T> ComparatorWithCompareResult<T> asComparatorWithCompareResult(final Comparator<T> comparator) {
        return (o1, o2) -> CompareResult.of(comparator.compare(o1, o2));
    }
}
