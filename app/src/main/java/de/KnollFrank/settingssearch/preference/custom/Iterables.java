package de.KnollFrank.settingssearch.preference.custom;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;
import java.util.function.Predicate;

public class Iterables {

    public static <T extends @Nullable Object> OptionalInt indexOf(final Iterable<T> iterable,
                                                                   final Predicate<? super T> predicate) {
        final int index = com.google.common.collect.Iterables.indexOf(iterable, predicate::test);
        return index != -1 ? OptionalInt.of(index) : OptionalInt.empty();
    }
}
