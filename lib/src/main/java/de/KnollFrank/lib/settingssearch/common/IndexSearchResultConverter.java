package de.KnollFrank.lib.settingssearch.common;

import java.util.OptionalInt;

public class IndexSearchResultConverter {

    private IndexSearchResultConverter() {
    }

    public static OptionalInt minusOneToEmpty(final int index) {
        return index == -1 ? OptionalInt.empty() : OptionalInt.of(index);
    }
}
