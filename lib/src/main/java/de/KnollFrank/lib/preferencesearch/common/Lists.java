package de.KnollFrank.lib.preferencesearch.common;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Collectors;

public class Lists {

    public static <T> List<T> concat(final List<List<T>> listOfLists) {
        return listOfLists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T> List<T> concat(final List<T> list1, final List<T> list2) {
        return concat(ImmutableList.of(list1, list2));
    }
}
