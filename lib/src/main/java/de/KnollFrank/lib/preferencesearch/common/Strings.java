package de.KnollFrank.lib.preferencesearch.common;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Strings {

    public static String joinNonNullCharSequences(final String delimiter, final CharSequence... charSequences) {
        return String.join(delimiter, filterNonNull(charSequences));
    }

    public static String join(final CharSequence[] charSequences) {
        return charSequences == null ?
                "" :
                String.join(", ", charSequences);
    }

    public static List<Integer> getIndicesOfNeedleWithinHaystack(final String haystack,
                                                                 final String needle) {
        return ImmutableList.copyOf(getIndicesOfNeedleWithinHaystackIterator(haystack, needle));
    }

    private static CharSequence[] filterNonNull(final CharSequence[] charSequences) {
        return Arrays
                .stream(charSequences)
                .filter(Objects::nonNull)
                .toArray(CharSequence[]::new);
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
