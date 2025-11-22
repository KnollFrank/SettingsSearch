package de.KnollFrank.lib.settingssearch.common;

import android.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lists {

    public static <T> Optional<HeadAndTail<T>> asHeadAndTail(final List<T> ts) {
        return ts.isEmpty() ?
                Optional.empty() :
                Optional.of(
                        new HeadAndTail<>(
                                getHead(ts).orElseThrow(),
                                getTail(ts).orElseThrow()));
    }

    public static <T> List<T> concat(final List<List<T>> lists) {
        return lists
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static <T> Optional<T> getElementAtIndex(final List<T> ts, final int index) {
        return 0 <= index && index < ts.size() ?
                Optional.of(ts.get(index)) :
                Optional.empty();
    }

    public static <T> Optional<T> getLastElement(final List<T> ts) {
        return getElementAtIndex(ts, ts.size() - 1);
    }

    public static <T> Optional<T> getHead(final List<T> ts) {
        return getElementAtIndex(ts, 0);
    }

    public static <T> Optional<List<T>> getTail(final List<T> ts) {
        return ts.isEmpty() ?
                Optional.empty() :
                Optional.of(ts.subList(1, ts.size()));
    }

    public static <T> List<T> reverse(final List<T> ts) {
        return com.google.common.collect.Lists.reverse(ts);
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
