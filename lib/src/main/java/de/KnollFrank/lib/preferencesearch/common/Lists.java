package de.KnollFrank.lib.preferencesearch.common;

import java.util.Arrays;
import java.util.List;
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
}
