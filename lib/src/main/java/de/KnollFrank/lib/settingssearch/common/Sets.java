package de.KnollFrank.lib.settingssearch.common;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Sets {

    public static <T> Set<T> union(final Collection<Set<T>> sets) {
        return sets
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
