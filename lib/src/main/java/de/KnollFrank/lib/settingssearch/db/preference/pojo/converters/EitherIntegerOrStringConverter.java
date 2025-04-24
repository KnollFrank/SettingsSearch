package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

public class EitherIntegerOrStringConverter implements Converter<Either<Integer, String>> {

    private static final String INTEGER_MARKER = "I";

    @TypeConverter
    @Override
    public String toString(final Either<Integer, String> value) {
        return value.join(
                integer -> INTEGER_MARKER + integer,
                string -> "S" + string);
    }

    @TypeConverter
    @Override
    public Either<Integer, String> fromString(final String string) {
        final String value = string.substring(INTEGER_MARKER.length());
        return string.startsWith(INTEGER_MARKER) ?
                Either.ofLeft(Integer.valueOf(value)) :
                Either.ofRight(value);
    }
}
