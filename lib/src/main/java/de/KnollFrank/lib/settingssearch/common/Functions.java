package de.KnollFrank.lib.settingssearch.common;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Functions {

    // FK-TODO: add private constructor to each class having only static methods
    private Functions() {
    }

    public static <T, U> BiPredicate<U, T> swap(final BiPredicate<T, U> biPredicate) {
        return (u, t) -> biPredicate.test(t, u);
    }

    public static <T, U> Predicate<U> apply(final BiPredicate<T, U> biPredicate, final T t) {
        return u -> biPredicate.test(t, u);
    }
}
