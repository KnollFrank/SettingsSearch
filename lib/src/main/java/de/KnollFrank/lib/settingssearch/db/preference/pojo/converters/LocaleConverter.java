package de.KnollFrank.lib.settingssearch.db.preference.pojo.converters;

import androidx.room.TypeConverter;

import java.util.List;
import java.util.Locale;

public class LocaleConverter implements Converter<Locale, String> {

    @TypeConverter
    @Override
    public String convertForward(final Locale locale) {
        return getLanguageTag(locale);
    }

    @TypeConverter
    @Override
    public Locale convertBackward(final String languageTag) {
        return getLocale(languageTag);
    }

    public static List<String> getLanguageTags(final List<Locale> locales) {
        return locales
                .stream()
                .map(LocaleConverter::getLanguageTag)
                .toList();
    }

    public static List<Locale> getLocales(final List<String> languageTags) {
        return languageTags
                .stream()
                .map(LocaleConverter::getLocale)
                .toList();
    }

    private static String getLanguageTag(final Locale locale) {
        return locale.toLanguageTag();
    }

    private static Locale getLocale(final String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }
}
