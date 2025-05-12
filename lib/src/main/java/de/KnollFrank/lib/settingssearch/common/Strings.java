package de.KnollFrank.lib.settingssearch.common;

import static de.KnollFrank.lib.settingssearch.common.IndexSearchResultConverter.minusOne2Empty;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class Strings {

    public static List<Integer> getIndicesOfNeedleWithinHaystack(final String haystack,
                                                                 final String needle) {
        return ImmutableList.copyOf(getIndicesOfNeedleWithinHaystackIterator(haystack, needle));
    }

    private static Iterator<Integer> getIndicesOfNeedleWithinHaystackIterator(final String haystack,
                                                                              final String needle) {
        if (needle.isEmpty()) {
            return Collections.emptyIterator();
        }
        return new Iterator<>() {

            private OptionalInt nextIndex = getNextIndex(0);

            @Override
            public boolean hasNext() {
                return nextIndex.isPresent();
            }

            @Override
            public Integer next() {
                final int actualIndex = nextIndex.orElseThrow();
                nextIndex = getNextIndex(actualIndex + 1);
                return actualIndex;
            }

            private OptionalInt getNextIndex(final int fromIndex) {
                // FK-TODO: Suche nicht nur mit indexOf(), sondern ähnlich zur Suche in AndroidStudio, z.B. Strg-Alt-T, dann "mergedscreen" eingeben, es wird dann sogar MergedPreferenceScreen gefunden und die passenden Teile gehighlightet.
                return indexOf(haystack, needle, fromIndex);
            }
        };
    }

    public static OptionalInt indexOf(final String haystack, final String needle, final int fromIndex) {
        return minusOne2Empty(haystack.indexOf(needle, fromIndex));
    }

    public static Optional<String> toString(final Optional<CharSequence> charSequence) {
        return charSequence.map(CharSequence::toString);
    }
}
