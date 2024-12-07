package de.KnollFrank.lib.settingssearch.common.compare;

import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_EQUAL_TO_ARG2;
import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_GREATER_THAN_ARG2;
import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_LESS_THAN_ARG2;

import java.util.List;

import de.KnollFrank.lib.settingssearch.common.Lists;

class LexicographicalListComparator<T> implements ComparatorWithCompareResult<List<T>> {

    private final ComparatorWithCompareResult<T> elementComparator;

    public LexicographicalListComparator(final ComparatorWithCompareResult<T> elementComparator) {
        this.elementComparator = elementComparator;
    }

    @Override
    public CompareResult compare(final List<T> list1, final List<T> list2) {
        if (list1.size() < list2.size()) {
            return ARG1_LESS_THAN_ARG2;
        } else if (list1.size() > list2.size()) {
            return ARG1_GREATER_THAN_ARG2;
        } else {
            return Lists
                    .zip(list1, list2)
                    .stream()
                    .map(elementPair -> elementComparator.compare(elementPair.first, elementPair.second))
                    .filter(compareResult -> compareResult != ARG1_EQUAL_TO_ARG2)
                    .findFirst()
                    .orElse(ARG1_EQUAL_TO_ARG2);
        }
    }
}
