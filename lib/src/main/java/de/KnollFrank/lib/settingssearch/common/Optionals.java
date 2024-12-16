package de.KnollFrank.lib.settingssearch.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Optionals {

    // adapted from Optional.stream()
    public static <T> Stream<T> stream(final Optional<T> optional) {
        if (optional.isEmpty()) {
            return Stream.empty();
        } else {
            return Stream.of(optional.orElseThrow());
        }
    }

    @SafeVarargs
    public static <T> List<T> getPresentElements(final Optional<T>... elements) {
        return streamOfPresentElements(elements).collect(Collectors.toList());
    }

    @SafeVarargs
    public static <T> Stream<T> streamOfPresentElements(final Optional<T>... elements) {
        return Arrays
                .stream(elements)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public static <T> List<T> asList(final Optional<T[]> elements) {
        return elements
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);
    }
}
