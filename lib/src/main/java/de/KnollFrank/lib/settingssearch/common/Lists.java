package de.KnollFrank.lib.settingssearch.common;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lists {

    public static <T> List<T> concat(final List<List<T>> lists) {
        return lists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T> Optional<T> getLastElement(final List<T> ts) {
        if (ts.isEmpty()) {
            return Optional.empty();
        }
        final T lastElement = ts.get(ts.size() - 1);
        return Optional.of(lastElement);
    }

    public static <T> T head(final List<T> ts) {
        return ts.get(0);
    }

    public static <T> List<T> tail(final List<T> ts) {
        return ts.subList(1, ts.size());
    }

    public static <T> List<T> reverse(final List<T> ts) {
        final List<T> tsReversed = new ArrayList<>(ts);
        Collections.reverse(tsReversed);
        return tsReversed;
    }

    // adapted from https://stackoverflow.com/questions/31963297/how-to-zip-two-java-lists
    public static <A, B> List<Pair<A, B>> zip(final List<A> as, final List<B> bs) {
        if (as.size() != bs.size()) {
            throw new IllegalArgumentException();
        }
        return IntStream
                .range(0, as.size())
                .mapToObj(i -> Pair.create(as.get(i), bs.get(i)))
                .collect(Collectors.toList());
    }
}
