package de.KnollFrank.lib.settingssearch.common;

import static de.KnollFrank.lib.settingssearch.common.Comparators.ComparatorResult.ARG1_EQUAL_TO_ARG2;
import static de.KnollFrank.lib.settingssearch.common.Comparators.ComparatorResult.ARG1_GREATER_THAN_ARG2;
import static de.KnollFrank.lib.settingssearch.common.Comparators.ComparatorResult.ARG1_LESS_THAN_ARG2;

import java.util.Comparator;
import java.util.Optional;


public class Comparators {

    public enum ComparatorResult {

        ARG1_LESS_THAN_ARG2(-1),
        ARG1_EQUAL_TO_ARG2(0),
        ARG1_GREATER_THAN_ARG2(+1);

        public final int value;

        ComparatorResult(final int value) {
            this.value = value;
        }
    }

    public static <T> Comparator<Optional<T>> emptiesLast(final Comparator<T> comparator) {
        return new Comparator<>() {

            private static final ComparatorResult ARG1_LAST = ARG1_GREATER_THAN_ARG2;
            private static final ComparatorResult ARG2_LAST = ARG1_LESS_THAN_ARG2;

            @Override
            public int compare(final Optional<T> optional1, final Optional<T> optional2) {
                if (optional1.isEmpty() && optional2.isEmpty()) {
                    return ARG1_EQUAL_TO_ARG2.value;
                } else if (optional1.isEmpty()) {
                    return ARG1_LAST.value;
                } else if (optional2.isEmpty()) {
                    return ARG2_LAST.value;
                } else {
                    return comparator.compare(optional1.get(), optional2.get());
                }
            }
        };
    }
}
