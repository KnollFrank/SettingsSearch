package de.KnollFrank.lib.settingssearch.common;

import java.util.Optional;
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
}
