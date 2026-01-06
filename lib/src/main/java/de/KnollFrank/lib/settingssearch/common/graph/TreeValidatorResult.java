package de.KnollFrank.lib.settingssearch.common.graph;

import java.util.Optional;

class TreeValidatorResult {

    private final Optional<String> error;

    private TreeValidatorResult(Optional<String> error) {
        this.error = error;
    }

    public static TreeValidatorResult valid() {
        return new TreeValidatorResult(Optional.empty());
    }

    public static TreeValidatorResult invalid(final String errorMessage) {
        return new TreeValidatorResult(Optional.of(errorMessage));
    }

    public boolean isValid() {
        return error.isEmpty();
    }

    public void throwIfInvalid() {
        error.ifPresent(msg -> {
            throw new IllegalArgumentException(msg);
        });
    }
}
