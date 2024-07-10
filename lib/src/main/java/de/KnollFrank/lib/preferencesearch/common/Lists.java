package de.KnollFrank.lib.preferencesearch.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lists {

    public static <T> List<T> concat(final List<List<T>> listOfLists) {
        return listOfLists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T> List<T> concat(final List<T>... lists) {
        return concat(Arrays.asList(lists));
    }

    public static <T> List<T> asList(final Optional<T[]> elements) {
        return elements
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);
    }

    public static <T> List<? extends T> getNonEmptyElements(final List<Optional<? extends T>> elements) {
        return elements
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
