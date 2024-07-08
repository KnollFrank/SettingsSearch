package de.KnollFrank.lib.preferencesearch.common;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class Options {

    // adapted from java.util.Optional.or()
    public static <T> Optional<T> or(final Optional<T> optional, Supplier<? extends Optional<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (optional.isPresent()) {
            return optional;
        } else {
            @SuppressWarnings("unchecked")
            final Optional<T> r = (Optional<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }
}
