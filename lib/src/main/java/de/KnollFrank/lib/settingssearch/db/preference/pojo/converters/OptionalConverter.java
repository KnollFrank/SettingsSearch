package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import java.util.Optional;

class OptionalConverter<T> implements Converter<Optional<T>> {

    private final static String EMPTY_OPTIONAL = "__emptyOptional__";
    private final Converter<T> delegate;

    public OptionalConverter(final Converter<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString(final Optional<T> value) {
        return value
                .map(delegate::toString)
                .orElse(EMPTY_OPTIONAL);
    }

    @Override
    public Optional<T> fromString(final String string) {
        return EMPTY_OPTIONAL.equals(string) ?
                Optional.empty() :
                Optional.of(delegate.fromString(string));
    }
}
