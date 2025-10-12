package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class LocaleConverter implements Converter<String, Locale> {

    @TypeConverter
    @Override
    public Locale doForward(final String language) {
        return new Locale(language);
    }

    @TypeConverter
    @Override
    public String doBackward(final Locale locale) {
        return locale.getLanguage();
    }

    public Set<Locale> doForward(final Set<String> locales) {
        return locales
                .stream()
                .map(this::doForward)
                .collect(Collectors.toSet());
    }
}
