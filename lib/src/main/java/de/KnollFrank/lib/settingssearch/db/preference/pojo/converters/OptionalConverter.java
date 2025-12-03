package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

class OptionalConverter<T> implements Converter<Optional<T>, String> {

    private final static String EMPTY_OPTIONAL = "__emptyOptional__";
    private final Converter<T, String> delegate;

    public OptionalConverter(final Converter<T, String> delegate) {
        this.delegate = delegate;
    }

    @TypeConverter
    @Override
    public String convertForward(final Optional<T> value) {
        return value
                .map(delegate::convertForward)
                .orElse(EMPTY_OPTIONAL);
    }

    @TypeConverter
    @Override
    public Optional<T> convertBackward(final String string) {
        return EMPTY_OPTIONAL.equals(string) ?
                Optional.empty() :
                Optional.of(delegate.convertBackward(string));
    }
}
