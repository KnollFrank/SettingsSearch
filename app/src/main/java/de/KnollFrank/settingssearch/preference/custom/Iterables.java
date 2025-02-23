package de.KnollFrank.settingssearch.preference.custom;

import static de.KnollFrank.lib.settingssearch.common.IndexSearchResultConverter.minusOne2Empty;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;
import java.util.function.Predicate;

public class Iterables {

    public static <T extends @Nullable Object> OptionalInt indexOf(final Iterable<T> iterable,
                                                                   final Predicate<? super T> predicate) {
        return minusOne2Empty(com.google.common.collect.Iterables.indexOf(iterable, predicate::test));
    }
}
