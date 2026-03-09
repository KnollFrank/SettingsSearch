package de.KnollFrank.lib.settingssearch.common;

import android.os.LocaleList;

import androidx.annotation.Size;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;

public class Locales {

    private Locales() {
    }

    public static Locale getCurrentLocaleOrDefault(final LocaleList localeList) {
        return getCurrentLocale(localeList.isEmpty() ? LocaleList.getAdjustedDefault() : localeList);
    }

    public static Locale getActualUsedLocale(final LocaleList systemLocales, final List<Locale> appLocales) {
        return Locales
                .getFirstMatch(systemLocales, appLocales)
                .or(() -> appLocales.stream().findFirst())
                .orElseGet(() -> getCurrentLocaleOrDefault(systemLocales));
    }

    private static Locale getCurrentLocale(final @Size(min = 1) LocaleList localeList) {
        return localeList.get(0);
    }

    private static Optional<Locale> getFirstMatch(final LocaleList haystack, final List<Locale> needles) {
        return Optional.ofNullable(
                haystack.getFirstMatch(
                        LocaleConverter.getLanguageTags(needles).toArray(String[]::new)));
    }
}