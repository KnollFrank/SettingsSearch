package de.KnollFrank.lib.preferencesearch.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Strings {

    public static String joinNonNullElements(final String delimiter,
                                             final List<? extends CharSequence> elements) {
        return String.join(delimiter, filterNonNull(elements));
    }

    public static String join(final String delimiter, final Optional<CharSequence[]> elements) {
        return String.join(delimiter, asList(elements));
    }

    public static List<Integer> getIndicesOfNeedleWithinHaystack(final String haystack,
                                                                 final String needle) {
        return ImmutableList.copyOf(getIndicesOfNeedleWithinHaystackIterator(haystack, needle));
    }

    private static <T> List<T> asList(final Optional<T[]> elements) {
        final Builder<T> builder = ImmutableList.builder();
        elements.map(Arrays::asList).ifPresent(builder::addAll);
        return builder.build();
    }

    private static <T> List<T> filterNonNull(final List<T> ts) {
        return ts
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Iterator<Integer> getIndicesOfNeedleWithinHaystackIterator(final String haystack,
                                                                              final String needle) {
        if (needle.isEmpty()) {
            return Collections.emptyIterator();
        }
        return new Iterator<>() {

            private int nextIndex = getNextIndex(0);

            @Override
            public boolean hasNext() {
                return nextIndex != -1;
            }

            @Override
            public Integer next() {
                final int actualIndex = nextIndex;
                nextIndex = getNextIndex(nextIndex + 1);
                return actualIndex;
            }

            private int getNextIndex(final int fromIndex) {
                return haystack.indexOf(needle, fromIndex);
            }
        };
    }
}
