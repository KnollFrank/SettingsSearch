package de.KnollFrank.lib.settingssearch.common.compare;

import static de.KnollFrank.lib.settingssearch.common.compare.Comparators.asComparator;
import static de.KnollFrank.lib.settingssearch.common.compare.Comparators.asComparatorWithCompareResult;
import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_EQUAL_TO_ARG2;
import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_GREATER_THAN_ARG2;
import static de.KnollFrank.lib.settingssearch.common.compare.CompareResult.ARG1_LESS_THAN_ARG2;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ComparatorFactory {

    public static <T> Comparator<List<T>> lexicographicalListComparator(final Comparator<T> elementComparator) {
        return asComparator(new LexicographicalListComparator<>(asComparatorWithCompareResult(elementComparator)));
    }

    public static <T> Comparator<Optional<T>> emptiesLast(final Comparator<T> comparator) {
        return asComparator(emptiesLast(asComparatorWithCompareResult(comparator)));
    }

    private static <T> ComparatorWithCompareResult<Optional<T>> emptiesLast(final ComparatorWithCompareResult<T> comparator) {
        return new ComparatorWithCompareResult<>() {

            private static final CompareResult ARG1_LAST = ARG1_GREATER_THAN_ARG2;
            private static final CompareResult ARG2_LAST = ARG1_LESS_THAN_ARG2;

            @Override
            public CompareResult compare(final Optional<T> optional1, final Optional<T> optional2) {
                if (optional1.isEmpty() && optional2.isEmpty()) {
                    return ARG1_EQUAL_TO_ARG2;
                } else if (optional1.isEmpty()) {
                    return ARG1_LAST;
                } else if (optional2.isEmpty()) {
                    return ARG2_LAST;
                } else {
                    return comparator.compare(optional1.orElseThrow(), optional2.orElseThrow());
                }
            }
        };
    }
}
