package de.KnollFrank.lib.settingssearch.common;

import java.util.OptionalInt;

public class IndexSearchResultConverter {

    public static OptionalInt minusOne2Empty(final int index) {
        return index == -1 ? OptionalInt.empty() : OptionalInt.of(index);
    }
}
