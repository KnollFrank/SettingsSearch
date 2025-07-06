package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Set;
import java.util.stream.Collectors;

// FK-TODO: rename to LocaleConverter
public class Locale2Converter implements Converter<String, java.util.Locale> {

    @TypeConverter
    @Override
    public java.util.Locale doForward(final String language) {
        return new java.util.Locale(language);
    }

    @TypeConverter
    @Override
    public String doBackward(final java.util.Locale locale) {
        return locale.getLanguage();
    }

    public Set<java.util.Locale> doForward(final Set<String> locales) {
        return locales
                .stream()
                .map(this::doForward)
                .collect(Collectors.toSet());
    }
}
