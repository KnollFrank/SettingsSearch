package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

class OptionalConverter<T> implements Converter<Optional<T>, String> {

    private final Converter<T, String> delegate;

    public OptionalConverter(final Converter<T, String> delegate) {
        this.delegate = delegate;
    }

    @TypeConverter
    @Override
    public String convertForward(final Optional<T> value) {
        return value
                .map(delegate::convertForward)
                .orElse(null);
    }

    @TypeConverter
    @Override
    public Optional<T> convertBackward(final String string) {
        return string == null ?
                Optional.empty() :
                Optional.of(delegate.convertBackward(string));
    }
}
