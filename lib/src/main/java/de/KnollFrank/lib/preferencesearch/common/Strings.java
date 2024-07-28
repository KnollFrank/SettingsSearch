package de.KnollFrank.lib.preferencesearch.common;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
                // FK-TODO: Suche nicht nur mit indexOf(), sondern ähnlich zur Suche in AndroidStudio, z.B. Strg-Alt-T, dann "mergedscreen" eingeben, es wird dann sogar MergedPreferenceScreen gefunden und die passenden Teile gehighlightet.
                return haystack.indexOf(needle, fromIndex);
            }
        };
    }
}
