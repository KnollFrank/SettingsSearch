package de.KnollFrank.lib.settingssearch.common;

import java.util.Comparator;
import java.util.Optional;

public class Comparators {

    private static final int ARG1_LESS_THAN_ARG2 = -1;
    private static final int ARG1_EQUAL_TO_ARG2 = 0;
    private static final int ARG1_GREATER_THAN_ARG2 = +1;

    public static <T> Comparator<Optional<T>> emptiesLast(final Comparator<T> comparator) {
        return (optional1, optional2) -> {
            if (optional1.isEmpty() && optional2.isEmpty()) {
                return ARG1_EQUAL_TO_ARG2;
            } else if (optional1.isEmpty()) {
                return ARG1_GREATER_THAN_ARG2;
            } else if (optional2.isEmpty()) {
                return ARG1_LESS_THAN_ARG2;
            } else {
                return comparator.compare(optional1.get(), optional2.get());
            }
        };
    }
}
