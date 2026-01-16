package de.KnollFrank.lib.settingssearch.common;

import com.google.common.collect.MoreCollectors;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Sets {

    public static <T> Set<T> union(final Collection<Set<T>> sets) {
        return sets
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> difference(final Set<T> set1, Set<T> set2) {
        return com.google.common.collect.Sets.difference(set1, set2);
    }

    public static <T> Set<T> intersection(final Set<T> set1, final Set<T> set2) {
        return com.google.common.collect.Sets.intersection(set1, set2);
    }

    public static <T> Optional<T> findElementByPredicate(final Set<T> elements, final Predicate<T> predicate) {
        return asOptional(filterElementsByPredicate(elements, predicate));
    }

    public static <T> Optional<T> asOptional(final Set<T> elements) {
        return elements
                .stream()
                .collect(MoreCollectors.toOptional());
    }

    public static <E> Set<E> filterElementsByPredicate(final Set<E> elements, final Predicate<E> predicate) {
        return elements
                .stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
}
