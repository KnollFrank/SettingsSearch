package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.common.Classes;

class ClassConverter implements Converter<Class<?>, String> {

    @TypeConverter
    @Override
    public String convertForward(final Class<?> aClass) {
        return aClass.getName();
    }

    @TypeConverter
    @Override
    public Class<?> convertBackward(final String className) {
        return Classes.getClass(className);
    }
}
