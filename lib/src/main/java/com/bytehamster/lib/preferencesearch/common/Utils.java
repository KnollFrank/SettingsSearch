package com.bytehamster.lib.preferencesearch.common;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    public static <T> List<T> concat(final List<List<T>> listOfLists) {
        return listOfLists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
