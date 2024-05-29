package com.bytehamster.lib.preferencesearch.common;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sets {

    public static <T> Set<T> union(final Collection<Set<T>> sets) {
        return union(sets.stream());
    }

    private static <T> Set<T> union(final Stream<Set<T>> streamOfSets) {
        return streamOfSets.flatMap(Set::stream).collect(Collectors.toSet());
    }
}
