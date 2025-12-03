package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import com.codepoetics.ambivalence.Either;

public class EitherIntegerOrStringConverter implements Converter<Either<Integer, String>, String> {

    private static final String INTEGER_MARKER = "I";

    @TypeConverter
    @Override
    public String convertForward(final Either<Integer, String> value) {
        return value.join(
                integer -> INTEGER_MARKER + integer,
                string -> "S" + string);
    }

    @TypeConverter
    @Override
    public Either<Integer, String> convertBackward(final String string) {
        final String value = string.substring(INTEGER_MARKER.length());
        return string.startsWith(INTEGER_MARKER) ?
                Either.ofLeft(Integer.valueOf(value)) :
                Either.ofRight(value);
    }
}
