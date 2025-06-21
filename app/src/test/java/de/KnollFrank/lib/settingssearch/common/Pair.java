package de.KnollFrank.lib.settingssearch.common;

public record Pair<F, S>(F first, S second) {

    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<>(a, b);
    }
}
