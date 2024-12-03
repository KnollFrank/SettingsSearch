package de.KnollFrank.lib.settingssearch.common;

import java.util.Comparator;
import java.util.Optional;

public class Comparators {

    public static <T extends Comparable<T>> Comparator<Optional<T>> emptiesLast(final Comparator<T> comparator) {
        return (optional1, optional2) -> {
            if (optional1.isEmpty() && optional2.isEmpty()) {
                return 0;
            } else if (optional1.isEmpty()) {
                return +1;
            } else if (optional2.isEmpty()) {
                return -1;
            } else {
                return comparator.compare(optional1.get(), optional2.get());
            }
        };
    }
}
