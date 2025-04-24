package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.common.Classes;

// FK-TODO: unit test
public class ClassConverter implements Converter<Class<? extends PreferenceFragmentCompat>> {

    @TypeConverter
    @Override
    public String toString(final Class<? extends PreferenceFragmentCompat> value) {
        return value.getName();
    }

    @TypeConverter
    @Override
    public Class<? extends PreferenceFragmentCompat> fromString(final String string) {
        return Classes.getClass(string);
    }
}
