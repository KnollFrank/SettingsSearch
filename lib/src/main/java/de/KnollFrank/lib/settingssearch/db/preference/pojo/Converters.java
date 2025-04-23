package de.KnollFrank.lib.settingssearch.db.preference.pojo;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import java.util.Optional;

import javax.annotation.Nullable;

import de.KnollFrank.lib.settingssearch.common.Classes;

public class Converters {

    @TypeConverter
    public static Optional<String> string2Optional(final @Nullable String string) {
        return Optional.ofNullable(string);
    }

    @TypeConverter
    public static @Nullable String optional2String(final Optional<String> optional) {
        return optional.orElse(null);
    }

    @TypeConverter
    public static Class<? extends PreferenceFragmentCompat> string2Class(final String clazz) {
        return Classes.getClass(clazz);
    }

    @TypeConverter
    public static String class2String(final Class<? extends PreferenceFragmentCompat> clazz) {
        return clazz.getName();
    }
}
