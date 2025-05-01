package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.preference.PreferenceFragmentCompat;
import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.common.Classes;

public class PreferenceFragmentClassConverter implements Converter<Class<? extends PreferenceFragmentCompat>, String> {

    @TypeConverter
    @Override
    public String doForward(final Class<? extends PreferenceFragmentCompat> clazz) {
        return clazz.getName();
    }

    @TypeConverter
    @Override
    public Class<? extends PreferenceFragmentCompat> doBackward(final String string) {
        return Classes.getClass(string);
    }
}
