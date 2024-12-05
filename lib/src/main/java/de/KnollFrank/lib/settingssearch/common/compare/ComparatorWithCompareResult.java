package de.KnollFrank.lib.settingssearch.common.compare;

@FunctionalInterface
interface ComparatorWithCompareResult<T> {

    CompareResult compare(T o1, T o2);
}
