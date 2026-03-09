package de.KnollFrank.lib.settingssearch.common;

import android.os.LocaleList;

import androidx.annotation.Size;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleConverter;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.converters.LocaleListConverter;

public class Locales {

    private Locales() {
    }

    public static Locale getCurrentLocale(final @Size(min = 1) LocaleList localeList) {
        return localeList.get(0);
    }

    public static Locale getDisplayLocale(final @Size(min = 1) List<Locale> supportedLocales,
                                          final List<Locale> desiredLocales) {
        if (supportedLocales.isEmpty()) {
            throw new IllegalArgumentException("supportedLocales must not be empty");
        }
        return Locales
                .findBestSupportedLocaleForDesiredLocales(supportedLocales, desiredLocales)
                .orElseGet(() -> getPrimaryLocale(supportedLocales));
    }

    private static Optional<Locale> findBestSupportedLocaleForDesiredLocales(final List<Locale> supportedLocales,
                                                                             final List<Locale> desiredLocales) {
        return Locales
                .getFirstMatch(desiredLocales, supportedLocales)
                .flatMap(bestDesiredLocale -> getFirstMatch(supportedLocales, List.of(bestDesiredLocale)));
    }

    private static Optional<Locale> getFirstMatch(final List<Locale> haystack, final List<Locale> needles) {
        return Optional.ofNullable(
                new LocaleListConverter()
                        .convertBackward(haystack)
                        .getFirstMatch(
                                LocaleConverter.getLanguageTags(needles).toArray(String[]::new)));
    }

    private static Locale getPrimaryLocale(final @Size(min = 1) List<Locale> locales) {
        return locales.get(0);
    }
}