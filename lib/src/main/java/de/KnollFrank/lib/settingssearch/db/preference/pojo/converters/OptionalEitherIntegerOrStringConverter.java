package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverter implements Converter<Optional<Either<Integer, String>>> {

    private static final OptionalConverter<Either<Integer, String>> converter = new OptionalConverter<>(new EitherIntegerOrStringConverter());

    @TypeConverter
    @Override
    public String toString(final Optional<Either<Integer, String>> value) {
        return converter.toString(value);
    }

    @TypeConverter
    @Override
    public Optional<Either<Integer, String>> fromString(final String string) {
        return converter.fromString(string);
    }
}
