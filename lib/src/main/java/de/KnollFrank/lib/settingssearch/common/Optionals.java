package de.KnollFrank.lib.settingssearch.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Optionals {

    @SafeVarargs
    public static <T> Stream<T> streamOfPresentElements(final Optional<T>... elements) {
        return Arrays
                .stream(elements)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow);
    }

    public static <T> List<T> asList(final Optional<T[]> elements) {
        return elements
                .map(Arrays::asList)
                .orElseGet(Collections::emptyList);
    }
}
