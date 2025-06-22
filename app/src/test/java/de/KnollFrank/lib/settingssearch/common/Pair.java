package de.KnollFrank.lib.settingssearch.common;

// FK-TODO: remove this class and use android.util.Pair instead?
public record Pair<F, S>(F first, S second) {

    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<>(a, b);
    }
}
