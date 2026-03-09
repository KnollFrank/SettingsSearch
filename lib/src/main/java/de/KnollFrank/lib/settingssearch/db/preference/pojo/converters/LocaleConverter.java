package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.Locale;

public class LocaleConverter implements Converter<Locale, String> {

    @TypeConverter
    @Override
    public String convertForward(final Locale locale) {
        return locale.toLanguageTag();
    }

    @TypeConverter
    @Override
    public Locale convertBackward(final String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }
}
