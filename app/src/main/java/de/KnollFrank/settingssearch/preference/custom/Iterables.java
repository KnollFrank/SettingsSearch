package de.KnollFrank.settingssearch.preference.custom;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;

public class Iterables {

    public static <T extends @Nullable Object> OptionalInt indexOf(final Iterable<T> items,
                                                                   final java.util.function.Predicate<? super T> predicate) {
        final int index = com.google.common.collect.Iterables.indexOf(items, predicate::test);
        return index != -1 ? OptionalInt.of(index) : OptionalInt.empty();
    }
}
