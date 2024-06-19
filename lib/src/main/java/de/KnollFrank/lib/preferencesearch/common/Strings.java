package de.KnollFrank.lib.preferencesearch.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import java.util.List;

public class Strings {

    public static List<Integer> getIndices(final String haystack, final String needle) {
        final Builder<Integer> indicesBuilder = ImmutableList.builder();
        int index = -1;
        while (true) {
            index = haystack.indexOf(needle, index + 1);
            if (index == -1) {
                break;
            }
            indicesBuilder.add(index);
        }
        return indicesBuilder.build();
    }
}
