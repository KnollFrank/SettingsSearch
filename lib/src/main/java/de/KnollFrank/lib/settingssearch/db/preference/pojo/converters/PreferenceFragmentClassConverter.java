package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.common.Classes;

public class PreferenceFragmentClassConverter implements Converter<Class<? extends PreferenceFragmentCompat>> {

    @TypeConverter
    @Override
    public String toString(final Class<? extends PreferenceFragmentCompat> clazz) {
        return clazz.getName();
    }

    @TypeConverter
    @Override
    public Class<? extends PreferenceFragmentCompat> fromString(final String string) {
        return Classes.getClass(string);
    }
}
