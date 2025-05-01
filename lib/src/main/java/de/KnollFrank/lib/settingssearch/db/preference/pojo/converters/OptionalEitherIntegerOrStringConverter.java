package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

import java.util.Optional;

public class OptionalEitherIntegerOrStringConverter implements Converter<Optional<Either<Integer, String>>, String> {

    private static final OptionalConverter<Either<Integer, String>> converter = new OptionalConverter<>(new EitherIntegerOrStringConverter());

    @TypeConverter
    @Override
    public String doForward(final Optional<Either<Integer, String>> value) {
        return converter.doForward(value);
    }

    @TypeConverter
    @Override
    public Optional<Either<Integer, String>> doBackward(final String string) {
        return converter.doBackward(string);
    }
}
