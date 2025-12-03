package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalIntegerConverter implements Converter<Optional<Integer>, Integer> {

    @TypeConverter
    @Override
    public Integer convertForward(final Optional<Integer> value) {
        return value.orElse(null);
    }

    @TypeConverter
    @Override
    public Optional<Integer> convertBackward(final Integer value) {
        return Optional.ofNullable(value);
    }
}
