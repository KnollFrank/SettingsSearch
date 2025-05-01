package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Optional;

// FK-TODO: make "implements Converter<Optional<Integer>, Integer>" after adding a second parameter to Converter interface
public class OptionalIntegerConverter {

    @TypeConverter
    public Integer toString(final Optional<Integer> value) {
        return value.orElse(null);
    }

    @TypeConverter
    public Optional<Integer> fromString(final Integer value) {
        return Optional.ofNullable(value);
    }
}
