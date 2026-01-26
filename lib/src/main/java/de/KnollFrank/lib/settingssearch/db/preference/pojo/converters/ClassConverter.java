package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import de.KnollFrank.lib.settingssearch.common.Classes;

class ClassConverter<T> implements Converter<Class<? extends T>, String> {

    @TypeConverter
    @Override
    public String convertForward(final Class<? extends T> aClass) {
        return aClass.getName();
    }

    @TypeConverter
    @Override
    public Class<? extends T> convertBackward(final String className) {
        return Classes.getClass(className);
    }
}
