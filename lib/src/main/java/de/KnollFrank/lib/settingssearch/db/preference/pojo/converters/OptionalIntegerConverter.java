package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

public class OptionalIntegerConverter implements Converter<Optional<Integer>, Integer> {

    @TypeConverter
    @Override
    public Integer doForward(final Optional<Integer> value) {
        return value.orElse(null);
    }

    @TypeConverter
    @Override
    public Optional<Integer> doBackward(final Integer value) {
        return Optional.ofNullable(value);
    }
}
