package de.KnollFrank.lib.settingssearch.search;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import de.KnollFrank.lib.settingssearch.common.Strings;

public class CaseInsensitiveSubstringMatcher implements StringMatcher {

    @Override
    public Set<IndexRange> getMatches(final String haystack, final String needle) {
        return this
                .getIndicesOfNeedleWithinHaystack(haystack.toLowerCase(), needle.toLowerCase())
                .stream()
                .map(index -> new IndexRange(index, index + needle.length()))
                .collect(Collectors.toSet());
    }

    private List<Integer> getIndicesOfNeedleWithinHaystack(final String haystack,
                                                           final String needle) {
        return ImmutableList.copyOf(getIndicesOfNeedleWithinHaystackIterator(haystack, needle));
    }

    private Iterator<Integer> getIndicesOfNeedleWithinHaystackIterator(final String haystack,
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
                return Strings.indexOf(haystack, needle, fromIndex);
            }
        };
    }
}
