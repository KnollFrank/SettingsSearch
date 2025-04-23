package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.room.TypeConverter;

import java.util.Optional;

import javax.annotation.Nullable;

public class Converters {

    @TypeConverter
    public static Optional<String> string2Optional(final @Nullable String string) {
        return Optional.ofNullable(string);
    }

    @TypeConverter
    public static @Nullable String optional2String(final Optional<String> optional) {
        return optional.orElse(null);
    }
}
