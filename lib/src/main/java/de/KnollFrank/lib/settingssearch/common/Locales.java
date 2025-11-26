package de.KnollFrank.lib.settingssearch.common;

import android.content.res.Resources;

import java.util.Locale;

public class Locales {

    public static Locale getLanguageLocale(final Locale locale) {
        return new Locale(locale.getLanguage());
    }

    public static Locale getCurrentLanguageLocale(final Resources resources) {
        return getLanguageLocale(getCurrentLocale(resources));
    }

    private static Locale getCurrentLocale(final Resources resources) {
        return resources.getConfiguration().getLocales().get(0);
    }
}
